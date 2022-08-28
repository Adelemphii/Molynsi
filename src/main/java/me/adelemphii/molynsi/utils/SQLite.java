package me.adelemphii.molynsi.utils;

import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.utils.player.User;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * The SQLite manager for the plugin
 */
public class SQLite {

    private final Molynsi plugin;
    private final String url;
    public SQLite(Molynsi plugin) {
        this.plugin = plugin;
        url = "jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "/data/players.db";
        createPlayerTable();
    }
    private static Connection connection;

    /**
     * Connect to the player database.
     * @return if connected
     */
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

    /**
     * Disconnect from the player database.
     * @return if disconnected
     */
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

    /**
     * If the database is connected.
     * @return if connected
     */
    public boolean isConnected() {
        return (connection != null);
    }

    /**
     * Get the connection.
     * @return connection
     */
    public Connection getConnection() {
        return connection;
    }

    private void createPlayerTable() {
        // booleans are stored as either 0 (false) or 1 (true),
        // the seconds since UNIX time
        String sql = """
                CREATE TABLE IF NOT EXISTS players (
                	id integer PRIMARY KEY,
                	uuid text NOT NULL,
                 alive integer,
                 infected integer,
                 turned integer,
                 timeInfected integer
                );""";

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

    /**
     * Save player data to the table.
     */
    public void saveToPlayerTable() {
        String sql = "INSERT INTO players(id,uuid,alive,infected,turned,timeInfected) " +
                "VALUES(?,?,?,?,?,?)";

        Map<UUID, User> users = plugin.getInfectionManager().getUsers();

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
                    ps.executeUpdate();
                } catch(SQLException e) {
                    updatePlayerInTable(user);
                }
            });
        }
    }

    /**
     * Update player info to the table.
     * @param user User to update
     */
    public void updatePlayerInTable(User user) {
        String updateSQL = "UPDATE players SET uuid = ? ," +
                "alive = ? ," +
                "infected = ? ," +
                "turned = ? ," +
                "timeInfected = ? ," +
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

                ps.setString(9, user.getUuid().toString());
                ps.executeUpdate();

            } catch(SQLException e) {
                System.out.println(e.getMessage());
                plugin.getLogger().log(Level.SEVERE, "Failed to save to Player Table in Database.");
            }
        }
    }

    /**
     * Collect all the user data from the table.
     */
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
                            rs.getLong("timeInfected")
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
                            rs.getLong("timeInfected")
                    );
                    userMap.put(user.getUuid(), user);
                }
            }
            plugin.getLogger().info("Successfully collected users to hashmap.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(":thinking_emoji:");
        }

        plugin.getInfectionManager().setUsers(userMap);
    }
}
