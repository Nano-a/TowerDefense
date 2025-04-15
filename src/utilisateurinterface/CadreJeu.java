package utilisateurinterface;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import compInterface.CadreApp;
import outils.IntercSouris;
import outils.Journalisateur;
import outils.NivJournal;

public class CadreJeu extends CadreApp implements WindowFocusListener {

    private PanneauPrincipale mainPane;
    private Jeu tdGame;
    private PanneauSelecNiv levelSelectPane;
    private PanneauParam settingsPane;

    
    private PanneauMenu menuPane;

    private JComponent last;

    private IntercSouris mouseHooker;

    public CadreJeu() {
        super();
        mouseHooker = new IntercSouris(this);
        this.addWindowFocusListener(this);

        mainPane = new PanneauPrincipale(this);
        tdGame = new Jeu(this);
        levelSelectPane = new PanneauSelecNiv(this, tdGame);
        menuPane = new PanneauMenu(this, tdGame);
        settingsPane = new PanneauParam(this);

        this.setContentPane(mainPane);
        this.setGlassPane(menuPane);

        this.setVisible(true);
    }

    public void switchToMainPanel() {
        switchPanel(mainPane);
    }

    public void switchToLevelSelectPanel() {
        switchPanel(levelSelectPane);
    }

    public void switchToGamePanel() {
        switchPanel(tdGame);
        tdGame.grabFocus();
    }

    public void switchToSettingsPanel() {
        switchPanel(settingsPane);
    }

    private void switchPanel(JComponent panel) {
        if (panel == tdGame) {
            mouseHooker.setActive(true);
        } else {
            mouseHooker.setActive(false);
        }
        last = (JComponent) this.getContentPane();
        this.setContentPane(panel);
        SwingUtilities.invokeLater(() -> this.validate());
    }

    public void toggleMenu(boolean b, String msg) {
        if (b) {
            menuPane.showMenu((JComponent) this.getContentPane(), msg);
            mouseHooker.setActive(false);
        } else {
            menuPane.setVisible(false);

            if (this.getContentPane() == tdGame) {
                mouseHooker.setActive(true);
            }
        }
        SwingUtilities.invokeLater(() -> this.validate());
    }

    public void toggleMenu(boolean b) {
        toggleMenu(b, null);
    }

    public void returnToLast() {
        switchPanel(last);
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        if (CadreJeu.this.getContentPane() == tdGame) {
            mouseHooker.setActive(true);
        }
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        mouseHooker.setActive(false);
    }

    public static void main(String args[]) {
        new CadreJeu();
        Journalisateur.getInstance().log("Program Startup Complete", NivJournal.STATUS);
    }

}
