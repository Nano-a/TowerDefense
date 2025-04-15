package utilisateurinterface;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import compInterface.BoutonAction;
import compInterface.Volet;
import outils.Composant;
import outils.Journalisateur;
import reglage.GestionParam;
import reglage.Param;

public class PanneauParam extends Volet implements ActionListener {
    private GestionParam sio;
    private Param settings;

    private CadreJeu tdf;

    private BoutonAction retour, showLogger;

    public PanneauParam(CadreJeu tdf) {
        this.tdf = tdf;

        sio = new GestionParam();
        settings = sio.getSettings();

        retour = new BoutonAction("Retour");
        showLogger = new BoutonAction("Visionner le terminal");

        Composant.add(retour, this, 0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        Composant.add(showLogger, this, 0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        retour.addActionListener(this);
        showLogger.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == retour) {
            tdf.returnToLast();
        } else if (source == showLogger) {
            Journalisateur.getInstance().showLogWindow();
        }
    }
}
