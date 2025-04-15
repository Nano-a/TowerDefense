package composants;

public abstract class ElementMobile extends ElementJeu {
    protected double speed;

    public ElementMobile(double speed, double x, double y, String iconPath) {
        super(x, y, iconPath);
        this.speed = speed;
    }
    
    public double getSpeed() {
        return speed;
    }

    public void addX(double n) {
        x = x + n;
    }

    public void addY(double n) {
        y = y + n;
    }
}
