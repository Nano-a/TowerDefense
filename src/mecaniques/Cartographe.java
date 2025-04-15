package mecaniques;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import categories.NatureTerrain;

public class Cartographe {

    public static NatureTerrain[][] parseTerrainFile(String s) {
        String[][] data = parseCSVFile(s);

        List<NatureTerrain[]> terrainTypes = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            List<NatureTerrain> tetPart = new ArrayList<>();

            for (int j = 0; j < data[i].length; j++) {
                for (NatureTerrain t : NatureTerrain.getTerrains()) {
                    if (t.getType().equals(data[i][j])) {
                        tetPart.add(t);
                        break;
                    }
                }
            }
            terrainTypes.add(tetPart.toArray(new NatureTerrain[tetPart.size()]));
        }
        return terrainTypes.toArray(new NatureTerrain[terrainTypes.size()][]);

    }

    private static String[][] parseCSVFile(String s) {

        List<String[]> data = new ArrayList<>();

        try {
            System.out.println("Loading file " + s);
            InputStream in = Cartographe.class.getClassLoader().getResourceAsStream(s);

            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(",");

                data.add(temp);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Shouldn't happen");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data.toArray(new String[data.size()][]);
    }
}
