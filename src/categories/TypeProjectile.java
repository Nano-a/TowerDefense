package categories;

public enum TypeProjectile {
    MAGIC(5),
    BASIC(5);

    TypeProjectile(double speed) {
        this.speed = speed;
    }

    private double speed;

    public double getSpeed() {
        return speed;
    }
}
