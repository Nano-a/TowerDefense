package utilisateurinterface.Action;

import static gestionnaire.Etat.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import categories.GenreEnemie;
import categories.GenreTour;
import categories.NatureTerrain;
import compInterface.Volet;
import composants.Adversaire;
import composants.ChampBataille;
import composants.ElementJeu;
import gestionnaire.Deroulement;
import mouvement.Geometrie;
import outils.Constantes;
import outils.Journalisateur;
import outils.NivJournal;
import surveillant.Gardien;
import surveillant.Surveille;

public class BarreJeu extends Volet implements Gardien {

    private Point mouseOnScreen;

    private Deroulement game;

    private Rectangle selectedRegion;

    private boolean top, bot, left, right;

    private MapMoveThread mapMoveThread;

    public BarreJeu() {
        game = new Deroulement();

        resetRectangle();

        this.setPreferredSize(Constantes.GAME_DIMENSION);
        this.setBackground(Color.BLACK);
        this.addMouseListener(new MyMouseListener());
        this.addMouseMotionListener(new MyMouseMotionListener());
        this.addKeyListener(new MyKeyListener());
        this.setFocusable(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        synchronized (selectedRegion) {
            ChampBataille[][] terrains = game.getTerrain();

            double blockSizeX = Constantes.DEFAULT_BLOCK_SIZE;
            double blockSizeY = Constantes.DEFAULT_BLOCK_SIZE;
            for (int y = 0; y < terrains.length; y++) {
                for (int x = 0; x < terrains[y].length; x++) {
                    if (!Geometrie.isObjectInsideRegion(terrains[y][x], selectedRegion)) {
                        continue;
                    }
                    g.setColor(terrains[y][x].getType().getColor());
                    g.fillRect((int) (x * blockSizeX + 1 - selectedRegion.getX()),
                            (int) (y * blockSizeY + 1 - selectedRegion.getY()),
                            (int) blockSizeX,
                            (int) blockSizeY);
                    if (terrains[y][x].getType() == NatureTerrain.BUILDABLE) {
                        g.setColor(Color.BLACK);
                        g.drawRect((int) (x * blockSizeX - selectedRegion.getX()),
                                (int) (y * blockSizeY - selectedRegion.getY()),
                                (int) blockSizeX,
                                (int) blockSizeY);
                    }
                }
            }
            List<ElementJeu> gcs = game.getMap().getGameComponents();

            synchronized (gcs) {
                for (ElementJeu gc : gcs) {
                    if (Geometrie.isObjectInsideRegion(gc, selectedRegion)) {
                        gc.draw(g, selectedRegion);
                    }
                }
            }

            // Draw Mouse hover.
            if (mouseOnScreen != null) {
                drawTowerHighlightOnMouse(g);
            }
        }
    }

    private void drawTowerHighlightOnMouse(Graphics g) {

        GenreTour selectedTower = game.getBuildTowerType();
        if (selectedTower != null) { 
            Image img;
            Point point = new Point(mouseOnScreen.x, mouseOnScreen.y);
            convertToGamePoint(point);

            if (game.isTowerLocationValid(point)) {
                img = selectedTower.getSpriteActive();
            } else {
                img = selectedTower.getSpriteDeactive();
            }

            int x = (int) Math.round(this.mouseOnScreen.getX() - selectedTower.getWidth() / 2);
            int y = (int) Math.round(this.mouseOnScreen.getY() - selectedTower.getHeight() / 2);
            g.drawImage(img, x, y, null);
        }
    }

    @Override
    public void update(Surveille o, String msg) {
        if (msg.equals(Constantes.OBSERVER_GAME_TICK)) {
            this.repaint();
        } else if (msg.equals(Constantes.OBSERVER_GAME_PAUSED) || msg.equals(Constantes.OBSERVER_LEVEL_FAILED)
                || msg.equals(Constantes.OBSERVER_LEVEL_COMPLETE)) {
            resetRectangle();
        } else if (msg.equals(Constantes.OBSERVER_NEW_GAME)) {
            resetRectangle();
        } else if (msg.equals(Constantes.OBSERVER_STATE_RUNNING)) {
            startMapThread();
        } else if (msg.equals(Constantes.OBSERVER_STATE_PAUSED)) {

        } else if (msg.equals(Constantes.OBSERVER_STATE_TERMINATED)) {

        }
    }

    private void resetRectangle() {
        selectedRegion = new Rectangle(0, 0,
                (int) Constantes.GAME_DIMENSION.getWidth(), (int) Constantes.GAME_DIMENSION.getHeight());
        top = false;
        bot = false;
        left = false;
        right = false;
    }

    public Deroulement getGameProcess() {
        return game;
    }

    private class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            if (Constantes.DEBUGGING_MODE) {
                if (e.getKeyChar() == 'e') {
                    game.getMap().addComponent(new Adversaire(GenreEnemie.BASIC, 0, 0));
                } else if (e.getKeyChar() == 'm') {
                    System.out.println("Gold!");
                    game.gainGold(1000); // Money maker!
                }
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    private class MyMouseListener implements MouseListener {
        private Point down;

        @Override
        public void mouseClicked(MouseEvent e) {
            BarreJeu.this.grabFocus();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (down == null) {
                down = e.getPoint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (down != null) {
                Point p = e.getPoint();
                if (Geometrie.withinDistance(p, down)) {
                    convertToGamePoint(p);
                    game.mouseClick(p);
                }
                down = null;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            BarreJeu.this.grabFocus();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            mouseOnScreen = null;
        }
    }

    private class MyMouseMotionListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {
            BarreJeu.this.getParent().dispatchEvent(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseOnScreen = e.getPoint();
            BarreJeu.this.getParent().dispatchEvent(e);
        }
    }

    private void convertToGamePoint(Point point) {
        point.setLocation((point.getX() + selectedRegion.getX()), (point.getY() + selectedRegion.getY()));
    }

    private void startMapThread() {
        System.out.println("Creating Map Move Thread");

        if (mapMoveThread == null || mapMoveThread.getState() == Thread.State.TERMINATED) {
            mapMoveThread = new MapMoveThread();
        }

        if (mapMoveThread.isAlive()) {
            Journalisateur.getInstance().log("Map Thread is somehow still alive... zz", NivJournal.WARNING);
            mapMoveThread.interrupt();
            try {
                mapMoveThread.join(1000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Journalisateur.getInstance().log("Map Thread now " + mapMoveThread.isAlive(), NivJournal.WARNING);
            }
        }
        mapMoveThread.start();
    }

    private class MapMoveThread extends Thread {
        boolean wideEnough, tallEnough;
        double width, height;

        public MapMoveThread() {
            width = game.getMapSize().getWidth();
            height = game.getMapSize().getHeight();

            wideEnough = width > Constantes.GAME_DIMENSION.getWidth();
            tallEnough = height > Constantes.GAME_DIMENSION.getHeight() - Constantes.HUD_DIMENSION.getHeight();
        }

        @Override
        public void run() {
            while (game.getState() != PLAYING) {
                try {
                    Thread.sleep(100);
                    System.out.println(game.getState().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            while (game.getState() == PLAYING) {
                if (!BarreJeu.this.hasFocus() || !BarreJeu.this.isVisible()) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                long start = System.nanoTime();
                Point mouse = MouseInfo.getPointerInfo().getLocation();
                Point p = BarreJeu.this.getLocationOnScreen();
                Rectangle r = new Rectangle((int) p.getX(), (int) p.getY(),
                        (int) Constantes.GAME_DIMENSION.getWidth(), (int) Constantes.GAME_DIMENSION.getHeight());

                top = Geometrie.isPointInTop(mouse, r, Constantes.MOVEMENT_PIXEL_PADDING);
                bot = Geometrie.isPointInBot(mouse, r, Constantes.MOVEMENT_PIXEL_PADDING);
                left = Geometrie.isPointInLeft(mouse, r, Constantes.MOVEMENT_PIXEL_PADDING);
                right = Geometrie.isPointInRight(mouse, r, Constantes.MOVEMENT_PIXEL_PADDING);

                handleMapMove();

                long delay = Math.round((System.nanoTime() - start) / 1000000.0);
                if (delay >= Constantes.MAP_MOVE_REFRESH_DELAY) {
                    continue;
                }
                try {
                    Thread.sleep(Constantes.MAP_MOVE_REFRESH_DELAY - delay);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
            System.out.println("Terminating Map Thread");
        }

        private void handleMapMove() {
            synchronized (selectedRegion) {
                int speed = Constantes.MAP_MOVE_SPEED / Constantes.REFRESH_RATE;
                if (tallEnough) {
                    if (top) {
                        if (selectedRegion.getY() - speed < 0) {
                            selectedRegion.setLocation((int) selectedRegion.getX(), 0);
                        } else {
                            selectedRegion.setLocation(
                                    (int) selectedRegion.getX(), (int) selectedRegion.getY() - speed);
                        }

                    } else if (bot) {
                        if (selectedRegion.getY() + speed + selectedRegion.getHeight() > height
                                + Constantes.HUD_DIMENSION.getHeight()) {
                            selectedRegion.setLocation((int) selectedRegion.getX(), (int) height
                                    - (int) selectedRegion.getHeight() + (int) Constantes.HUD_DIMENSION.getHeight());
                        } else {
                            selectedRegion.setLocation(
                                    (int) selectedRegion.getX(), (int) selectedRegion.getY() + speed);
                        }
                    }
                }
                if (wideEnough) {
                    if (left) {
                        if (selectedRegion.getX() - speed < 0) {
                            selectedRegion.setLocation(0, (int) selectedRegion.getY());
                        } else {
                            selectedRegion.setLocation(
                                    (int) selectedRegion.getX() - speed, (int) selectedRegion.getY());
                        }
                    } else if (right) {
                        if (selectedRegion.getX() + speed + selectedRegion.getWidth() > width) {
                            selectedRegion.setLocation((int) width - (int) selectedRegion.getWidth(),
                                    (int) selectedRegion.getY());
                        } else {
                            selectedRegion.setLocation(
                                    (int) selectedRegion.getX() + speed, (int) selectedRegion.getY());

                        }
                    }
                }
            }
        }
    }
}
