package me.tomisanhues2.bantrial.data;

import lombok.NonNull;
import me.tomisanhues2.bantrial.Ban;
import me.tomisanhues2.bantrial.events.PlayerBanEvent;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class BanManager {
    private final Ban plugin;

    public BanManager(Ban plugin) {
        this.plugin = plugin;
    }

    public void addBan(UUID playerUUID, String reason, String duration, @NonNull UUID bannerUUID) {
        System.out.println("Adding ban");
        System.out.println("Player UUID: " + playerUUID);
        System.out.println("Reason: " + reason);
        System.out.println("Duration: " + duration);
        System.out.println("Banner UUID: " + bannerUUID);
        Objects.requireNonNull(Bukkit.getPlayer(bannerUUID)).sendMessage("Banned " + Bukkit.getOfflinePlayer(playerUUID).getName() + " for " + duration + " for " + reason);
        BanData banData =
                new BanData(playerUUID, new Date(), duration, reason, bannerUUID);
        plugin.database.processBan(banData);
        Bukkit.getPluginManager().callEvent(new PlayerBanEvent(banData));
    }

    public void removeBan(UUID playerUUID) {
        plugin.database.expireBan(playerUUID);
    }
}
