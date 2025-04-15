package mecaniques;

import java.util.List;

import composants.Adversaire;
import composants.ChampBataille;
import mouvement.Vecteur;

public class DeplacementAdv {

    public static boolean updateEnemy(Adversaire e, long refreshTime) {

        if (!e.getPath().isEmpty()) {
            List<ChampBataille> path = e.getPath();

            ChampBataille curr = path.get(0);
            int curX = curr.getX();
            int curY = curr.getY();
            if (e.getX() >= curX * curr.getWidth() &&
                    e.getX() <= (curX + 1) * curr.getWidth() &&
                    e.getY() >= curY * curr.getHeight() &&
                    e.getY() <= (curY + 1) * curr.getHeight()) {
                path.remove(curr);
            }

            if (path.isEmpty()) {
                return true;

            } else {
                ChampBataille target = path.get(0);
                Vecteur v = new Vecteur((target.getCenX() - (e.getX() + e.getWidth() / 2)),
                        (target.getCenY() - (e.getY() + e.getHeight() / 2)));

                double speed = e.getSpeed() * (double) refreshTime / 1000.0;
                e.addX(speed * v.getXRatio());
                e.addY(speed * v.getYRatio());
            }
        }
        return false;
    }

}
