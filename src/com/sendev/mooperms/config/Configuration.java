package com.sendev.mooperms.config;

import com.sendev.mooperms.MooPerms;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Configuration
{

    private final JavaPlugin plugin;
    private final String fileName;
    private File folder;
    private File configFile;
    private FileConfiguration fileConfiguration;

    public Configuration(JavaPlugin plugin, String fileName)
    {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        if (!plugin.isInitialized()) {
            throw new IllegalArgumentException("plugin must be initiaized");
        }

        this.plugin = plugin;
        this.fileName = fileName;
        this.folder = plugin.getDataFolder();
        this.configFile = new File(folder, fileName);

        if (!configFile.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        this.fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    public Configuration(MooPerms plugin, File folder, String fileName)
    {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("plugin must be initiaized");
        }

        this.plugin = (JavaPlugin) plugin;
        this.fileName = fileName;
        this.folder = folder;
        this.configFile = new File(folder, fileName);

        if (!configFile.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        this.fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    public Configuration(MooPerms plugin, File folder, String fileName, List<String> yaml)
    {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("plugin must be initiaized");
        }

        this.plugin = (JavaPlugin) plugin;
        this.fileName = fileName;
        this.folder = folder;
        this.configFile = new File(folder, fileName);

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();

                FileWriter fw = new FileWriter(configFile.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);

                for (String line : yaml) {
                    bw.write(line + "\r\n");
                }

                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        this.fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    public void reloadConfig()
    {
        if (configFile == null) {
            if (folder == null) {
                throw new IllegalStateException();
            }
            configFile = new File(folder, fileName);
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource(fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            fileConfiguration.setDefaults(defConfig);
        }
    }

    public FileConfiguration getConfig()
    {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }
        return fileConfiguration;
    }

    public void saveConfig()
    {
        if (fileConfiguration == null || configFile == null) {
            return;
        } else {
            try {
                getConfig().save(configFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
            }
        }
    }

    public void saveDefaultConfig()
    {
        if (!configFile.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }
}
