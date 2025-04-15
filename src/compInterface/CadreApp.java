package compInterface;

import javax.swing.JFrame;

import outils.Composant;
import outils.Constantes;

public class CadreApp extends JFrame {
    public CadreApp() {
        this.setUndecorated(true);
        this.setSize(Constantes.FRAME_DEFAULT_SIZE);
        Composant.center(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
