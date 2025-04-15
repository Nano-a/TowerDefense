package mecaniques;

import java.util.List;

import composants.Adversaire;
import composants.Munition;
import mouvement.Vecteur;

public class Trajectoire {

    public Trajectoire() {

    }

    public static boolean updateProjectile(Munition p, int refreshDelay) {
        Adversaire e = p.getTarget();
        double x2 = e.getX();
        double y2 = e.getY();

        double x1 = p.getX();
        double y1 = p.getY();

        Vecteur v = new Vecteur(x2 - x1, y2 - y1);

        double magnitude = p.getSpeed();
        if (v.getLength() <= magnitude) {
            p.setX(x2);
            p.setY(y2);
            return true;
        }
        double dx = magnitude * v.getXRatio();
        double dy = magnitude * v.getYRatio();

        p.setX(x1 + dx);
        p.setY(y1 + dy);
        return false;
    }
    public void setNewTarget(List<Adversaire> enemies, Munition p) {

    }

}
