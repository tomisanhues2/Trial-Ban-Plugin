package me.tomisanhues2.bantrial.database;

import me.tomisanhues2.bantrial.data.BanData;
import me.tomisanhues2.bantrial.data.History;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Date;
import java.util.*;

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
        allowPKR =
                plugin.config.getConfig().getBoolean("database.allow-public-key-retrieval");
        URL =
                "jdbc:mysql://" + this.HOST + ":" + this.PORT + "/" + this.DATABASE + "?useSSL=" + this.useSSL + "&allowPublicKeyRetrieval=" + this.allowPKR;
        DATABASE_NAME = plugin.config.getConfig().getString("database.database-name");
        try {
            plugin.getLogger().info("Attempting to connect to MySQL database at " + this.HOST + ":" + this.PORT + "...");
            Class.forName("com.mysql.jdbc.Driver");
            this.connection =
                    DriverManager.getConnection(this.URL, this.USERNAME, this.PASSWORD);
            this.connection.setAutoCommit(true);
        } catch (ClassNotFoundException | SQLException e) {
            plugin.getLogger().severe("Failed to connect to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe(e.getMessage());
        }
    }

    public boolean reconConn() {
        try {
            this.connection =
                    DriverManager.getConnection(this.URL, this.USERNAME, this.PASSWORD);
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe(e.getMessage());
            return false;
        }
    }

    public void createTables() {
        try {
            Statement statement = getScriptStatements("db-setup.sql");
            assert statement != null;
            statement.executeBatch();
            statement.close();

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to create tables in MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe(e.getMessage());
        }
    }

    private Statement getScriptStatements(String fileName) {
        try {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(Objects.requireNonNull(plugin.getResource(fileName))));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            String[] queries = builder.toString().split(";");
            Statement statement = this.connection.createStatement();
            for (String query : queries) {
                statement.addBatch(query);
            }
            return statement;
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to create tables in MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe(e.getMessage());
            return null;
        }
    }

    public void deleteTables() {
        try {
            Statement statement = getScriptStatements("db-delete.sql");
            assert statement != null;
            statement.executeBatch();
            statement.close();
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to create tables in MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe(e.getMessage());
        }
    }

    public void processBan(BanData ban) {
        addActiveBan(ban);
        addNewHistory(ban);
        if (!isPlayerInList(ban.getPlayerUUID())) addPlayerToDatabase(ban);
    }

    public void expireBan(UUID uuid) {
        removeActiveBan(uuid);
        updateHistoryStatus(uuid, "EXPIRED");
    }

    public void addPlayerToDatabase(BanData ban) {
        try {
            PreparedStatement ps =
                    this.connection.prepareStatement("INSERT INTO banned_players (player_uuid) VALUES (?)");
            ps.setString(1, ban.getPlayerUUID().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save player to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe("Attempting to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "...");
            e.printStackTrace();
            if (reconConn())
                plugin.getLogger().info("Successfully reconnected to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            else
                plugin.getLogger().severe("Failed to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "!");
        }
    }

    public void addActiveBan(BanData banData) {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT IGNORE INTO " + this.DATABASE_NAME + ".active_bans (player_uuid, banner_uuid, ban_reason, ban_date, ban_duration, unban_date) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, banData.getPlayerUUID().toString());
            ps.setString(2, banData.getBannerUUID().toString());
            ps.setString(3, banData.getReason());
            ps.setLong(4, banData.getBanDate().getTime());
            ps.setString(5, banData.getDuration());
            ps.setLong(6, -1);
            if (banData.getUnbanDate() != null) ps.setLong(6,
                    banData.getUnbanDate() == null ? -1
                                                   : banData.getUnbanDate().getTime());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save banData to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe("Attempting to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "...");
            e.printStackTrace();
            if (reconConn())
                plugin.getLogger().info("Successfully reconnected to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            else
                plugin.getLogger().severe("Failed to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "!");
        }
    }

    public void addNewHistory(BanData banData) {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO " + this.DATABASE_NAME + ".player_history (player_uuid, banner_uuid, ban_reason, ban_date, ban_duration, unban_date, ban_status) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, banData.getPlayerUUID().toString());
            ps.setString(2, banData.getBannerUUID().toString());
            ps.setString(3, banData.getReason());
            ps.setLong(4, banData.getBanDate().getTime());
            ps.setString(5, banData.getDuration());
            ps.setLong(6, -1);
            if (banData.getUnbanDate() != null)
                ps.setLong(6, banData.getUnbanDate().getTime());
            ps.setString(7, "ACTIVE");
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save ban to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe("Attempting to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "...");
            e.printStackTrace();
            if (reconConn())
                plugin.getLogger().info("Successfully reconnected to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            else
                plugin.getLogger().severe("Failed to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "!");
        }
    }

    public void removeActiveBan(UUID uuid) {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("DELETE FROM " + this.DATABASE_NAME + ".active_bans WHERE player_uuid = ?");
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to remove ban from MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe("Attempting to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "...");
            if (reconConn())
                plugin.getLogger().info("Successfully reconnected to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            else
                plugin.getLogger().severe("Failed to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "!");
        }
    }

    public void updateHistoryStatus(UUID uuid, String status) {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("UPDATE " + this.DATABASE_NAME + ".player_history SET ban_status = ? WHERE player_uuid = ? AND ban_status = 'ACTIVE'");
            ps.setString(1, status);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to update ban status in MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe("Attempting to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "...");
            if (reconConn())
                plugin.getLogger().info("Successfully reconnected to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            else
                plugin.getLogger().severe("Failed to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "!");
        }
    }

    public ArrayList<History> getHistories(UUID playerUUID) {
        ArrayList<History> histories = new ArrayList<>();
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM " + this.DATABASE_NAME + ".player_history WHERE player_uuid = ? ORDER BY history_id DESC");
            ps.setString(1, playerUUID.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                History history =
                        new History(rs.getInt(1), UUID.fromString(rs.getString(2)), UUID.fromString(rs.getString(3)), rs.getString(4), new Date(rs.getLong(5)), rs.getString(6), new Date(rs.getLong(7)), rs.getString(8));
                histories.add(history);
            }
            return histories;
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to get ban history from MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe("Attempting to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "...");
            if (reconConn())
                plugin.getLogger().info("Successfully reconnected to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            else
                plugin.getLogger().severe("Failed to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "!");
        }
        return null;
    }

    public boolean isPlayerActiveBan(UUID uuid) {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM " + this.DATABASE_NAME + ".active_bans WHERE player_uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to check ban status in MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe("Attempting to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "...");
            if (reconConn())
                plugin.getLogger().info("Successfully reconnected to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            else
                plugin.getLogger().severe("Failed to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "!");
        }
        return false;
    }

    public boolean isPlayerInList(UUID uuid) {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM " + this.DATABASE_NAME + ".banned_players WHERE player_uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to check ban status in MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe("Attempting to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "...");
            if (reconConn())
                plugin.getLogger().info("Successfully reconnected to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            else
                plugin.getLogger().severe("Failed to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "!");
        }
        return false;
    }

    public BanData getActiveBan(UUID playerUUID) {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM " + this.DATABASE_NAME + ".active_bans WHERE player_uuid = ?");
            ps.setString(1, playerUUID.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                BanData banData = new BanData(playerUUID);
                banData.setBannerUUID(UUID.fromString(rs.getString(3)));
                banData.setReason(rs.getString(4));
                banData.setBanDate(new Date(rs.getLong(5)));
                banData.setDuration(rs.getString(6));
                banData.processUnbanDate();
                return banData;
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to get ban data from MySQL database at " + this.HOST + ":" + this.PORT + "!");
            plugin.getLogger().severe("Attempting to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "...");
            e.printStackTrace();
            if (reconConn())
                plugin.getLogger().info("Successfully reconnected to MySQL database at " + this.HOST + ":" + this.PORT + "!");
            else
                plugin.getLogger().severe("Failed to reconnect to MySQL database at " + this.HOST + ":" + this.PORT + "!");
        }
        return null;
    }

}
