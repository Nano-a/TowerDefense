package mecaniques;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import categories.GenreEnemie;
import outils.Constantes;
import outils.LecteurDeFichier;

import java.util.*;

public class AnalyseurNivJson {

    private String name, map;
    private Map<List<GenreEnemie>, Integer> spawnTimer;
    private Queue<List<GenreEnemie>> spawnQueue;

    public AnalyseurNivJson() {
        spawnTimer = new HashMap<>();
        spawnQueue = new LinkedList<>();
    }

    public void readLevel(int level) {
        spawnTimer.clear();
        spawnQueue.clear();

        String contents = LecteurDeFichier.readFile(Constantes.LEVEL_DIR + String.valueOf(level) + ".json");
        parseJson(contents);
    }

    public String getFile() {
        return Constantes.MAP_DIR + map;
    }

    private void parseJson(String contents) {
        try {
            JSONObject jo = new JSONObject(contents);
            JSONObject sub = jo.getJSONObject("level");

            this.name = sub.getString("name");
            this.map = sub.getString("map");

            JSONArray ja = sub.getJSONArray("enemy-spawn");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject wave = ja.getJSONObject(i);
                List<GenreEnemie> waveSpawn = parseEnemyType(wave.getString("spawn"));

                spawnQueue.add(waveSpawn);
                spawnTimer.put(waveSpawn, wave.getInt("Duration"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private List<GenreEnemie> parseEnemyType(String spawn) {
        List<GenreEnemie> wave = new LinkedList<>();
        String[] names = spawn.split(",");
        for (String s : names) {
            for (GenreEnemie e : GenreEnemie.values()) {
                if (e.getShorthand().equalsIgnoreCase(s)) {
                    wave.add(e);
                }
            }
        }
        return wave;
    }

    public Map<List<GenreEnemie>, Integer> getSpawnTime() {
        return spawnTimer;
    }

    public Queue<List<GenreEnemie>> getSpawnQueue() {
        return spawnQueue;
    }

}
