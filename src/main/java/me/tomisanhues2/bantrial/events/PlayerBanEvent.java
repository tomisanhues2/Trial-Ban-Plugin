package me.tomisanhues2.bantrial.events;

import me.tomisanhues2.bantrial.data.BanData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerBanEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final BanData banData;

    public PlayerBanEvent(BanData banData) {
        this.banData = banData;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public BanData getBanData() {
        return banData;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
