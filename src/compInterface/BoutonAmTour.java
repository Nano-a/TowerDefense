package compInterface;

import javax.swing.*;

import categories.GenreTour;
import composants.Tour;
import outils.Constantes;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class BoutonAmTour extends BoutonAction implements MouseListener, MouseMotionListener {

    private GenreTour upgradeType;
    private GenreTour prevType;

    public BoutonAmTour(GenreTour tt, Tour selectedTower) {
        super();

        this.setIcon(new ImageIcon(tt.getSprite()));
        this.setText(tt.getCost() + " g");

        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.setHorizontalTextPosition(SwingConstants.CENTER);

        this.setIconTextGap(3);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setFont(Constantes.SMALL_LABEL_FONT);

        upgradeType = tt;

        tool = new InfoTour(tt, selectedTower);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addActionListener((e) -> {
            showHideToolTip(false, null);
        });

    }

    private JFrame tool;

    public GenreTour getUpgradeType() {
        return upgradeType;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        showHideToolTip(true, e.getLocationOnScreen());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        showHideToolTip(false, null);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        moveWindow(e.getLocationOnScreen());
    }

    private void showHideToolTip(boolean b, Point p) {
        if (b)
            moveWindow(p);

        tool.setVisible(b);
        tool.repaint();

    }

    private void moveWindow(Point point) {
        SwingUtilities.invokeLater(() -> {
            tool.setLocation(new Point((int) point.getX() + 10, (int) point.getY() + 1));
            tool.repaint();
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

}
