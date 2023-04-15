package me.tomisanhues2.bantrial.database;

import me.tomisanhues2.bantrial.Ban;
import me.tomisanhues2.bantrial.data.BanData;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class MySQLDatabase extends Database {

    private final String HOST;
    private final String PORT;
    private final String USERNAME;
    private final String PASSWORD;
    private final String DATABASE;
    private final boolean useSSL;
    private final boolean allowPKR;
    private final String URL;
    private final String DATABASE_NAME;

    public MySQLDatabase() {
        HOST = plugin.config.getConfig().getString("database.host");
        PORT = plugin.config.getConfig().getString("database.port");
        USERNAME = plugin.config.getConfig().getString("database.username");
        PASSWORD = plugin.config.getConfig().getString("database.password");
        DATABASE = plugin.config.getConfig().getString("database.database-name");
        useSSL = plugin.config.getConfig().getBoolean("database.use-ssl");
        allowPKR = plugin.config.getConfig().getBoolean("database.allow-public-key-retrieval");
        URL = "jdbc:mysql://" + this.HOST + ":" + this.PORT + "/" + this.DATABASE + "?useSSL=" + this.useSSL + "&allowPublicKeyRetrieval=" + this.allowPKR;
        DATABASE_NAME = plugin.config.getConfig().getString("database.database-name");
        try {
            plugin.getLogger().info("Attempting to connect to MySQL database at " + this.HOST + ":" + this.PORT + "...");
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(this.URL, this.USERNAME, this.PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            plugin.getLogger().severe("Failed to connect to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe(e.getMessage());
        }
    }

    public boolean reconConn() {
        try {
            this.connection = DriverManager.getConnection(this.URL, this.USERNAME, this.PASSWORD);
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe(e.getMessage());
            return false;
        }
    }

    public void createTables() {
        ScriptRunner runner = new ScriptRunner(this.connection);
        Reader reader = new InputStreamReader(Objects.requireNonNull(plugin.getResource("db-setup.sql")));
        runner.runScript(reader);
    }

    public void deleteTables() {
        ScriptRunner runner = new ScriptRunner(this.connection);
        Reader reader = new InputStreamReader(Objects.requireNonNull(plugin.getResource("db-delete.sql")));
        runner.runScript(reader);
    }


    public void saveBan(BanData ban) {
        try {
            PreparedStatement ps = connection.prepareStatement("REPLACE INTO " + this.DATABASE_NAME + ".active_bans (ban_type, player_uuid, banner_uuid, ban_reason, ban_date, ban_duration, unban_date) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, ban.getBanType().toString());
            ps.setString(2, ban.getPlayerUUID().toString());
            ps.setString(3, ban.getBannerUUID().toString());
            ps.setString(4, ban.getReason());
            ps.setLong(5, ban.getBanDate().getTime());
            ps.setString(6, ban.getDuration());
            if (ban.getUnbanDate() != null)
                ps.setLong(7, ban.getUnbanDate().getTime());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save ban to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe("Attempting to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "...");
            if (reconConn())
                plugin.getLogger().info("Successfully reconnected to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            else
                plugin.getLogger().severe("Failed to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "!");
        }
    }

}
