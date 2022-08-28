package me.adelemphii.molynsi;

import co.aikar.commands.PaperCommandManager;
import me.adelemphii.molynsi.commands.CommandMolynsi;
import me.adelemphii.molynsi.listeners.*;
import me.adelemphii.molynsi.infection.InfectionManager;
import me.adelemphii.molynsi.utils.ConfigManager;
import me.adelemphii.molynsi.utils.DisplayManager;
import me.adelemphii.molynsi.utils.SQLite;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.logging.Level;

/**
 * Main Class of the plugin.
 */
public final class Molynsi extends JavaPlugin {

    private Connection connection;
    private SQLite SQLite;

    private InfectionManager infectionManager;
    private DisplayManager displayManager;
    private ConfigManager configManager;

    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {

        configManager = new ConfigManager(this);
        commandManager = new PaperCommandManager(this);
        infectionManager = new InfectionManager(this);
        saveDefaultConfig();

        // START SQL CONNECTION/COLLECTION
        getLogger().info("Establishing connection to SQLite Database...");
        SQLite = new SQLite(this);
        if(SQLite.connect()) {
            connection = SQLite.getConnection();
            getLogger().info("Success! Connected to Database.");
        } else {
            getLogger().log(Level.SEVERE, "Connection to SQLite Database failed... disabling plugin.");
            getPluginLoader().disablePlugin(this);
            return;
        }

        getLogger().info("Collecting users from database...");
        SQLite.collectAllUsersFromTable();
        // END SQL CONNECTION/COLLECTION

        commandManager.registerCommand(new CommandMolynsi(this));
        registerEvents();
        displayManager = new DisplayManager(this);

    }

    @Override
    public void onDisable() {
        getLogger().info("Commencing save to database..");
        SQLite.saveToPlayerTable();

        if(connection != null) SQLite.disconnect();
        getLogger().info("Successfully disconnected from database.");
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new TurnListener(this), this);
        pluginManager.registerEvents(new InfectListener(this), this);
        pluginManager.registerEvents(new PlayerDeathListener(this), this);
        pluginManager.registerEvents(new GameEndListener(this), this);
        pluginManager.registerEvents(new PlayerDamageListener(this), this);
    }

    /**
     * Get the Config Manager.
     * @return ConfigManager
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Get the Display Manager.
     * @return DisplayManager
     */
    public DisplayManager getDisplayManager() {
        return displayManager;
    }

    /**
     * Get the connection to the SQLite Database.
     * @return Connection to database
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Get the SQLite manager class.
     * @return SQLite manager class
     */
    public me.adelemphii.molynsi.utils.SQLite getSQLite() {
        return SQLite;
    }

    /**
     * Get the Infection Manager.
     * @return InfectionManager
     */
    public InfectionManager getInfectionManager() {
        return infectionManager;
    }
}
