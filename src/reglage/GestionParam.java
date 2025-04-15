package reglage;

import com.fasterxml.jackson.databind.ObjectMapper;

import outils.Constantes;

import java.io.File;
import java.io.IOException;

public class GestionParam {

    private ObjectMapper mapper;
    private Param settings;

    public GestionParam() {
        mapper = new ObjectMapper();

        setupSaveFile();
    }

    public Param getSettings() {
        return settings;
    }

    public void readFile() {
        File file = new File(Constantes.SETTINGS_FILE);
        try {
            settings = mapper.readValue(file, Param.class);
        } catch (IOException e) {
            settings = new Param();
            save();
            e.printStackTrace();
        }
    }

    private File saveFile;

    private void setupSaveFile() {
        System.out.println("Checking save file");
        saveFile = new File(Constantes.SETTINGS_FILE);
        if (!saveFile.exists()) {
            System.out.println("File not found");
            saveFile.getParentFile().mkdirs();
            try {
                saveFile.createNewFile();
                writeDefaultFile();
                System.out.println("Creating new Settings File since none exist");
            } catch (IOException e) {
                e.printStackTrace();
            }
            saveFile.setWritable(true);
        } else {
            System.out.println("File found");
        }
    }

    public void writeDefaultFile() {
        settings = new Param();
        save();
    }

    public void save() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(saveFile, settings);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
