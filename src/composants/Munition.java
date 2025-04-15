package composants;

import java.awt.*;

import categories.TypeProjectile;

public class Munition extends ElementMobile {
    private Adversaire target;
    private Tour tower;

    private int dmg;
    private double speed;

    public Munition(Adversaire target, int dmg, double speed, double x, double y, String iconPath) {
        super(speed, x, y, iconPath);
        this.target = target;
        this.dmg = dmg;

        width = 10;
        height = 10;

    }

    public Munition(Adversaire e, Tour t, TypeProjectile projectileType) {
        super(projectileType.getSpeed(), t.getX(), t.getY(), null);
        this.tower = t;
        this.target = e;
        this.dmg = (int) Math.round(t.getAtkDmg());
        width = 10;
        height = 10;
    }

    public void draw(Graphics g, Rectangle selectedRegion) {
        g.setColor(Color.ORANGE);

        int screenX = (int) Math.round(x - selectedRegion.getX());
        int screenY = (int) Math.round(y - selectedRegion.getY());
        g.fillOval(screenX, screenY, (int) width, (int) height);
    }

    public Tour getTower() {
        return tower;
    }

    public Adversaire getTarget() {
        return target;
    }

    public int getDmg() {
        return dmg;
    }

    public void setTarget(Adversaire t) {
        this.target = t;
    }

    public void setDmg(int d) {
        this.dmg = d;
    }

}
