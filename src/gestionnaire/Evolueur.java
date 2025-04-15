package gestionnaire;

import static gestionnaire.Etat.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cartographie.EtatPartie;
import cartographie.PlanDeJeu;
import composants.Adversaire;
import composants.ElementJeu;
import composants.Munition;
import mecaniques.DeplacementAdv;
import mecaniques.Trajectoire;
import mouvement.Geometrie;
import outils.Constantes;
import outils.Journalisateur;
import outils.NivJournal;
import surveillant.Surveille;

public class Evolueur extends Surveille {
    private GenerateurEnnemi spawner;

    private EvolutionThread evolutionThread;

    private volatile Etat state;
    private volatile boolean evolving;

    private EtatPartie gs;
    private PlanDeJeu map;

    private List<Adversaire> retarget;
    private Map<Adversaire, Integer> toSpawn;

    public Evolueur(GenerateurEnnemi spawner, EtatPartie gs) {
        state = TERMINATED;
        evolving = false;
        evolutionThread = new EvolutionThread();
        this.gs = gs;
        this.map = gs.getGameMap();
        this.spawner = spawner;

        retarget = new ArrayList<>();
        toSpawn = new HashMap<>();

    }

    public Etat getState() {
        return state;
    }

    private class EvolutionThread extends Thread {
        private long delay;

