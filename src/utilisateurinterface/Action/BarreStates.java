package utilisateurinterface.Action;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import categories.GenreTour;
import compInterface.BoutonAction;
import compInterface.BoutonAmTour;
import compInterface.EtiquetteTexte;
import compInterface.Volet;
import composants.Tour;
import gestionnaire.Deroulement;
import outils.Composant;
import outils.Constantes;
import surveillant.Gardien;
import surveillant.Surveille;

public class BarreStates extends Volet implements Gardien, ActionListener {
    private Barre hudPane;
    private Deroulement game;

    private List<BoutonAmTour> towerUpgrades;
    private EtiquetteTexte type;
    private BoutonAction sellTower;
    private JPanel upgradesPane;

    public BarreStates(Barre hudPane, Deroulement game) {
        this.hudPane = hudPane;
        this.game = game;

        towerUpgrades = new ArrayList<>();
        type = new EtiquetteTexte("", EtiquetteTexte.MEDIUM, EtiquetteTexte.CENTER);

        sellTower = new BoutonAction("Sell ()");
        sellTower.setFont(Constantes.MEDIUM_LABEL_FONT);

        upgradesPane = new JPanel();

        sellTower.addActionListener(this);

        Composant.add(type, this, 0, 0, 1, 1, 0, 0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        Composant.add(sellTower, this, 0, 1, 1, 1, 0, 0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        Composant.add(upgradesPane, this, 0, 2, 1, 1, 0, 0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        this.setPreferredSize(Constantes.HUD_STATS_SIZE);
        upgradesPane.setPreferredSize(
                new Dimension((int) this.getPreferredSize().getWidth(),
                        (int) (this.getPreferredSize().getHeight() * 0.5)));

        this.setVisible(false);
    }

    @Override
    public void update(Surveille o, String msg) {
        if (msg.equals(Constantes.OBSERVER_TOWER_SELECTED) ||
                msg.equals(Constantes.OBSERVER_SOLD_TOWER) ||
                msg.equals(Constantes.OBSERVER_UPGRADED_TOWER) ||
                msg.equals(Constantes.OBSERVER_NEW_GAME)) {
            System.out.println("Refresh Selected Tower");
            displayTowerInfo(game.getSelectedTower());
        }
    }

    private void displayTowerInfo(Tour selectedTower) {

        System.out.println("Updating Stats Pane");

        upgradesPane.removeAll();
        towerUpgrades.clear();

        if (selectedTower != null) {
            List<GenreTour> upgradeTypes = selectedTower.getUpgrades();
            for (GenreTour tt : upgradeTypes) {
                towerUpgrades.add(createUpgradeButton(tt));
            }

            type.setText(selectedTower.getTypeName());
            sellTower.setText("Sell " + selectedTower.getSellPrice() + " (g)");

            for (int i = 0; i < towerUpgrades.size(); i++) {
                Composant.add(towerUpgrades.get(i), upgradesPane, i % 3, i / 3, 1, 1, 1, 1, GridBagConstraints.BOTH,
                        GridBagConstraints.CENTER);

            }
            this.setVisible(true);
        } else {
            this.setVisible(false);
        }
        SwingUtilities.invokeLater(() -> this.repaint());
    }

    private BoutonAmTour createUpgradeButton(GenreTour tt) {
        BoutonAmTour b = new BoutonAmTour(tt, game.getSelectedTower());
        b.addActionListener(this);
        return b;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (BoutonAmTour b : towerUpgrades) {
            if (e.getSource() == b) {
                game.attemptUpgradeTower(b.getUpgradeType());
                return;
            }
        }

        if (e.getSource() == sellTower) {
            game.attemptSellTower();

        }

    }
}
