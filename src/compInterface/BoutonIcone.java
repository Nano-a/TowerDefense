package compInterface;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import outils.ChargImage;
import outils.Constantes;

public class BoutonIcone extends JButton {

    public BoutonIcone(String s) {
        super();

        this.setContentAreaFilled(false);
        this.setPreferredSize(Constantes.ICON_SIZE);
        this.setBorder(new EmptyBorder(0, 0, 0, 0));

        this.setIconTextGap(0);

        BufferedImage i = ChargImage.readImage(s, Constantes.ICON_DIR, Constantes.ICON_FILE_TYPE);
        this.setIcon(new ImageIcon(ChargImage.scaleImage(i,
                (int) Constantes.ICON_SIZE.getWidth(),
                (int) Constantes.ICON_SIZE.getHeight())));

    }
}
