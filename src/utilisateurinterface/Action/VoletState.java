package utilisateurinterface.Action;

import java.awt.Color;
import java.awt.GridBagConstraints;

import javax.swing.SwingUtilities;

import compInterface.EtiquetteTexte;
import compInterface.Volet;
import gestionnaire.Deroulement;
import outils.Composant;
import outils.Constantes;
import surveillant.Gardien;
import surveillant.Surveille;

public class VoletState extends Volet implements Gardien {

    private Barre hudPane;
    private Deroulement game;

    private EtiquetteTexte lGold;
    private EtiquetteTexte lTime;
    private EtiquetteTexte lLevel;
    private EtiquetteTexte lWave;
    private EtiquetteTexte lNextWave;
    private EtiquetteTexte lHealth;

    public VoletState(Barre hudPane, Deroulement game) {
        this.hudPane = hudPane;
        this.game = game;

        lGold = new EtiquetteTexte("Gold ", EtiquetteTexte.MEDIUM, EtiquetteTexte.LEFT, EtiquetteTexte.INVISIBLE);
        lTime = new EtiquetteTexte("Time ", EtiquetteTexte.MEDIUM, EtiquetteTexte.LEFT, EtiquetteTexte.INVISIBLE);
        lLevel = new EtiquetteTexte("Level ", EtiquetteTexte.MEDIUM, EtiquetteTexte.LEFT, EtiquetteTexte.INVISIBLE);
        lWave = new EtiquetteTexte("Wave ", EtiquetteTexte.MEDIUM, EtiquetteTexte.LEFT, EtiquetteTexte.INVISIBLE);
        lNextWave = new EtiquetteTexte("Next Wave In ", EtiquetteTexte.MEDIUM, EtiquetteTexte.LEFT, EtiquetteTexte.INVISIBLE);
        lHealth = new EtiquetteTexte("Health ", EtiquetteTexte.MEDIUM, EtiquetteTexte.LEFT, EtiquetteTexte.INVISIBLE);

        resetText();

        Composant.add(lGold, this, 0, 0, 2, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER,
                Constantes.INSETS_BETWEEN, Constantes.INSETS_OUTSIDE, Constantes.INSETS_BETWEEN, Constantes.INSETS_OUTSIDE);
        Composant.add(lTime, this, 0, 1, 2, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER,
                Constantes.INSETS_BETWEEN, Constantes.INSETS_OUTSIDE, Constantes.INSETS_BETWEEN, Constantes.INSETS_OUTSIDE);
        Composant.add(lLevel, this, 0, 2, 2, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER,
                Constantes.INSETS_BETWEEN, Constantes.INSETS_OUTSIDE, Constantes.INSETS_BETWEEN, Constantes.INSETS_OUTSIDE);
        Composant.add(lWave, this, 0, 3, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER,
                Constantes.INSETS_BETWEEN, Constantes.INSETS_OUTSIDE, Constantes.INSETS_BETWEEN, Constantes.INSETS_OUTSIDE);
        Composant.add(lNextWave, this, 1, 3, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER,
                Constantes.INSETS_BETWEEN, Constantes.INSETS_OUTSIDE, Constantes.INSETS_BETWEEN, Constantes.INSETS_OUTSIDE);
        Composant.add(lHealth, this, 0, 4, 2, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER,
                Constantes.INSETS_BETWEEN, Constantes.INSETS_OUTSIDE, Constantes.INSETS_BETWEEN, Constantes.INSETS_OUTSIDE);

        this.setBackground(Color.WHITE);

        this.setMinimumSize(Constantes.HUD_STATE_SIZE);
        this.setPreferredSize(Constantes.HUD_STATE_SIZE);
    }

    @Override
    public void update(Surveille o, String msg) {
        if (msg.equals(Constantes.OBSERVER_GOLD_CHANGED)) {
            updateGold();
        } else if (msg.equals(Constantes.OBSERVER_NEW_GAME)) {
            updateGold();
            updateTime();
            updateWave();
            updateLevel();
            updateHealth();
        } else if (msg.equals(Constantes.OBSERVER_TIME_MODIFIED)) {
            updateTime();
        } else if (msg.equals(Constantes.OBSERVER_WAVE_SPAWNED)) {
            updateWave();
        } else if (msg.equals(Constantes.OBSERVER_BASE_HEALTH_CHANGED)) {
            updateHealth();
        } else if (msg.equals(Constantes.OBSERVER_STATE_TERMINATED)) {
            // Reset HUD.
            resetText();
        }
    }

    private void resetText() {
        lGold.setText("Gold");
        lTime.setText("Time");
        lLevel.setText("Level");
        lWave.setText("Wave");
        lNextWave.setText("Next Wave in: ");
        lHealth.setText("Health");

    }

    private void updateTime() {
        int time = (int) Math.round(game.getGameState().getTimeMS() / 1000.0);
        int minutes = time / 60;
        int seconds = time % 60;
        String sMin, sSec;
        if (minutes < 10) {
            sMin = "0" + String.valueOf(minutes);
        } else {
            sMin = String.valueOf(minutes);
        }
        if (seconds < 10) {
            sSec = "0" + String.valueOf(seconds);
        } else {
            sSec = String.valueOf(seconds);
        }

        SwingUtilities.invokeLater(() -> {
            lTime.setText("Time " + sMin + ":" + sSec);
            if (!game.isDoneSpawn()) {
                lNextWave.setText("Next Wave " + game.getTimeToNextWave());
            } else {
                lNextWave.setText("");
            }
        });

    }

    private void updateGold() {
        SwingUtilities.invokeLater(() -> lGold.setText("Gold " + game.getGameState().getGold()));
    }

    private void updateWave() {
        SwingUtilities.invokeLater(() -> lWave.setText("Wave " + game.getWave()));
    }

    private void updateLevel() {
        SwingUtilities.invokeLater(() -> lLevel.setText("Level " + game.getGameState().getLevel()));
    }

    private void updateHealth() {
        SwingUtilities.invokeLater(() -> lHealth.setText("Health " + game.getGameState().getBaseHealth()));
    }
}
