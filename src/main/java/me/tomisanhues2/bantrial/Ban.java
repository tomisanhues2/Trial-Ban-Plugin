package me.tomisanhues2.bantrial;

import me.tomisanhues2.bantrial.commands.BanCommand;
import me.tomisanhues2.bantrial.commands.HistoryCommand;
import me.tomisanhues2.bantrial.commands.UnbanCommand;
import me.tomisanhues2.bantrial.config.Config;
import me.tomisanhues2.bantrial.config.Messages;
import me.tomisanhues2.bantrial.data.BanManager;
import me.tomisanhues2.bantrial.database.Database;
import me.tomisanhues2.bantrial.database.MySQLDatabase;
import me.tomisanhues2.bantrial.gui.GUIListener;
import me.tomisanhues2.bantrial.gui.GUIManager;
import me.tomisanhues2.bantrial.listeners.CustomListeners;
import me.tomisanhues2.bantrial.listeners.PlayerListeners;
import me.tomisanhues2.bantrial.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Ban extends JavaPlugin {
    private static Ban instance;
    public Messages messages;
    public Config config;
    public Utils utils;
    public Database database;
    public BanManager banManager;
    public GUIManager guiManager;


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
        banManager = new BanManager(this);

        //GUI Initialization
        guiManager = new GUIManager();
        GUIListener guiListener = new GUIListener(guiManager);
        getServer().getPluginManager().registerEvents(guiListener, this);
        getServer().getPluginManager().registerEvents(new CustomListeners(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);

        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("unban").setExecutor(new UnbanCommand(this));
        getCommand("history").setExecutor(new HistoryCommand(this));

    }

    @Override
    public void onDisable() {
        if (database.connection != null) {
            try {
                database.connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
