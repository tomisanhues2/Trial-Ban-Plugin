package me.tomisanhues2.bantrial.data;

import me.tomisanhues2.bantrial.Ban;
import me.tomisanhues2.bantrial.utils.BanType;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class BanData {

    private final Ban plugin = Ban.getInstance();
    private final UUID playerUUID;

    private Date banDate;
    private String duration;
    private String reason;
    private UUID bannerUUID;

    @Nullable
    private Date unbanDate;

    public BanData(UUID playerUUID, Date banDate, String duration, String reason, UUID bannerUUID) {
        this.playerUUID = playerUUID;
        this.banDate = banDate;
        this.duration = duration;
        this.reason = reason;
        this.bannerUUID = bannerUUID;
        if (duration.equalsIgnoreCase("PERM_BAN")) {
            this.unbanDate = null;
        } else {
            this.unbanDate = plugin.utils.calculateUnbanDate(duration);
        }

    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }


    public Date getBanDate() {
        return banDate;
    }

    public String getDuration() {
        return duration;
    }

    public String getReason() {
        return reason;
    }

    @Nullable
    public Date getUnbanDate() {
        return unbanDate;
    }

    public String getUnbanDateString() {
        if (this.unbanDate == null) {
            return null;
        } else {
            return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(this.unbanDate);
        }
    }

    public void setBanDate(Date banDate) {
        this.banDate = banDate;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setBannerUUID(UUID bannerUUID) {
        this.bannerUUID = bannerUUID;
    }

    public void setUnbanDate(@Nullable Date unbanDate) {
        this.unbanDate = unbanDate;
    }

    public UUID getBannerUUID() {
        return this.bannerUUID;
    }
}
