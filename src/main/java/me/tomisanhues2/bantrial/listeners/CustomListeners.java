package me.tomisanhues2.bantrial.listeners;

import me.tomisanhues2.bantrial.Ban;
import me.tomisanhues2.bantrial.events.PlayerBanEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CustomListeners implements Listener {
    private final Ban plugin;

    public CustomListeners(Ban plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerBanEvent(PlayerBanEvent event) {
        if (!Bukkit.getOfflinePlayer(event.getBanData().getPlayerUUID()).isOnline())
            return;
        Player player = Bukkit.getPlayer(event.getBanData().getPlayerUUID());
        assert player != null;
        player.kickPlayer("You have been banned for: " + event.getBanData().getReason());
    }
}
