package me.tomisanhues2.bantrial;

import org.bukkit.plugin.java.JavaPlugin;

public class Ban extends JavaPlugin {
    private static Ban instance;

    {
        instance = this;
    }

    public static Ban getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

    }

}
