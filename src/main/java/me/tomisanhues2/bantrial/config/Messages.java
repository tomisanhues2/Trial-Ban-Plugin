package me.tomisanhues2.bantrial.config;

import me.tomisanhues2.bantrial.Ban;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class Messages {

    private final Ban plugin;
    private final String prefix;
    private File file;
    private FileConfiguration config;

    public Messages(Ban plugin) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) plugin.saveResource("messages.yml", false);
        config = plugin.getConfig();
        try {
            config.load(file);
            plugin.getLogger().info("Loaded messages.yml");
        } catch (Exception e) {
            plugin.getLogger().severe("Could not load messages.yml");
            plugin.getLogger().severe(e.getMessage());
        }

        if (config.getBoolean("use-prefix")) {
            prefix = config.getString("prefix");
        } else {
            prefix = "";
        }
    }

    public String getMessage(String path) {
        String message = config.getString(path);
        if (message == null) return "Message not found";
        return ChatColor.translateAlternateColorCodes('&',prefix + message);
    }
}
