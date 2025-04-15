package compInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import outils.Constantes;

public class BoutonAction extends JButton {

    public BoutonAction() {
        super();
        setLook();
    }

    public BoutonAction(String s) {
        super(s);
        setLook();
    }

    public BoutonAction(Image sprite) {
        super(new ImageIcon(sprite));
        setLook();
    }

    private void setLook() {
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setFont(Constantes.DEFAULT_FONT);
        this.addMouseListener(new MyMouseListener());
        this.setFocusPainted(false);
        this.setBorder(new LineBorder(Color.BLACK, 1));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (this.isSelected()) {
            g.setColor(Constantes.BUTTON_SELECTED_COLOR);
        } else {
            g.setColor(Constantes.BUTTON_UNSELECTED_COLOR);
        }

        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        super.paintComponent(g);

    }

    private class MyMouseListener implements MouseListener {

        private void refresh() {
            SwingUtilities.invokeLater(() -> BoutonAction.this.validate());
        }

        private void paintBorder(boolean b) {
            if (BoutonAction.this.isBorderPainted() != b) {
                BoutonAction.this.setBorderPainted(b);
                refresh();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            paintBorder(false);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            BoutonAction.this.setSelected(true);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            BoutonAction.this.setSelected(false);
            paintBorder(false);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            paintBorder(true);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            paintBorder(false);
        }
    }
}
