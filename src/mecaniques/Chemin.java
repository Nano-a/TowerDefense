package mecaniques;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import categories.NatureTerrain;
import composants.ChampBataille;

public class Chemin {
    private ChampBataille[][] terrains;

    public Chemin(ChampBataille[][] terrains) {
        this.terrains = terrains;

    }

    public List<ChampBataille> generatePath(ChampBataille start, ChampBataille destination) {
        Set<ChampBataille> open = new HashSet<>();
        Set<ChampBataille> closed = new HashSet<>();
        Map<ChampBataille, ChampBataille> cameFrom = new HashMap<>();
        Map<ChampBataille, Integer> gScore = new HashMap<>();
        Map<ChampBataille, Integer> fScore = new HashMap<>();

        for (int i = 0; i < terrains.length; i++) {
            for (int j = 0; j < terrains[i].length; j++) {
                ChampBataille t = terrains[i][j];
                gScore.put(t, Integer.MAX_VALUE);
                fScore.put(t, Integer.MAX_VALUE);
            }
        }

        gScore.put(start, 0);
        fScore.put(start, estimateDistanceToEnd(start, destination));
        open.add(start);

        ChampBataille curr = start;

        while (open.size() != 0) {
            curr = getLowestInSet(gScore.get(curr), curr, open, destination);

            if (curr == destination) {
                System.out.println("Proper Path found");
                return generatePath(cameFrom, start, destination);
            }

            Set<ChampBataille> neigh = getNearTerrain(curr.getX(), curr.getY());
            for (ChampBataille tmp : neigh) {
                if (!open.contains(tmp) && !closed.contains(tmp)) {
                    double tmpG = gScore.get(curr) + getDistanceBetween(curr, tmp);

                    if (tmpG >= gScore.get(tmp)) {
                        continue;
                    }

                    int cost = (int) tmpG + estimateDistanceToEnd(tmp, destination);
                    cameFrom.put(tmp, curr);
                    gScore.put(tmp, Integer.valueOf((int) tmpG));
                    fScore.put(tmp, cost);
                    open.add(tmp);

                }
            }

            open.remove(curr);
            closed.add(curr);

        }

        System.out.println("No path");
        return null;
    }

    private List<ChampBataille> generatePath(Map<ChampBataille, ChampBataille> cameFrom, ChampBataille start, ChampBataille destination) {
        Stack<ChampBataille> stack = new Stack<>();

        ChampBataille curr = destination;
        while (curr != start) {
            stack.push(curr);
            curr = cameFrom.get(curr);
        }

        List<ChampBataille> path = new ArrayList<>();
        while (!stack.empty()) {
            path.add(stack.pop());
        }
        return path;
    }

    private Integer getDistanceBetween(ChampBataille n, ChampBataille n2) {
        return (int) Math.round(Math.sqrt(
                Math.pow(Math.abs(n.getX() - n2.getX()), 2) +
                        Math.pow(Math.abs(n.getY() - n2.getY()), 2)));
    }

    private Integer estimateDistanceToEnd(ChampBataille n, ChampBataille destination) {
        return (int) Math.round(Math.sqrt(
                Math.pow(Math.abs(n.getX() - destination.getX()), 2) +
                        Math.pow(Math.abs(n.getY() - destination.getY()), 2)));
    }

    private ChampBataille getLowestInSet(Integer curG, ChampBataille curr, Set<ChampBataille> toSearch, ChampBataille destination) {
        double min = Integer.MAX_VALUE;
        ChampBataille lowest = null;
        for (ChampBataille tt : toSearch) {
            double h = estimateDistanceToEnd(tt, destination);
            double g = getDistanceBetween(curr, tt) + curG;

            if (g + h < min) {
                min = g + h;
                lowest = tt;
            }
        }
        return lowest;
    }

    private Set<ChampBataille> getNearTerrain(int x, int y) {
        int ySize = terrains.length;
        int xSize = terrains[0].length;

        Set<ChampBataille> queue = new HashSet<>();

        if (x > 0) {
            addIf(queue, terrains[y][x - 1]);
            if (y > 0) {
            }

            if (y < ySize - 1) {
            }
        }

        if (y > 0) {
            addIf(queue, terrains[y - 1][x]);
            if (x < xSize - 1) {
            }
        }

        if (y < ySize - 1) {
            addIf(queue, terrains[y + 1][x]);
            if (x < xSize - 1) {
            }
        }
        if (x < xSize - 1) {
            addIf(queue, terrains[y][x + 1]);
        }

        return queue;

    }

    private static void addIf(Set<ChampBataille> queue, ChampBataille terrain) {
        NatureTerrain type = terrain.getType();
        if (type == NatureTerrain.START || type == NatureTerrain.NEXUS || type == NatureTerrain.MOVEABLE) {
            queue.add(terrain);
        }
    }
}
