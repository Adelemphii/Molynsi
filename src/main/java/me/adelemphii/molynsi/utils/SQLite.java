package me.adelemphii.molynsi.utils;

import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.utils.player.User;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class SQLite {

    private final Molynsi plugin;
    public SQLite(Molynsi plugin) {
        this.plugin = plugin;
        createPlayerTable();
    }

    String url = "jdbc:sqlite:/AMP/Minecraft/plugins/Molynsi/data/players.db";
    private static Connection connection;

    public boolean connect() {
        if(!isConnected()) {
            try {
                connection = DriverManager.getConnection(url);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean disconnect() {
        if(isConnected()) {
            try {
                connection.close();
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
        return true;
    }

    public boolean isConnected() {
        return (connection != null);
    }

    // getConnection
    public Connection getConnection() {
        return connection;
    }

    private void createPlayerTable() {
        String sql = "CREATE TABLE IF NOT EXISTS players (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	uuid text NOT NULL,\n"
                + " alive integer,\n"
                + " infected integer,\n" // booleans are stored as either 0 (false) or 1 (true),
                + " turned integer,\n"
                + " timeInfected integer,\n" // the seconds since UNIX time
                + " statsApplied integer,\n"
                + " maxHealth real,\n"
                + " speed real\n"
                + ");";

        // TODO: Work on saving/reading to this

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            plugin.getLogger().info("Successfully created table.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveToPlayerTable() {
        String sql = "INSERT INTO players(id,uuid,alive,infected,turned,timeInfected,statsApplied,maxHealth,speed) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";

        Map<UUID, User> users = plugin.getUserManager().getUsers();

        if(isConnected()) {
            users.forEach((integer, user) -> {
                try(PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, user.getUuid().toString());
                    ps.setString(2, user.getUuid().toString());

                    ps.setBoolean(3, user.isAlive());
                    ps.setBoolean(4, user.isInfected());
                    ps.setBoolean(5, user.isTurned());

                    long secondsSinceUnix = 0;
                    if(user.getTimeInfected() != null) secondsSinceUnix = (user.getTimeInfected() / 1000);
                    ps.setLong(6, secondsSinceUnix);

                    ps.setBoolean(7, user.isStatsApplied());
                    ps.setDouble(8, user.getMaxHealth());

                    ps.setFloat(9, user.getSpeed());
                    ps.executeUpdate();
                } catch(SQLException e) {
                    updatePlayerInTable(user);
                }
            });
        } else {
            this.connect();
            users.forEach((integer, user) -> {
                try(PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, user.getUuid().toString());
                    ps.setString(2, user.getUuid().toString());

                    ps.setBoolean(3, user.isAlive());
                    ps.setBoolean(4, user.isInfected());
                    ps.setBoolean(5, user.isTurned());

                    long secondsSinceUnix = 0;
                    if(user.getTimeInfected() != null) secondsSinceUnix = (user.getTimeInfected() / 1000);
                    ps.setLong(6, secondsSinceUnix);

                    ps.setBoolean(7, user.isStatsApplied());
                    ps.setDouble(8, user.getMaxHealth());

                    ps.setFloat(9, user.getSpeed());
                    ps.executeUpdate();
                } catch(SQLException e) {
                    updatePlayerInTable(user);
                }
            });
        }
    }

    public void updatePlayerInTable(User user) {
        String updateSQL = "UPDATE players SET uuid = ? ," +
                "alive = ? ," +
                "infected = ? ," +
                "turned = ? ," +
                "timeInfected = ? ," +
                "statsApplied = ? ," +
                "maxHealth = ? ," +
                "speed = ? " +
                "WHERE id = ?";

        if(isConnected()) {
            try(PreparedStatement ps = connection.prepareStatement(updateSQL)) {
                ps.setString(1, user.getUuid().toString());
                ps.setBoolean(2, user.isAlive());

                ps.setBoolean(3, user.isInfected());
                ps.setBoolean(4, user.isTurned());

                long secondsSinceUnix = 0;
                if(user.getTimeInfected() != null) secondsSinceUnix = (user.getTimeInfected() / 1000);
                ps.setLong(5, secondsSinceUnix);

                ps.setBoolean(6, user.isStatsApplied());
                ps.setDouble(7, user.getMaxHealth());
                ps.setFloat(8, user.getSpeed());
                ps.setString(9, user.getUuid().toString());
                ps.executeUpdate();

            } catch(SQLException e) {
                System.out.println(e.getMessage());
                plugin.getLogger().log(Level.SEVERE, "Failed to update Player Table in Database.");
            }
        } else {
            this.connect();
            try(PreparedStatement ps = connection.prepareStatement(updateSQL)) {
                ps.setString(1, user.getUuid().toString());
                ps.setBoolean(2, user.isAlive());

                ps.setBoolean(3, user.isInfected());
                ps.setBoolean(4, user.isTurned());

                long secondsSinceUnix = 0;
                if(user.getTimeInfected() != null) secondsSinceUnix = (user.getTimeInfected() / 1000);
                ps.setLong(5, secondsSinceUnix);

                ps.setBoolean(6, user.isStatsApplied());
                ps.setDouble(7, user.getMaxHealth());
                ps.setFloat(8, user.getSpeed());
                ps.setString(9, user.getUuid().toString());
                ps.executeUpdate();

            } catch(SQLException e) {
                System.out.println(e.getMessage());
                plugin.getLogger().log(Level.SEVERE, "Failed to save to Player Table in Database.");
            }
        }
    }

    public void collectAllUsersFromTable() {
        String sql = "SELECT * FROM players";

        Map<UUID, User> userMap = new HashMap<>();

        try {
            if(isConnected()) {
                Statement stmt  = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while(rs.next()) {

                    User user = new User(
                            UUID.fromString(rs.getString("uuid")),
                            rs.getBoolean("alive"),
                            rs.getBoolean("infected"),
                            rs.getBoolean("turned"),
                            rs.getLong("timeInfected"),
                            rs.getBoolean("statsApplied"),
                            rs.getDouble("maxHealth"),
                            rs.getFloat("speed")
                    );
                    userMap.put(user.getUuid(), user);
                }
            } else {
                this.connect();
                Statement stmt  = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while(rs.next()) {

                    User user = new User(
                            UUID.fromString(rs.getString("uuid")),
                            rs.getBoolean("alive"),
                            rs.getBoolean("infected"),
                            rs.getBoolean("turned"),
                            rs.getLong("timeInfected"),
                            rs.getBoolean("statsApplied"),
                            rs.getDouble("maxHealth"),
                            rs.getFloat("speed")
                    );
                    userMap.put(user.getUuid(), user);
                }
            }
            plugin.getLogger().info("Successfully collected users to hashmap.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(":thinking_emoji:");
        }

        plugin.getUserManager().setUsers(userMap);
    }
}
