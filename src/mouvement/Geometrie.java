package mouvement;

import java.awt.*;

import composants.ElementJeu;
import composants.ChampBataille;
import composants.Tour;
import outils.Constantes;

public class Geometrie {

    public static boolean withinDistance(Point a, Point b) {
        return getDistance(a, b) < Constantes.CLICK_VAR_DISTANCE;
    }

    public static double getDistance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    public static boolean isPointInObject(Point point, ElementJeu gc) {
        return point.getX() >= gc.getX() && point.getX() <= gc.getX() + gc.getWidth() &&
                point.getY() >= gc.getY() && point.getY() <= gc.getY() + gc.getHeight();
    }

    public static boolean withinRange(ElementJeu g1, ElementJeu g2, double range) {
        return range >= Math.sqrt(Math.pow(g1.getX() - g2.getX(), 2) + Math.pow(g1.getY() - g2.getY(), 2));
    }

    public static boolean isObjectInsideRegion(ChampBataille terrain, Rectangle selectedRegion) {
        return (terrain.getPixelX() + terrain.getWidth() > selectedRegion.getX() &&
                terrain.getPixelX() < selectedRegion.getX() + selectedRegion.getWidth() &&

                terrain.getPixelY() + terrain.getHeight() > selectedRegion.getY() &&
                terrain.getPixelY() < selectedRegion.getY() + selectedRegion.getHeight());
    }

    public static boolean isObjectInsideRegion(ElementJeu gc, Rectangle selectedRegion) {
        return (gc.getX() + gc.getWidth() > selectedRegion.getX() &&
                gc.getX() < selectedRegion.getX() + selectedRegion.getWidth() &&
                gc.getY() + gc.getHeight() > selectedRegion.getY() &&
                gc.getY() < selectedRegion.getY() + selectedRegion.getHeight());
    }

    public static boolean isPointInRegion(Point p, Rectangle r) {
        return (p.getX() >= r.getX() &&
                p.getX() <= r.getX() + r.getWidth() &&
                p.getY() >= r.getY() &&
                p.getY() <= r.getY() + r.getHeight());
    }

    public static boolean isPointInTop(Point p, Rectangle r, int pad) {
        return p.getY() <= r.getY() + pad && p.getY() >= r.getY();
    }

    public static boolean isPointInBot(Point p, Rectangle r, int pad) {
        return p.getY() >= r.getY() + r.getHeight() - pad && p.getY() < r.getY() + r.getHeight();
    }

    public static boolean isPointInLeft(Point p, Rectangle r, int pad) {
        return p.getX() <= r.getX() + pad && p.getX() >= r.getX();
    }

    public static boolean isPointInRight(Point p, Rectangle r, int pad) {
        return p.getX() >= r.getX() + r.getWidth() - pad && p.getX() <= r.getX() + r.getWidth();
    }

    public static boolean hasIntersection(ElementJeu a, ElementJeu b) {
        Point pa = a.getPoint();
        Point pb = b.getPoint();
        if (pa.x == pb.x && pa.y == pb.y) {
            return true; // Overlapping.
        }

        boolean withinHeight = false, withinWidth = false;

        if (pb.x > pa.x) {
            if (pb.x - pa.x < a.getWidth()) {
                withinWidth = true;
            }
        } else {
            if (pa.x - pb.x < b.getWidth()) {
                withinWidth = true;
            }
        }

        if (pb.y > pa.y) {
            if (pb.y - pa.y < a.getHeight()) {
                withinHeight = true;
            }
        } else {
            if (pa.y - pb.y < b.getHeight()) {
                withinHeight = true;
            }
        }
        return withinWidth && withinHeight;
    }
}
