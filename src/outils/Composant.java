package outils;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

public class Composant {
    public static void add(JComponent toAdd, JComponent addTo, int x, int y, int width, int height,
            double weightx, double weighty, int fill, int anchor,
            Insets i) {
        if (!(addTo.getLayout() instanceof GridBagLayout)) {
            addTo.setLayout(new GridBagLayout());
        }

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = width;
        c.gridheight = height;
        c.fill = fill;
        c.anchor = anchor;
        c.gridx = x;
        c.gridy = y;
        c.weightx = weightx;
        c.weighty = weighty;
        c.insets = i;

        addTo.add(toAdd, c);
    }

    public static void add(JComponent toAdd, JComponent addTo, int x, int y, int width, int height,
            double weightx, double weighty, int fill, int anchor,
            int i, int j, int k, int l) {
        add(toAdd, addTo, x, y, width, height, weightx, weighty, fill, anchor, new Insets(i, j, k, l));
    }

    public static void add(JComponent toAdd, JComponent addTo, int x, int y, int width, int height,
            double weightx, double weighty, int fill, int anchor) {
        add(toAdd, addTo, x, y, width, height, weightx, weighty, fill, anchor, 0, 0, 0, 0);
    }

    public static void add(JComponent toAdd, JComponent addTo) {
        add(toAdd, addTo, 0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
    }

    public static void add(JComponent toAdd, JLayeredPane addTo, int i, int x, int y, int width, int height,
            double weightx, double weighty, int fill, int anchor) {
        add(toAdd, addTo, i, x, y, width, height, weightx, weighty, fill, anchor, 0, 0, 0, 0);
    }

    public static void add(JComponent toAdd, JLayeredPane addTo, int i, int x, int y, int width, int height,
            double weightx, double weighty, int fill, int anchor, int j, int k, int l, int m) {
        if (!(addTo.getLayout() instanceof GridBagLayout)) {
            addTo.setLayout(new GridBagLayout());
        }

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = width;
        c.gridheight = height;
        c.fill = fill;
        c.anchor = anchor;
        c.gridx = x;
        c.gridy = y;
        c.weightx = weightx;
        c.weighty = weighty;
        Insets in = new Insets(j, k, l, m);
        c.insets = in;
        addTo.setLayer(toAdd, i);
        addTo.add(toAdd, c, i);
    }

    public static void center(JFrame frame) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2,
                dim.height / 2 - frame.getSize().height / 2);
    }

}
