package me.tomisanhues2.bantrial.database;

import me.tomisanhues2.bantrial.Ban;

import java.sql.Connection;

public abstract class Database implements IDatabase {
    public Connection connection;
    protected Ban plugin = Ban.getInstance();
    public Database() {
        connection = null;
    }
}
