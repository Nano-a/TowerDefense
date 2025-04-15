package compInterface;

import java.awt.GridBagConstraints;

import categories.GenreTour;
import composants.Tour;
import outils.Composant;

public class InfoTour extends InfoBullePerso {

        private EtiquetteTexte[][] dataArray;
        private EtiquetteTexte title;

        public InfoTour(GenreTour t, Tour selectedTower) {
                super();

                dataArray = new EtiquetteTexte[5][2];

                GenreTour prev = selectedTower.getType();
                title = new EtiquetteTexte(t.getName(), EtiquetteTexte.SMALL_BOLD_FONT);

                dataArray[0][0] = new EtiquetteTexte("Type ", EtiquetteTexte.SMALL_BOLD_FONT);
                dataArray[0][1] = new EtiquetteTexte(t.getType(), EtiquetteTexte.SMALL);

                dataArray[1][0] = new EtiquetteTexte("Range ", EtiquetteTexte.SMALL_BOLD_FONT);
                dataArray[1][1] = new EtiquetteTexte(String.valueOf(prev.getRange()) + " -> " +
                                String.valueOf(t.getRange()), EtiquetteTexte.SMALL);

                dataArray[2][0] = new EtiquetteTexte("Cost ", EtiquetteTexte.SMALL_BOLD_FONT);
                dataArray[2][1] = new EtiquetteTexte(String.valueOf(prev.getCost()) + " -> " +
                                String.valueOf(t.getCost()), EtiquetteTexte.SMALL);

                dataArray[3][0] = new EtiquetteTexte("Rate ", EtiquetteTexte.SMALL_BOLD_FONT);
                dataArray[3][1] = new EtiquetteTexte(String.valueOf(prev.getFireRate()) + " -> " +
                                String.valueOf(t.getFireRate()), EtiquetteTexte.SMALL);

                dataArray[4][0] = new EtiquetteTexte("Dmg ", EtiquetteTexte.SMALL_BOLD_FONT);
                dataArray[4][1] = new EtiquetteTexte(String.valueOf(prev.getDmg()) + " -> " +
                                String.valueOf(t.getDmg()), EtiquetteTexte.SMALL);

                Volet dataHolder = new Volet();
                for (int i = 0; i < dataArray.length; i++) {
                        for (int j = 0; j < dataArray[i].length; j++) {
                                Composant.add(dataArray[i][j], dataHolder, j, i, 1, 1, 1, 1, GridBagConstraints.BOTH,
                                                GridBagConstraints.CENTER);
                        }
                }

                Composant.add(title, content, 0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
                Composant.add(dataHolder, content, 0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

                this.pack();
                // this.setSize(new Dimension(100, 100));
        }
}
