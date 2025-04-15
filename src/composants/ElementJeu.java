package composants;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class ElementJeu {
    protected double x;
    protected double y;
    protected double width;
    protected double height;

    protected String iconPath;

    public ElementJeu(double x, double y, String iconPath) {
        this.x = x;
        this.y = y;
        this.iconPath = iconPath;
    }

    public abstract void draw(Graphics g, Rectangle selectedRegion);

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Dimension getSize() {
        return new Dimension((int) width, (int) height);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Point getPoint() {
        return new Point((int) x, (int) y);
    }
}
