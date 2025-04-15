package cartographie;

import categories.GenreTour;
import composants.Tour;
import mecaniques.AnalyseurNivJson;
import outils.Constantes;
import surveillant.Surveille;


public class EtatPartie extends Surveille {

    private PlanDeJeu carte;

    private AnalyseurNivJson levelParser;

    private int gold;
    private int score;
    private int timeElapsed;
    private int level;
    private GenreTour buildTowerType;
    private Tour selectedTower;

    public EtatPartie() {
        carte = new PlanDeJeu();
        levelParser = new AnalyseurNivJson();
        reset();
    }

    public void reset() {
        carte.reset();
        gold = Constantes.DEFAULT_GOLD;
        score = 0;
        timeElapsed = 0;
        buildTowerType = null;
        selectedTower = null;
    }

    public void setSelectedTower(Tour t) {
        if (t == selectedTower)
            return;

        if (selectedTower != null)
            selectedTower.setSelected(false);

        selectedTower = t;

        if (selectedTower != null)
            selectedTower.setSelected(true);

        notifyObservers(Constantes.OBSERVER_TOWER_SELECTED);
    }

    public Tour getSelectedTower() {
        return selectedTower;
    }

    public void setBuildTowerType(GenreTour tt) {
        buildTowerType = tt;
    }

    public GenreTour getBuildTowerType() {
        return buildTowerType;
    }

    public int getGold() {
        return gold;
    }

    public String getBaseHealth() {
        return carte.getBase().getHealth();
    }

    public int getScore() {
        return score;
    }

    public void levelCompleted() {
        reset();
        notifyObservers(Constantes.OBSERVER_LEVEL_COMPLETE);
    }

    public void levelFailed() {
        reset();
        notifyObservers(Constantes.OBSERVER_LEVEL_FAILED);
    }

    public void increaseTime(int timeMS) {
        timeElapsed += timeMS;
    }

    public PlanDeJeu getGameMap() {
        return carte;
    }

    public void useGold(int gold) {
        this.gold -= gold;
        notifyObservers(Constantes.OBSERVER_GOLD_CHANGED);
    }

    public void gainGold(int gold) {
        this.gold += gold;
        notifyObservers(Constantes.OBSERVER_GOLD_CHANGED);
    }

    public int getTimeMS() {
        return timeElapsed;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
