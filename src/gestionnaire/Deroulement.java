package gestionnaire;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cartographie.EtatPartie;
import cartographie.PlanDeJeu;
import categories.GenreTour;
import categories.NatureTerrain;
import composants.ChampBataille;
import composants.ElementJeu;
import composants.Tour;
import mecaniques.AnalyseurNivJson;
import mouvement.Geometrie;
import outils.Constantes;
import outils.Journalisateur;
import outils.NivJournal;
import surveillant.Gardien;
import surveillant.Surveille;

public class Deroulement extends Surveille implements Iterable<ElementJeu>, Gardien {

    private EtatPartie state;
    private Evolueur evolver;
    private PlanDeJeu map;
    private GenerateurEnnemi spawner;
    private AnalyseurNivJson levelParser;

    public Deroulement() {
        levelParser = new AnalyseurNivJson();
        spawner = new GenerateurEnnemi();
        state = new EtatPartie();
        evolver = new Evolueur(spawner, state);
        map = state.getGameMap();

        evolver.addObserver(this);
        state.addObserver(this);
    }


    public void mouseClick(Point p) {
        if (isTowerLocationValid(p)) {
            buildTower(p);
        } else {
            adjustTowerSelection(p);
        }
    }

    private void adjustTowerSelection(Point p) {

        Tour selected = state.getSelectedTower();
        if (selected != null) {
            if (Geometrie.isPointInObject(p, selected)) {
                return;
            }
        }

        if (selected != null) {
            state.setSelectedTower(null);
        }

        synchronized (map.getTowers()) {
            for (Tour t : map.getTowers()) {
                if (Geometrie.isPointInObject(p, t)) {
                    state.setSelectedTower(t);
                    break;
                }
            }
        }
    }

    public Point adjustPointToGrid(Point p) {
        return p;
    }

    public boolean isTowerLocationValid(Point p) {
        if (state.getBuildTowerType() == null) {
            return false;
        }

        List<ChampBataille> terrs = getTerrainInRange(p);
        for (ChampBataille t : terrs) {
            if (t.getType() != NatureTerrain.BUILDABLE) {
                return false;
            }
        }
        GenreTour type = state.getBuildTowerType();
        Tour temp = new Tour(p.x - type.getWidth() / 2, p.y - type.getHeight() / 2, type); 
        synchronized (map.getTowers()) {
            for (Tour t : map.getTowers()) {
                if (Geometrie.hasIntersection(temp, t)) {
                    return false;
                }
            }
        }

        return true;
    }

    private List<ChampBataille> getTerrainInRange(Point p) {
        double blockW = Constantes.DEFAULT_BLOCK_SIZE;
        double blockH = Constantes.DEFAULT_BLOCK_SIZE;
        List<ChampBataille> terrs = new ArrayList<>();

        GenreTour tt = state.getBuildTowerType();
        double xMin = p.getX() - tt.getWidth() / 2;
        double yMin = p.getY() - tt.getHeight() / 2;
        double xMax = xMin + tt.getWidth();
        double yMax = yMin + tt.getHeight();

        ChampBataille[][] terrains = map.getTerrains();

        int startI, startJ, endI, endJ;

        if (xMin <= 0)
            startI = 0;
        else
            startI = (int) Math.floor(xMin / blockW);

        if (xMax >= terrains[0].length * blockW)
            endI = terrains[0].length;
        else
            endI = (int) Math.ceil(xMax / blockW);

        if (yMin <= 0)
            startJ = 0;
        else
            startJ = (int) Math.floor(yMin / blockH);

        if (yMax >= terrains.length * blockH)
            endJ = terrains.length;
        else
            endJ = (int) Math.ceil(yMax / blockH);

        for (int j = startJ; j < endJ; j++) {
            for (int i = startI; i < endI; i++) {
                terrs.add(terrains[j][i]);
            }
        }

        return terrs;
    }

    private boolean buildTower(Point p) {
        GenreTour type = state.getBuildTowerType();
        int cost = type.getCost();
        if (state.getGold() >= cost) {
            state.useGold(cost);
            Tour tower = new Tour(p.getX() - type.getWidth() / 2, p.getY() - type.getHeight() / 2, type);
            map.addComponent(tower);
            return true;
        }
        return false;
    }

    public void attemptUpgradeTower(GenreTour upgradeType) {
        if (state.getGold() >= upgradeType.getUpgradeCost()) {
            state.useGold(upgradeType.getUpgradeCost());
            getSelectedTower().modifyType(upgradeType);
            notifyObservers(Constantes.OBSERVER_UPGRADED_TOWER);
            Journalisateur.getInstance().log("Successfully upgraded tower", NivJournal.STATUS);
        } else {
            Journalisateur.getInstance().log("Cannot upgrade tower, insufficient gold", NivJournal.WARNING);
        }
    }

    public void attemptSellTower() {
        state.gainGold((int) Math.round(getSelectedTower().getSellPrice()));
        map.removeComponent(getSelectedTower());
        setSelectedTower(null);
        notifyObservers(Constantes.OBSERVER_SOLD_TOWER);

    }

    @Override
    public void update(Surveille o, String msg) {
        notifyObservers(msg);
    }

    public void startNewGame(int level) {
        System.out.println("Commencement de la partie!!");
        state.reset();
        state.setLevel(level);
        levelParser.readLevel(level);
        map.reset();
        map.loadLevel(levelParser.getFile());
        spawner.loadLevel(levelParser.getSpawnTime(), levelParser.getSpawnQueue(),
                map.getStart().getPixelX(), map.getStart().getPixelY());
        evolver.changeState(Etat.PLAYING);
        notifyObservers(Constantes.OBSERVER_NEW_GAME);
    }

    public void pauseGame() {
        evolver.changeState(Etat.PAUSED);
        notifyObservers(Constantes.OBSERVER_GAME_PAUSED);
    }

    public void resumeGame() {
        evolver.changeState(Etat.PLAYING);
    }

    public void restartGame() {
        startNewGame(state.getLevel());
    }

    public Tour getSelectedTower() {
        return state.getSelectedTower();
    }

    public void setSelectedTower(Tour t) {
        state.setSelectedTower(t);
    }

    public void setBuildTowerType(GenreTour tt) {
        state.setBuildTowerType(tt);
    }

    public GenreTour getBuildTowerType() {
        return state.getBuildTowerType();
    }

    public PlanDeJeu getMap() {
        return map;
    }

    public ChampBataille[][] getTerrain() {
        return map.getTerrains();
    }

    @Override
    public Iterator iterator() {
        return map.getGameComponents().iterator();
    }

    public void gainGold(int i) {
        state.gainGold(i);
    }

    public Etat getState() {
        return evolver.getState();
    }

    public EtatPartie getGameState() {
        return state;
    }

    public boolean isDoneSpawn() {
        return spawner.isDoneSpawn();
    }

    public int getTimeToNextWave() {
        return spawner.getTimeToNextWave();
    }

    public String getWave() {
        return spawner.getWave();
    }

    public Dimension getMapSize() {
        return map.getMapSize();
    }
}
