package ru.mipt.engocab.core.config;

import ru.mipt.engocab.data.json.DataMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Alexander Ushakov
 */
public class Configuration {

    private static final String CONFIG_DIR_NAME = ".engocab";
    private static final String SETTINGS = "settings.json";
    private static final String USER_HOME = "user.home";
    //private static final String LINUX_HOME = "HOME";

    // working dir
    private static final String USER_DIR = "user.dir";

    private File settingsFile;

    public Configuration() {
        this.settingsFile = getSettingsFile();
        System.out.println(settingsFile.getParentFile());
    }

    public Settings load() {
        Settings settings = null;
        try {
            InputStream input = new FileInputStream(settingsFile);
            settings = DataMapper.deserializeFromStream(input, Settings.class);

        } catch (IOException ex) {
            throw new ConfigException();
        }
        return settings;
    }

    public void store(Settings settings) throws IOException {
        try (FileWriter writer = new FileWriter(settingsFile)) {
            String serialized = DataMapper.serialize(settings);
            writer.write(serialized);
            writer.flush();
        }
    }

    private File getSettingsFile() {
        String userDirPath = System.getProperty(USER_DIR);
        File settingsFile = null;
        if (userDirPath != null) {
            settingsFile = new File(userDirPath + File.separator + CONFIG_DIR_NAME + File.separator + SETTINGS);
        }
        if (settingsFile == null || !settingsFile.exists()) {

            String configDirPath = System.getProperty(USER_HOME) + File.separator + CONFIG_DIR_NAME;
            File configDir = new File(configDirPath);
            if (!configDir.exists()) {
                boolean created = configDir.mkdir();
                if (!created) {
                    throw new ConfigException();
                }
            }

            settingsFile = new File(configDirPath + File.separator + SETTINGS);
            if (!settingsFile.exists()) {
                try {
                    boolean created = settingsFile.createNewFile();
                    if (!created) {
                        throw new ConfigException();
                    }
                    try(FileWriter writer = new FileWriter(settingsFile)) {
                        writer.write("{}");
                        writer.flush();
                    }
                } catch (IOException e) {
                    throw new ConfigException();
                }
            }
        }

        return settingsFile;
    }

}
