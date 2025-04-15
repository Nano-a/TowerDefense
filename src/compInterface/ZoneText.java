package compInterface;

import javax.swing.*;

import outils.Constantes;

public class ZoneText extends JTextArea {

    public ZoneText() {
        super();
        this.setEditable(false);

        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setFont(Constantes.LOGGER_FONT);
    }
}
