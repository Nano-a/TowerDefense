package compInterface;

import javax.swing.*;

import outils.Constantes;

import java.awt.*;

public class EtiquetteTexte extends JLabel {

    public static final int SMALL = 1;
    public static final int MEDIUM = 2;
    public static final int LARGE = 3;

    public static final int LEFT = 11;
    public static final int MID = 12;

    public static final int INVISIBLE = 21;

    public static final int BOLD = 31;

    public static final Font SMALL_BOLD_FONT = new Font(Constantes.FONT_FAMILY, Font.BOLD, Constantes.DEFAULT_SIZE);

    public EtiquetteTexte(String name, Font font) {
        super(name);
        setup();
        this.setFont(font);

    }

    public EtiquetteTexte(String name, int size, int align, int style) {
        super(name);
        setup();
        setSize(size);

        setAlignment(align);
        if (style == INVISIBLE) {
            this.setOpaque(false);
        }
    }

    public EtiquetteTexte(String name, int size, int align) {
        super(name);
        setup();
        setSize(size);
        setAlignment(align);
    }

    public EtiquetteTexte(String name, int size) {
        super(name);
        setup();
        setSize(size);
    }

    private void setup() {
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setOpaque(true);
    }

    public void setSize(int size) {
        switch (size) {
            case LARGE:
                this.setFont(Constantes.LARGE_LABEL_FONT);
                break;
            case MEDIUM:
                this.setFont(Constantes.MEDIUM_LABEL_FONT);
                break;
            case SMALL:
                this.setFont(Constantes.SMALL_LABEL_FONT);
                break;
            default:
                break;
        }
    }

    public void setAlignment(int alignment) {
        switch (alignment) {
            case LEFT:
                this.setHorizontalAlignment(SwingConstants.LEFT);
                break;
            case MID:
                this.setHorizontalAlignment(SwingConstants.CENTER);
                break;
            default:
                break;
        }
    }

}
