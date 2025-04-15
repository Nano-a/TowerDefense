package composants;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import categories.GenreTour;
import categories.TypeProjectile;

public class Tour extends ElementJeu {
    private GenreTour type;

    private double timeTilNextFire;
    private double fireRate;

    private boolean selected;

    public Tour(double x, double y, GenreTour type) {
        super(x, y, null);
        modifyType(type);
        selected = false;
        timeTilNextFire = type.getFireRate();
    }

    public void modifyType(GenreTour upgradeType) {
        this.type = upgradeType;
        width = type.getWidth();
        height = type.getHeight();
        fireRate = type.getFireRate();

    }

    public void draw(Graphics g, Rectangle selectedRegion) {
        int screenX = (int) Math.round(x - selectedRegion.getX());
        int screenY = (int) Math.round(y - selectedRegion.getY());

        g.drawImage(type.getSprite(), screenX, screenY, null);

        int range = (int) type.getRange();

        if (selected) {
            g.setColor(Color.BLACK);
            g.drawRect(screenX, screenY, (int) width, (int) height);
            g.drawRect(screenX + 1, screenY + 1, (int) (width - 1), (int) (height - 1));
            g.setColor(Color.CYAN);
            g.drawOval((int) (screenX - range + width / 2), (int) (screenY - range + width / 2), 2 * range, 2 * range);

        }
    }

    public double getAtkDmg() {
        return type.getDmg();
    }

    public double getRange() {
        return type.getRange();
    }

    public boolean canFire() {
        return timeTilNextFire <= 0;

    }

    public void decrementCooldown(double v) {
        if (timeTilNextFire > 0) {
            this.timeTilNextFire -= v;
        }
    }

    public void setFired() {
        timeTilNextFire = 1000.0 / fireRate;
    }

    public void setSelected(boolean b) {
        selected = b;

    }

    public boolean isSelected() {
        return selected;
    }

    public List<GenreTour> getUpgrades() {
        return type.getUpgrades();
    }

    public String getTypeName() {
        return type.getName();
    }

    public GenreTour getType() {
        return type;
    }

    public double getSellPrice() {
        return type.getSellPrice();
    }

    public TypeProjectile getProjectileType() {
        return TypeProjectile.BASIC;
    }
}
