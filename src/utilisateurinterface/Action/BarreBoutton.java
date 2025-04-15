package utilisateurinterface.Action;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import categories.GenreTour;
import compInterface.BoutonTour;
import compInterface.EtiquetteTexte;
import compInterface.Volet;
import gestionnaire.Deroulement;
import outils.Composant;
import outils.Constantes;


public class BarreBoutton extends Volet
        implements ActionListener {
    private Barre hudPane;
    public static final String TOWER_CHANGED = "Tower Changed";

    private List<BoutonTour> buttons;

    private Volet buttonsPane;
    private EtiquetteTexte lTower;

    public BarreBoutton(Barre hudPane, Deroulement gs) {
        this.hudPane = hudPane;
        buttonsPane = new Volet();

        lTower = new EtiquetteTexte("Towers", EtiquetteTexte.MEDIUM, EtiquetteTexte.CENTER, EtiquetteTexte.INVISIBLE);

        Composant.add(lTower, this, 0, 0, 1, 1, 1, 0,
                GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, 10, 0, 0, 0);
        Composant.add(buttonsPane, this, 0, 1, 1, 1, 1, 1,
                GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        buttons = new ArrayList<>();
        makeAndAddButtons();

        this.setPreferredSize(Constantes.HUD_BUTTON_SIZE);
        this.setMinimumSize(Constantes.HUD_BUTTON_SIZE);
    }

    private void makeAndAddButtons() {
        GenreTour[] basicTowers = GenreTour.BASIC_TOWERS;
        for (GenreTour tt : basicTowers) {
            BoutonTour temp = new BoutonTour(tt);
            buttons.add(temp);
        }

        int x = Constantes.HUD_BUTTONS_PER_ROW;
        for (int i = 0; i < buttons.size(); i++) {
            Insets insets = getInsetsFor(i);
            Composant.add(buttons.get(i), buttonsPane, i % x, i / x, 1, 1, 1, 1,
                    GridBagConstraints.NONE, GridBagConstraints.CENTER, insets);
            buttons.get(i).addActionListener(this);
        }

    }

    private Insets getInsetsFor(int i) {
        int x = Constantes.HUD_BUTTONS_PER_ROW;
        int max = (buttons.size() - 1) / x;

        int top = (i / x == 0) ? 2 : 1;
        int bottom = (i / x == max) ? 2 : 1;

        int left = (i % x == 0) ? 2 : 1;
        int right = (i % x == x - 1) ? 2 : 1;

        right = (i == buttons.size() - 1) ? 2 : right;
        return new Insets(top, left, bottom, right);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        for (BoutonTour b : buttons) {
            if (b == source) {
                if (!b.isSelected()) {
                    firePropertyChange(TOWER_CHANGED, null, b.getType());
                    b.setSelected(true);
                } else {
                    firePropertyChange(TOWER_CHANGED, null, null);
                    b.setSelected(false);
                }
            } else {
                if (b.isSelected()) {
                    b.setSelected(false);
                }

            }
        }
    }
}
