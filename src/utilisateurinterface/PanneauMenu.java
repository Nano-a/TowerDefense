package utilisateurinterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.JComponent;
import javax.swing.JPanel;

import compInterface.BoutonAction;
import compInterface.EtiquetteTexte;
import outils.Composant;
import outils.Constantes;

/*
 * Represents a Menu Overlay.
 */
public class PanneauMenu extends JPanel implements ActionListener {

    private CadreJeu tdf;
    private Jeu tdg;

    private EtiquetteTexte displayMessage;
    private BoutonAction restartGame;
    private BoutonAction resumeGame;
    private BoutonAction returnToMain;
    private BoutonAction settings;
    private BoutonAction quit;

    private boolean clickToReturnEnabled = false;

    private BufferedImage background;

    public PanneauMenu(CadreJeu tdf, Jeu tdg) {
        this.tdf = tdf;
        this.tdg = tdg;

        this.addMouseListener(new MyMouseListener());

        displayMessage = new EtiquetteTexte("", EtiquetteTexte.LARGE);
        resumeGame = new BoutonAction("Continuer");
        restartGame = new BoutonAction("Rejouer la Partie");
        returnToMain = new BoutonAction("Revenir à la page d'acceuil");
        settings = new BoutonAction("Réglage");
        quit = new BoutonAction("Quitter");


        Composant.add(displayMessage, this, 0, 0, 1, 1, 1, 0,
                GridBagConstraints.BOTH, GridBagConstraints.CENTER, 0, 0, 10, 0);

        Composant.add(resumeGame, this, 0, 1, 1, 1, 1, 0,
                GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        Composant.add(restartGame, this, 0, 2, 1, 1, 1, 0,
                GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        Composant.add(returnToMain, this, 0, 3, 1, 1, 1, 0,
                GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        Composant.add(settings, this, 0, 4, 1, 1, 1, 0,
                GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        Composant.add(quit, this, 0, 5, 1, 1, 1, 0,
                GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        resumeGame.addActionListener(this);
        restartGame.addActionListener(this);
        returnToMain.addActionListener(this);
        settings.addActionListener(this);
        quit.addActionListener(this);
    }

    private void resume() {
        if (resumeGame.isVisible()) {
            tdg.resumeGame();
        }
        tdf.toggleMenu(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == resumeGame) {
            resume();
        } else if (source == restartGame) {
            tdg.restartGame();
        } else if (source == returnToMain) {
            tdf.switchToMainPanel();
        } else if (source == settings) {
            tdf.switchToSettingsPanel();
        }else if (source == quit) {
            System.exit(0);
        }
        tdf.toggleMenu(false);
    }

    public void showMenu(JComponent source, String msg) {
        if (source instanceof Jeu) {
            if (msg == null) {
                displayMessage.setVisible(false);
                resumeGame.setVisible(true);
                clickToReturnEnabled = true;
            } else if (msg.equals(Constantes.OBSERVER_LEVEL_COMPLETE)) {
                displayMessage.setText("Level Complete! :)");
                displayMessage.setVisible(true);
                resumeGame.setVisible(false);
                clickToReturnEnabled = false;
            } else if (msg.equals(Constantes.OBSERVER_LEVEL_FAILED)) {
                displayMessage.setText("Game Over! :(");
                displayMessage.setVisible(true);
                resumeGame.setVisible(false);
                clickToReturnEnabled = false;
            }
            restartGame.setVisible(true);
            returnToMain.setVisible(true);
            settings.setVisible(true);
        } else if (source instanceof PanneauPrincipale) {
            clickToReturnEnabled = false;
            displayMessage.setVisible(false);
            resumeGame.setVisible(false);
            restartGame.setVisible(false);
            returnToMain.setVisible(true);
            settings.setVisible(true);
        }
        setVisible(true);
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            createBlurredImage();
        }
        super.setVisible(b);
    }

    private void createBlurredImage() {
        try {
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(new Rectangle(
                    tdf.getX() + this.getX(), tdf.getY() + this.getY(), this.getWidth(), this.getHeight()));

            Kernel kernel = new Kernel(3, 3, new float[] { 1f / 9f, 1f / 9f, 1f / 9f,
                    1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f });
            BufferedImageOp op = new ConvolveOp(kernel);
            background = op.filter(image, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, null);
        }
        g.setColor(new Color(238, 242, 245, 200));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    private class MyMouseListener implements MouseListener {

        @Override
        public void mouseReleased(MouseEvent e) {
            if (clickToReturnEnabled) {
                resume();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

}
