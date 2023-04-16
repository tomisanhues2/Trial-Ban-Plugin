package me.tomisanhues2.bantrial.listeners;

import me.tomisanhues2.bantrial.Ban;
import me.tomisanhues2.bantrial.data.BanData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Date;
import java.util.UUID;

public class PlayerListeners implements Listener {
    private final Ban plugin;

    public PlayerListeners(Ban plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
        UUID playerUUID = event.getUniqueId();
        if (!plugin.database.isPlayerActiveBan(playerUUID)) {
            return;
        }
        BanData banData = plugin.database.getActiveBan(playerUUID);
        if (banData == null) {
            return;
        }
        Date date = new Date();
        if (date.after(banData.getUnbanDate())) {
            plugin.database.expireBan(playerUUID);
            return;
        }

        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, "You are banned");

    }
}
