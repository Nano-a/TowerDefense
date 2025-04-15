package cartographie;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

import categories.NatureTerrain;
import composants.Adversaire;
import composants.ChampBataille;
import composants.ElementJeu;
import composants.Fortresse;
import composants.Munition;
import composants.Tour;
import mecaniques.Cartographe;
import mecaniques.Chemin;
import outils.Constantes;

public class PlanDeJeu {
    private List<ElementJeu> gameComponents;

    private ChampBataille[][] terrains;
    private ChampBataille start, destination;

    private List<ChampBataille> path;

    private Double blockSize;
    private List<Tour> towers;
    private List<Adversaire> enemies;
    private List<Munition> projectiles;
    private List<Fortresse> bases;

    private Map<Adversaire, List<Munition>> targetMap;
    private Chemin pathBuilder;

    private ExecutorService pool;

    private Lock gcLock;

    public Lock getGcLock() {
        return gcLock;
    }

    public PlanDeJeu() {
        pool = Executors.newFixedThreadPool(10);
        gameComponents = new ArrayList<>();
        reset();
    }

    public void loadLevel(String levelTerrainFile) {
        NatureTerrain[][] terrainTypes = Cartographe.parseTerrainFile(levelTerrainFile);
        buildTerrain(terrainTypes);
        this.blockSize = Constantes.DEFAULT_BLOCK_SIZE;
    }

    public void reset() {
        synchronized (gameComponents) {
            long startT = System.nanoTime();
            this.gameComponents.clear();
            towers = new ArrayList<>();
            enemies = new ArrayList<>();
            projectiles = new ArrayList<>();
            bases = new ArrayList<>();
            targetMap = new HashMap<>();
            path = new ArrayList<>();
            long endT = System.nanoTime();
        }
    }

    private void buildTerrain(NatureTerrain[][] terrainTypes) {

        int width = (int) Math.round(Constantes.DEFAULT_BLOCK_SIZE);
        int height = (int) Math.round(Constantes.DEFAULT_BLOCK_SIZE);
        terrains = new ChampBataille[terrainTypes.length][terrainTypes[0].length];
        for (int i = 0; i < terrainTypes.length; i++) {
            for (int j = 0; j < terrainTypes[i].length; j++) {
                terrains[i][j] = new ChampBataille(terrainTypes[i][j], j, i, width, height);
                if (terrainTypes[i][j] == NatureTerrain.NEXUS) {
                    System.out.println("Found base");
                    Fortresse base = new Fortresse(1000, terrains[i][j], null);
                    bases.add(base);
                    gameComponents.add(base);
                    destination = terrains[i][j];
                } else if (terrainTypes[i][j] == NatureTerrain.START) {
                    System.out.println("Found start!");
                    start = terrains[i][j];
                }
            }
        }

        pathBuilder = new Chemin(terrains);
        path = pathBuilder.generatePath(start, destination);
    }

    public ChampBataille getStart() {
        return start;
    }

    private class Remover implements Runnable {
        private ElementJeu gc;

        public Remover(ElementJeu gc) {
            this.gc = gc;
        }

        @Override
        public void run() {
            synchronized (gameComponents) {
                gameComponents.remove(gc);
            }
            if (gc instanceof Munition) {
                Munition p = (Munition) gc;
                synchronized (projectiles) {
                    projectiles.remove(p);
                }
                synchronized (targetMap) {
                    Adversaire e = p.getTarget();
                    if (e != null && targetMap.get(e) != null) {
                        targetMap.get(e).remove(p);
                    }
                }

            } else if (gc instanceof Adversaire) {
                Adversaire e = (Adversaire) gc;
                synchronized (enemies) {
                    enemies.remove(e);
                }
                synchronized (targetMap) {
                    targetMap.remove(e);
                }

            } else if (gc instanceof Tour) {
                synchronized (towers) {
                    towers.remove(gc);
                }

            } else if (gc instanceof Fortresse) {
                synchronized (bases) {
                    bases.remove(gc);
                }
            } else {
                System.out.println("Wrong component type");
            }
        }
    }
    private class Adder implements Runnable {
        private ElementJeu gc;
        public Adder(ElementJeu gc) {
            this.gc = gc;
        }

        @Override
        public void run() {
            if (gc instanceof Munition) {
                Munition p = (Munition) gc;
                synchronized (projectiles) {
                    projectiles.add((Munition) gc);
                }
                synchronized (targetMap) {
                    if (p.getTarget() == null) {} else {
                        targetMap.get(p.getTarget()).add(p);
                    }
                }

            } else if (gc instanceof Adversaire) {
                Adversaire e = (Adversaire) gc;
                e.setPath(path);
                synchronized (enemies) {
                    enemies.add(e);
                }
                synchronized (targetMap) {
                    targetMap.put(e, new ArrayList<>());
                }

            } else if (gc instanceof Tour) {
                synchronized (towers) {
                    towers.add((Tour) gc);
                }

            } else if (gc instanceof Fortresse) {
                synchronized (bases) {
                    bases.add((Fortresse) gc);
                }

            } else {
                System.out.println("Wrong component type");
            }

            synchronized (gameComponents) {
                gameComponents.add(gc);
            }
        }
    }

    public void addComponent(ElementJeu gc) {
        pool.execute(new Adder(gc));
    }

    public void removeComponent(ElementJeu gc) {
        pool.execute(new Remover(gc));
    }

    public ChampBataille[][] getTerrains() {
        return terrains;
    }

    public Double getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(Double blockSize) {
        this.blockSize = blockSize;

    }

    public void setTarget(Munition p, Adversaire e) {
        synchronized (targetMap) {
            targetMap.get(e).add(p);
        }
    }

    public List<ElementJeu> getGameComponents() {
        return gameComponents;
    }

    public Map<Adversaire, List<Munition>> getTargetMap() {
        return targetMap;
    }

    public Fortresse getBase() {
        return bases.get(0);
    }

    public List<Adversaire> getEnemies() {
        return enemies;
    }

    public List<Munition> getProjectiles() {
        return projectiles;
    }

    public List<Tour> getTowers() {
        return towers;
    }

    public Dimension getMapSize() {
        double width = Constantes.DEFAULT_BLOCK_SIZE * terrains[0].length;
        double height = Constantes.DEFAULT_BLOCK_SIZE * terrains.length;
        return new Dimension((int) width, (int) height);
    }

    public List<ElementJeu> getToDel() {
        return null;
    }

    public List<ElementJeu> getToAdd() {
        return null;
    }

    public List<ChampBataille> generatePath() {
        return null;
    }

}