        @Override
        public void run() {
            Journalisateur.getInstance().log("Evolver Thread Running", NivJournal.STATUS);
            while (evolving) {
                long start = System.nanoTime();

                updateGameState();
                delay = Math.round((System.nanoTime() - start) / 1000000.0);
                Journalisateur.getInstance().updateDelay(delay);
                long sleepTime = Constantes.REFRESH_DELAY - delay;
                if (sleepTime <= 0) {
                    Journalisateur.getInstance().log("Lagging! No sleep!", NivJournal.WARNING);
                } else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException ignore) {}
                }
            }
            Journalisateur.getInstance().log("Evolver Thread Terminated", NivJournal.STATUS);
        }

        public void cancel() {
            evolving = false;

        }
    }

    private void updateGameState() {
        gs.increaseTime(Constantes.REFRESH_DELAY);
        notifyObservers(Constantes.OBSERVER_TIME_MODIFIED);

        checkGameEnd();
        updateSpawns();
        updateEnemies();
        updateProjectiles();
        updateTowers();
        updateRetargettingEnemies();
        notifyObservers(Constantes.OBSERVER_GAME_TICK);
    }

    private void updateTowers() {
        synchronized (map.getTowers()) {
            map.getTowers().forEach(t -> {
                t.decrementCooldown(Constantes.REFRESH_DELAY);
                if (t.canFire()) {
                    double range = t.getRange();
                    Point towerPt = new Point((int) t.getX(), (int) t.getY());
                    synchronized (map.getEnemies()) {
                        for (Adversaire e : map.getEnemies()) {
                            Point enemyPt = new Point((int) e.getX(), (int) e.getY());
                            if (Geometrie.getDistance(towerPt, enemyPt) < range) {
                                map.addComponent(new Munition(e, t, t.getProjectileType()));
                                t.setFired();
                                break;
                            }
                        }
                    }
                }
            });
        }
    }

    private void updateRetargettingEnemies() {
        synchronized (map.getTargetMap()) {
            Map<Adversaire, List<Munition>> target = map.getTargetMap();

            retarget.forEach(e -> {
                List<Munition> projs = target.get(e);
                if (projs != null) {
                    projs.forEach(p -> {
                        synchronized (map.getEnemies()) {
                            for (Adversaire e2 : map.getEnemies()) {
                                if (e2.getX() > 0 && e2.getY() > 0) {
                                    if (!retarget.contains(e2)) {
                                        if (Geometrie.withinRange(e2, p.getTower(), p.getTower().getRange())) {
                                            p.setTarget(e2);
                                            map.setTarget(p, e2);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            });

            retarget.clear();
        }
    }

    private void updateProjectiles() {
        List<ElementJeu> temp = new ArrayList<>();
        synchronized (map.getProjectiles()) {
            map.getProjectiles().forEach(p -> {
                boolean hitEnemy = Trajectoire.updateProjectile(p, Constantes.REFRESH_DELAY);
                if (hitEnemy) {
                    Adversaire e = p.getTarget();
                    boolean dead = e.takeDmg(p.getDmg());
                    if (dead) {
                        temp.add(e);
                        retarget.add(e);
                        gs.gainGold(e.getKillGold());
                    }
                    temp.add(p);

                }
            });
        }

        temp.forEach(e -> map.removeComponent(e));

    }

    private void updateEnemies() {
        List<Adversaire> temp = new ArrayList<>();
        synchronized (map.getEnemies()) {
            map.getEnemies().forEach(e -> {
                boolean reachedBase = DeplacementAdv.updateEnemy(e, Constantes.REFRESH_DELAY);
                if (reachedBase) {
                    temp.add(e);
                    map.getBase().takeDmg(e.getDmg());
                    notifyObservers(Constantes.OBSERVER_BASE_HEALTH_CHANGED);
                }
            });
        }
        temp.forEach(e -> map.removeComponent(e));

    }

    private void updateSpawns() {

        Map<Adversaire, Integer> replace = new HashMap<>();

        if (!toSpawn.isEmpty()) {
            toSpawn.forEach((k, v) -> {
                v = v - Constantes.REFRESH_DELAY;
                if (v <= 0) {
                    map.addComponent(k);
                } else {
                    replace.put(k, v);
                }
            });
        }
        toSpawn = replace;

        if (!spawner.isDoneSpawn()) {
            Map<Adversaire, Integer> add = spawner.update(Constantes.REFRESH_DELAY);
            if (add != null)
                add.forEach((k, v) -> toSpawn.put(k, v));
        }

    }

    private void checkGameEnd() {
        if (map.getBase().isExploded()) {
            System.out.println("Level failed");
            changeState(TERMINATED);
            gs.levelFailed();
        }

        if (spawner.isDoneSpawn()) {
            if (map.getEnemies().isEmpty() && toSpawn.isEmpty()) {
                System.out.println("Level complete");
                changeState(TERMINATED);
                gs.levelCompleted();

            }
        }
    }

    public void changeState(Etat s) {
        System.out.println("Trying to set state to " + s.toString());
        if (s == state) {
            Journalisateur.getInstance().log("Trying to make same state", NivJournal.WARNING);
            return;
        }

        if (s == Etat.PLAYING) {
            if (evolutionThread == null) {
                evolutionThread = new EvolutionThread();
            } else {
                evolutionThread = new EvolutionThread();
                System.out.println("Evolution thread not null?");
            }

            if (state == Etat.PAUSED) {
                evolving = true;
                evolutionThread.start();
            } else if (state == TERMINATED) {
                evolving = true;
                evolutionThread.start();
            }
            state = s;
            System.out.println("Game Running!");
            notifyObservers(Constantes.OBSERVER_GAME_RESUMED);
            notifyObservers(Constantes.OBSERVER_STATE_RUNNING);

        } else if (s == Etat.PAUSED) {
            evolutionThread.cancel();
            System.out.println("Evolution Thread State " + evolutionThread.getState());
            evolving = false;
            evolutionThread.interrupt();
            evolutionThread = null;

            state = s;
            notifyObservers(Constantes.OBSERVER_GAME_PAUSED);
            notifyObservers(Constantes.OBSERVER_STATE_PAUSED);
        } else if (s == TERMINATED) {
            evolutionThread.cancel();
            try {
                evolutionThread.join(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            evolving = false;
            evolutionThread.interrupt();
            evolutionThread = null;

            state = s;
            notifyObservers(Constantes.OBSERVER_STATE_TERMINATED);
        }
    }
}
