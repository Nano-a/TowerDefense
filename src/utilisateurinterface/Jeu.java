package utilisateurinterface;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import categories.GenreTour;
import compInterface.BoutonIcone;
import gestionnaire.Deroulement;
import outils.Composant;
import outils.Constantes;
import surveillant.Gardien;
import surveillant.Surveille;
import utilisateurinterface.Action.Barre;
import utilisateurinterface.Action.BarreBoutton;
import utilisateurinterface.Action.BarreJeu;

public class Jeu extends JLayeredPane implements ActionListener, Gardien {

    private CadreJeu tdf;
    private Deroulement game;

    private Barre hudPane;
    private BarreJeu gamePane;
    private MenuPane menuPane;

    private BoutonIcone pause;

    public Jeu(CadreJeu tdf) {
        this.tdf = tdf;

        gamePane = new BarreJeu();
        game = gamePane.getGameProcess();

        hudPane = new Barre(game);

        game.addObserver(this);
        game.addObserver(hudPane.getHUDStatsPane());
        game.addObserver(hudPane.getHUDStatePane());
        game.addObserver(hudPane);
        game.addObserver(gamePane);

        menuPane = new MenuPane();

        Composant.add(gamePane, this, 0, 0, 0, 1, 1, 1, 1,
                GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        Composant.add(hudPane, this, 1, 0, 1, 1, 1, 1, 0,
                GridBagConstraints.NONE, GridBagConstraints.BASELINE, 0, 0, 10, 0);

        Composant.add(menuPane, this, 1, 0, 0, 1, 1, 1, 1,
                GridBagConstraints.NONE, GridBagConstraints.FIRST_LINE_END);

        hudPane.getHUDButtonsPane().addPropertyChangeListener(new TowerChangeListener());
    }

    public void startNewGame(int level) {
        game.startNewGame(level);
    }

    public void resumeGame() {
        game.resumeGame();
    }

    public void restartGame() {
        game.restartGame();
    }

    @Override
    public void update(Surveille o, String msg) {
        if (msg.equals(Constantes.OBSERVER_LEVEL_FAILED) || msg.equals(Constantes.OBSERVER_LEVEL_COMPLETE)) {
            tdf.toggleMenu(true, msg);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == pause) {
            System.out.println("Pausing Game");
            game.pauseGame();
            tdf.toggleMenu(true);
        }

    }

    private class TowerChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(BarreBoutton.TOWER_CHANGED)) {
                game.setBuildTowerType((GenreTour) evt.getNewValue());
            }
        }
    }

    private class MenuPane extends JPanel {
        public MenuPane() {
            this.setPreferredSize(Constantes.ICON_SIZE); // TEMP SIZE.
            this.setOpaque(false);
            pause = new BoutonIcone("Pause"); // Show menu;
            pause.addActionListener(Jeu.this::actionPerformed);
            Composant.add(pause, this, 0, 0, 1, 1, 1, 1,
                    GridBagConstraints.NONE, GridBagConstraints.FIRST_LINE_END);
        }
    }

}
