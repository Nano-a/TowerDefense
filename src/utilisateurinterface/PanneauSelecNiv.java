package utilisateurinterface;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import compInterface.BoutonAction;
import compInterface.EtiquetteTexte;
import compInterface.Volet;
import mecaniques.AnalyseurNiv;
import outils.Composant;

public class PanneauSelecNiv extends JPanel implements ActionListener {
    private CadreJeu tdf;
    private Jeu tdg;

    private EtiquetteTexte title;
    private List<BoutonChoixNiveau> buttons;
    private Volet buttonsPane;
    private BoutonAction retour;

    public PanneauSelecNiv(CadreJeu tdf, Jeu tdg) {
        this.tdf = tdf;
        this.tdg = tdg;

        title = new EtiquetteTexte("Choisissez un niveau", EtiquetteTexte.LARGE, EtiquetteTexte.MID);
        buttons = new ArrayList<>();

        Composant.add(title, this, 0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        retour = new BoutonAction("Retour");
        retour.addActionListener(this);
        Composant.add(retour, this, 0, 1, 1, 1, 1, 0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        buttonsPane = new Volet();

        List<String> names = AnalyseurNiv.parseLevels();
        for (int i = 0; i < names.size(); i++) {
            BoutonChoixNiveau button = new BoutonChoixNiveau(names.get(i));
            buttons.add(button);
            Composant.add(button, buttonsPane, i % 3, i / 3, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
            button.addActionListener(this);
        }

        Composant.add(buttonsPane, this, 0, 2, 1, 1, 1, 10, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        for (BoutonChoixNiveau b : buttons) {
            if (b == source) {
                tdg.startNewGame(Integer.parseInt(b.getText()));
                tdf.switchToGamePanel();
                return;
            }
        }
        if (source == retour) {
            tdf.switchToMainPanel();
        }
    }
}
