package compInterface;

import java.awt.Color;

import javax.swing.JFrame;

public class InfoBullePerso extends JFrame {
    protected Volet content;

    public InfoBullePerso() {

        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setUndecorated(true);
        content = new Volet();
        content.setOpaque(false);
        setContentPane(content);
        setBackground(Color.WHITE);
    }
}
