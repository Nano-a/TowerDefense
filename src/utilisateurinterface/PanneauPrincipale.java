package utilisateurinterface;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import compInterface.BoutonAction;
import compInterface.EtiquetteTexte;
import compInterface.Spacer;
import outils.Composant;
import outils.Constantes;

public class PanneauPrincipale extends JPanel
                implements ActionListener {

        private EtiquetteTexte title;
        private EtiquetteTexte version;
        private BoutonAction start;
        private BoutonAction menu;
        private BoutonAction quit;
        private BoutonAction settings;

        private CadreJeu tdf;

        public PanneauPrincipale(CadreJeu tdf) {
                this.tdf = tdf;

                title = new EtiquetteTexte(Constantes.GAME_NAME, EtiquetteTexte.LARGE);
                version = new EtiquetteTexte(Constantes.VERSION_NUMBER, EtiquetteTexte.MEDIUM);

                start = new BoutonAction("Nouvelle Partie");
                start.addActionListener(this);

                menu = new BoutonAction("Menu");
                menu.addActionListener(this);

                settings = new BoutonAction("Réglage");
                settings.addActionListener(this);

                quit = new BoutonAction("Quitter :( ");
                quit.addActionListener(this);

                Composant.add(new Spacer(), this, 0, 0, 1, 1, 1, 0.5,
                                GridBagConstraints.BOTH, GridBagConstraints.PAGE_START);

                Composant.add(title, this, 0, 1, 1, 1, 1, 0,
                                GridBagConstraints.BOTH, GridBagConstraints.PAGE_START);
                Composant.add(version, this, 0, 2, 1, 1, 1, 0,
                                GridBagConstraints.BOTH, GridBagConstraints.PAGE_START);

                Composant.add(new Spacer(), this, 0, 3, 1, 1, 1, 1,
                                GridBagConstraints.BOTH, GridBagConstraints.PAGE_START);

                Composant.add(start, this, 0, 4, 1, 1, 1, 0,
                                GridBagConstraints.BOTH, GridBagConstraints.CENTER);
                Composant.add(menu, this, 0, 5, 1, 1, 1, 0,
                                GridBagConstraints.BOTH, GridBagConstraints.CENTER);
                Composant.add(settings, this, 0, 6, 1, 1, 1, 0,
                                GridBagConstraints.BOTH, GridBagConstraints.CENTER);

                Composant.add(quit, this, 0, 7, 1, 1, 1, 0,
                                GridBagConstraints.BOTH, GridBagConstraints.CENTER);

                Composant.add(new Spacer(), this, 0, 8, 1, 1, 1, 2,
                                GridBagConstraints.BOTH, GridBagConstraints.PAGE_END);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == start) {
                        tdf.switchToLevelSelectPanel();
                } else if (source == menu) {
                        tdf.toggleMenu(true);
                } else if (source == settings) {
                        tdf.switchToSettingsPanel();
                } else if (source == quit) {
                        System.exit(0);
                }
        }
}
