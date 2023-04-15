package me.tomisanhues2.bantrial.database;

import me.tomisanhues2.bantrial.Ban;

import java.sql.Connection;

public abstract class Database implements IDatabase {

    protected Ban plugin = Ban.getInstance();

    public Connection connection;

    public Database() {
        connection = null;
    }
}
