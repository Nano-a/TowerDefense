package composants;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import outils.Constantes;

public class Fortresse extends ElementJeu {
    private int health;
    private int maxHealth;

    public Fortresse(int health, ChampBataille t, String iconPath) {
        super(t.getPixelX(), t.getPixelY(), iconPath);

        this.health = health;
        this.maxHealth = health;

        this.width = t.getWidth();
        this.height = t.getHeight();

    }

    public void draw(Graphics g, Rectangle selectedRegion) {

        int screenX = (int) Math.round(x - selectedRegion.getX());
        int screenY = (int) Math.round(y - selectedRegion.getY());

        g.setColor(Color.BLUE);
        g.fillRect(screenX, screenY, (int) width, (int) height);
        g.setColor(Color.GREEN);

        double maxWidth = (width * Constantes.HEALTH_BAR_WIDTH_REDUCTION_FACTOR);

        int healthX = (int) (screenX + 0.5 * (width - maxWidth));
        int healthY = (screenY - Constantes.HEALTH_BAR_SPACING_BASE);

        int hWidth = (int) Math.round((maxWidth * ((double) health / (double) maxHealth)));

        g.fillRect(healthX, healthY, hWidth, Constantes.HEALTH_BAR_HEIGHT_BASE);

        g.setColor(Color.BLACK);
        g.drawRect(healthX, healthY, (int) maxWidth, Constantes.HEALTH_BAR_HEIGHT_BASE);

    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    public boolean takeDmg(int dmg) {
        health -= dmg;
        return health <= 0;
    }

    public String getHealth() {
        return health + "/" + maxHealth;
    }

    public boolean isExploded() {
        return health <= 0;
    }
}