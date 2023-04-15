package me.tomisanhues2.bantrial;

import me.tomisanhues2.bantrial.commands.BanCommand;
import me.tomisanhues2.bantrial.commands.HistoryCommand;
import me.tomisanhues2.bantrial.commands.UnbanCommand;
import me.tomisanhues2.bantrial.config.Config;
import me.tomisanhues2.bantrial.config.Messages;
import me.tomisanhues2.bantrial.database.Database;
import me.tomisanhues2.bantrial.database.MySQLDatabase;
import me.tomisanhues2.bantrial.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

public class Ban extends JavaPlugin {
    private static Ban instance;

    public Messages messages;
    public Config config;
    public Utils utils;
    public Database database;

    {
        instance = this;
    }

    public static Ban getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        messages = new Messages(this);
        config = new Config(this);
        utils = new Utils(this);
        database = new MySQLDatabase();
        if (database.connection != null) {
            getLogger().info("Connected to database");
        } else {
            getLogger().severe("Could not connect to database");
            getLogger().severe("Disabling plugin");
        }
        this.database.createTables();

        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("unban").setExecutor(new UnbanCommand(this));
        getCommand("history").setExecutor(new HistoryCommand(this));
    }

}
