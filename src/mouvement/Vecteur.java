package mouvement;

public class Vecteur {

    private double i;
    private double j;

    public Vecteur(double i, double j) {
        this.i = i;
        this.j = j;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public double getI() {
        return this.i;
    }

    public double getJ() {
        return this.j;
    }

    public double getLength() {
        return Math.sqrt(i * i + j * j);
    }

    public double getYRatio() {
        return j / getLength();
    }

    public double getXRatio() {
        return i / getLength();
    }
}
