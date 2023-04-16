package me.tomisanhues2.bantrial.database;

import me.tomisanhues2.bantrial.data.BanData;
import me.tomisanhues2.bantrial.data.History;

import java.util.ArrayList;
import java.util.UUID;

public interface IDatabase {

    boolean reconConn();
    void processBan(BanData banData);
    void deleteTables();
    void expireBan(UUID playerUUID);
    void addPlayerToDatabase(BanData banData);
    void addNewHistory(BanData banData);
    void addActiveBan(BanData banData);
    void updateHistoryStatus(UUID playerUUID, String status);
    void createTables();
    void removeActiveBan(UUID playerUUID);
    ArrayList<History> getHistories(UUID playerUUID);
    boolean isPlayerActiveBan(UUID playerUUID);




}
