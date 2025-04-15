package outils;

import javax.swing.*;

import compInterface.EtiquetteTexte;
import compInterface.Volet;
import compInterface.PanneauDefilement;
import compInterface.ZoneText;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Journalisateur extends JFrame {

    private static Journalisateur logger;

    private List<LogMessage> logs;

    public class LoggerThread extends Thread {
        private String message;
        private NivJournal level;

        public LoggerThread(String message, NivJournal level) {
            this.message = message;
            this.level = level;
        }

        public void run() {
            LogMessage logMsg = new LogMessage(message, level);
            logs.add(logMsg);
            if (Journalisateur.this.isVisible()) {
                SwingUtilities.invokeLater(() -> {
                    logWindow.append(logMsg.getText() + "\n");
                    scroll.scrollDown();
                    Journalisateur.this.validate();
                });
            }
        }
    }

    public void log(String message, NivJournal level) {
        (new LoggerThread(message, level)).start();
    }

    private ZoneText logWindow;
    private PanneauDefilement scroll;
    private Volet panel;

    private EtiquetteTexte delay, loadDelay;

    private Journalisateur() {
        logs = new ArrayList<>();

        logWindow = new ZoneText();
        scroll = new PanneauDefilement();
        scroll.setViewportView(logWindow);

        panel = new Volet();

        delay = new EtiquetteTexte("Delay Time ", EtiquetteTexte.SMALL);
        loadDelay = new EtiquetteTexte("Load Time ", EtiquetteTexte.SMALL);

        Composant.add(scroll, panel, 0, 0, 2, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        Composant.add(delay, panel, 0, 1, 1, 1, 1, 0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        Composant.add(loadDelay, panel, 1, 1, 1, 1, 1, 0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        this.setContentPane(panel);

        this.setSize(300, 600);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setVisible(true);
    }

    public void showLogWindow() {
        this.setVisible(true);
    }

    public void updateLoadDelay(long time) {
        loadDelay.setText("Load Time " + time + "ms");
        loadDelay.repaint();
    }

    public void updateDelay(long time) {
        delay.setText("Frame Time " + time + "ms");
        delay.repaint();
    }

    private class LogMessage {
        private String message;
        private NivJournal level;

        public LogMessage(String message, NivJournal level) {
            this.message = message;
            this.level = level;
        }

        public String getText() {
            return "[" + level.getName() + "]" + "- " + message;

        }
    }

    public static Journalisateur getInstance() {
        if (logger == null) {
            logger = new Journalisateur();
        }
        return logger;
    }
}
