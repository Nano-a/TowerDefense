package gestionnaire;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import categories.GenreEnemie;
import composants.Adversaire;
import outils.Constantes;

public class GenerateurEnnemi {
    private Random random;

    private double msSinceLastSpawn = 0;
    private int wave;
    private int maxWave;

    private int startX, startY;

    private Queue<List<GenreEnemie>> spawnQueue;
    private Map<List<GenreEnemie>, Integer> spawnTimer;

    public GenerateurEnnemi() {
        this.random = new Random();
    }

    public void loadLevel(Map<List<GenreEnemie>, Integer> spawnTimer, Queue<List<GenreEnemie>> spawnQueue, int startX,
            int startY) {
        this.startX = startX;
        this.startY = startY;
        this.spawnQueue = spawnQueue;
        this.spawnTimer = spawnTimer;
        msSinceLastSpawn = 0;
        maxWave = spawnQueue.size();
        wave = 0;
    }

    public Map<Adversaire, Integer> update(int timeMillis) {
        Map<Adversaire, Integer> ret = new HashMap<>();
        if (spawnQueue.isEmpty()) {
            System.err.println("Calling update when spawn empty");
            return ret;
        }

        msSinceLastSpawn += timeMillis;

        boolean spawnRepeat = true;

        while (spawnRepeat) {
            int timeToNext = spawnTimer.get(spawnQueue.peek());

            if (timeToNext <= msSinceLastSpawn / 1000) {
                System.out.println("Spawning Wave");
                wave++;
                msSinceLastSpawn -= timeToNext * 1000;
                List<GenreEnemie> toSpawnTypes = spawnQueue.poll();

                List<Adversaire> mobs = createMobs(toSpawnTypes);
                mobs.forEach(e -> {
                    int randomTimeMS = random.nextInt(2000);
                    ret.put(e, randomTimeMS);
                });

                spawnTimer.remove(toSpawnTypes);

                if (spawnQueue.isEmpty()) {
                    spawnRepeat = false;
                }
            } else {
                spawnRepeat = false;
            }
        }
        return ret;
    }

    private List<Adversaire> createMobs(List<GenreEnemie> next) {
        List<Adversaire> enemies = new LinkedList<>();
        Random random = new Random();
        int x = startX;
        int y = startY;

        for (GenreEnemie type : next) {
            enemies.add(new Adversaire(type, x, y));
            x -= random.nextInt(10) * 5;
            y = startY + random.nextInt((int) Math.round(Constantes.DEFAULT_BLOCK_SIZE) - 25);
        }
        return enemies;
    }

    public String getWave() {
        return String.valueOf(wave) + "/" + String.valueOf(maxWave);
    }

    public int getTimeToNextWave() {
        return spawnTimer.get(spawnQueue.peek()) - (int) (msSinceLastSpawn / 1000.0);
    }

    public boolean isDoneSpawn() {
        return spawnQueue.isEmpty();
    }

    public void reset() {
    }
}
