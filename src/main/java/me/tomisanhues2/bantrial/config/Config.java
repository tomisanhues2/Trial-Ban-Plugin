package me.tomisanhues2.bantrial.config;

import me.tomisanhues2.bantrial.Ban;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.concurrent.Callable;

public class Config {

    private final Ban plugin;
    private File file;
    private FileConfiguration config;

    public Config(Ban plugin) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) plugin.saveResource("config.yml", false);
        config = plugin.getConfig();
        try {
            config.load(file);
            plugin.getLogger().info("Loaded config.yml");
        } catch (Exception e) {
            plugin.getLogger().severe("Could not load config.yml");
            plugin.getLogger().severe(e.getMessage());
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
