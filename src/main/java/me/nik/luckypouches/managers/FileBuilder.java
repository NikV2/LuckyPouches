package me.nik.luckypouches.managers;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public final class FileBuilder {
    private final File file;

    private final YamlConfiguration configuration;

    public FileBuilder(String FilePath, String FileName) {
        this.file = new File(FilePath, FileName);
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void setValue(String ValuePath, Object Value) {
        this.configuration.set(ValuePath, Value);
    }

    public int getInt(String ValuePath) {
        return this.configuration.getInt(ValuePath);
    }

    public String getString(String ValuePath) {
        return this.configuration.getString(ValuePath);
    }

    public boolean getBoolean(String ValuePath) {
        return this.configuration.getBoolean(ValuePath);
    }

    public void save() {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}