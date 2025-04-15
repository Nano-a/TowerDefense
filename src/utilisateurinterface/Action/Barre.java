package utilisateurinterface.Action;

import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import categories.GenreTour;
import compInterface.Volet;
import gestionnaire.Deroulement;
import outils.Composant;
import outils.Constantes;
import surveillant.Gardien;
import surveillant.Surveille;

public class Barre extends JPanel implements Gardien {
    private BarreBoutton hudButtonPane;
    private BarreStates hudStatsPane;
    private VoletState hudStatePane;

    private GenreTour selectedTowerType;

    public Barre(Deroulement game) {
        this.setPreferredSize(Constantes.HUD_DIMENSION);
        this.setOpaque(false);

        hudButtonPane = new BarreBoutton(this, game);
        hudButtonPane.addPropertyChangeListener(new TowerChangeListener());

        hudStatsPane = new BarreStates(this, game);
        hudStatePane = new VoletState(this, game);

        Volet statsSpaceHolderPane = new Volet();

        Composant.add(hudStatsPane, statsSpaceHolderPane, 0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH,
                GridBagConstraints.CENTER);

        Composant.add(hudButtonPane, this, 0, 0, 1, 1, 1, 1,
                GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        Composant.add(statsSpaceHolderPane, this, 1, 0, 1, 1, 1, 1,
                GridBagConstraints.BOTH, GridBagConstraints.WEST);

        Composant.add(hudStatePane, this, 2, 0, 1, 1, 1, 1,
                GridBagConstraints.BOTH, GridBagConstraints.EAST);

        statsSpaceHolderPane.setPreferredSize(Constantes.HUD_STATS_SIZE);
        statsSpaceHolderPane.setMaximumSize(Constantes.HUD_STATS_SIZE);
        statsSpaceHolderPane.setMinimumSize(Constantes.HUD_STATS_SIZE);
        statsSpaceHolderPane.setSize(Constantes.HUD_STATS_SIZE);

    }

    public GenreTour getSelectedTowerType() {
        return selectedTowerType;
    }

    public void setSelectedTowerType(GenreTour towerName) {


    }

    public BarreBoutton getHUDButtonsPane() {
        return hudButtonPane;
    }

    public BarreStates getHUDStatsPane() {
        return hudStatsPane;
    }

    @Override
    public void update(Surveille o, String msg) {

    }

    public VoletState getHUDStatePane() {
        return hudStatePane;
    }

    private class TowerChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(BarreBoutton.TOWER_CHANGED)) {
                Barre.this.setSelectedTowerType((GenreTour) evt.getNewValue());
            }
        }
    }
}
