package me.nik.luckypouches.managers;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileBuilder {

    private final File file;

    private final YamlConfiguration configuration;

    public FileBuilder(String path, String name) {

        this.file = new File(path, name);

        if (!this.file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.configuration = YamlConfiguration.loadConfiguration(this.file);

        save();
    }

    public void setValue(String ValuePath, Object Value) {
        this.configuration.set(ValuePath, Value);
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    public void save() {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}