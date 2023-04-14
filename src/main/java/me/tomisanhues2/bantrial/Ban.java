package me.tomisanhues2.bantrial;

import me.tomisanhues2.bantrial.commands.BanCommand;
import me.tomisanhues2.bantrial.commands.HistoryCommand;
import me.tomisanhues2.bantrial.commands.UnbanCommand;
import me.tomisanhues2.bantrial.config.Config;
import me.tomisanhues2.bantrial.config.Messages;
import org.bukkit.plugin.java.JavaPlugin;

public class Ban extends JavaPlugin {
    private static Ban instance;

    public Messages messages;

    public Config config;

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

        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("unban").setExecutor(new UnbanCommand(this));
        getCommand("history").setExecutor(new HistoryCommand(this));
    }

}
