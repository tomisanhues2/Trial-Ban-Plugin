package me.tomisanhues2.bantrial.database;

import me.tomisanhues2.bantrial.data.BanData;

public interface IDatabase {

    boolean reconConn();
    void createTables();
    void saveBan(BanData banData);
}
