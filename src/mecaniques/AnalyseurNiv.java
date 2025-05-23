package mecaniques;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AnalyseurNiv {
    public static List<String> parseLevels() {
        List<String> levelNames = new ArrayList<>();

        String root = "/Levels/";
        String fileName = "";
        String fileEnd = ".json";
        int ext = 1;
        while (true) {
            try {
                InputStream in = AnalyseurNiv.class
                        .getResourceAsStream(root + fileName + Integer.toString(ext) + fileEnd);
                if (in != null) {
                    levelNames.add(Integer.toString(ext));
                    ext++;
                } else {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

        }
        return levelNames;
    }
}
