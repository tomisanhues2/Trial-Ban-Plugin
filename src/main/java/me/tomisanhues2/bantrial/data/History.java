package me.tomisanhues2.bantrial.data;

import me.tomisanhues2.bantrial.Ban;
import org.bukkit.Material;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class History {
    private static final Ban plugin = Ban.getInstance();
    private int id;
    private UUID playerUUID;
    private UUID bannerUUID;
    private String reason;
    private Date date;
    private Date unbanDate;
    private String duration;

    private String status;

    public int getId() {
        return id;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public UUID getBannerUUID() {
        return bannerUUID;
    }

    public String getReason() {
        return reason;
    }

    public Date getDate() {
        return date;
    }

    public String getDuration() {
        return duration;
    }

    public Date getUnbanDate() {
        return unbanDate;
    }

    public String getStatus() {
        return status;
    }

    public History(int id, UUID playerUUID, UUID bannerUUID, String reason, Date date, String duration, Date unbanDate, String status) {
        this.id = id;
        this.playerUUID = playerUUID;
        this.bannerUUID = bannerUUID;
        this.reason = reason;
        this.date = date;
        this.duration = duration;
        this.unbanDate = unbanDate;
        this.status = status;
    }

    public Material getMaterial() {
        if (status.equalsIgnoreCase("ACTIVE")) {
            return Material.REDSTONE_BLOCK;
        }
        return Material.LIME_STAINED_GLASS;
    }
}
