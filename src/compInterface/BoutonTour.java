package compInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import categories.GenreTour;
import outils.Constantes;

public class BoutonTour extends JButton {

    private GenreTour type;

    private Border lineBorder, emptyBorder;

    public BoutonTour(GenreTour type) {
        lineBorder = new LineBorder(Color.BLUE, 1);
        emptyBorder = new EmptyBorder(1, 1, 1, 1);

        Icon i = getIcon(type.getIcon());
        this.type = type;
        if (i != null) {
            this.setIcon(i);
        } else {
            System.out.println("No Icon designated for this tower");
        }

        this.setText(String.valueOf(type.getCost()));

        setLook();
    }

    private void setLook() {
        this.setPreferredSize(Constantes.HUD_TOWER_ICON_SIZE);
        this.setContentAreaFilled(false);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.setFocusPainted(false);
        this.setIconTextGap(2);
        this.setMargin(new Insets(0, 0, 0, 0));
        this.setFont(Constantes.HUD_TOWER_GOLD_FONT);

    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
        if (b) {
            this.setBorder(lineBorder);
        } else {
            this.setBorder(emptyBorder);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        super.paintComponent(g);

    }

    public GenreTour getType() {
        return type;
    }

    private ImageIcon getIcon(String s) {
        try {
            BufferedImage i = ImageIO
                    .read(getClass().getResourceAsStream(Constantes.TOWER_ICON_DIR + s + Constantes.TOWER_ICON_FILETYPE));
            int a = (int) Constantes.HUD_TOWER_ICON_SIZE.getWidth();
            Image scaled = i.getScaledInstance(a, a, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Friends - If you are getting this error, it is likely that you forgot to add" +
                    " the res folder as resources. (Right click res folder -> mark directory as -> resources");
        }

        return null;
    }
}
