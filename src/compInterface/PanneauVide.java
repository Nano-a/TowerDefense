package compInterface;

import java.awt.*;

public class PanneauVide extends Volet {
    public PanneauVide(Dimension d) {
        this.setPreferredSize(d);
        this.setMinimumSize(d);
        this.setMaximumSize(d);

    }
}
