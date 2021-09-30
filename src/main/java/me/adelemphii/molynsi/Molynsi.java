package me.adelemphii.molynsi;

import me.adelemphii.molynsi.events.PlayerJoinLeaveEvents;
import me.adelemphii.molynsi.infection.InfectionManager;
import me.adelemphii.molynsi.utils.CommandManager;
import me.adelemphii.molynsi.utils.SQLite;
import me.adelemphii.molynsi.utils.player.User;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class Molynsi extends JavaPlugin {

    Connection connection;
    private SQLite SQLite;

    Map<Integer, User> users = new HashMap<>();

    InfectionManager infectionManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        getCommand("molynsi").setExecutor(new CommandManager());
        getServer().getPluginManager().registerEvents(new PlayerJoinLeaveEvents(this), this);

        // START SQL CONNECTION/COLLECTION
        getLogger().info("Establishing connection to SQLite Database...");
        SQLite = new SQLite(this);
        if(SQLite.connect()) {
            connection = SQLite.getConnection();
            getLogger().info("Success! Connected to Database.");
        }
        else {
            getLogger().log(Level.SEVERE, "Connection to SQLite Database failed... disabling plugin.");
            getPluginLoader().disablePlugin(this);
            return;
        }

        getLogger().info("Collecting users from database...");
        SQLite.collectAllUsersFromTable();
        // END SQL CONNECTION/COLLECTION

        infectionManager = new InfectionManager(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Commencing save to database..");
        SQLite.saveToPlayerTable();

        if(connection != null) SQLite.disconnect();
        getLogger().info("Successfully disconnected from database.");
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void setUsers(Map<Integer, User> users) {
        this.users = users;
    }
}
