package me.adelemphii.molynsi;

import co.aikar.commands.PaperCommandManager;
import me.adelemphii.molynsi.commands.CommandMolynsi;
import me.adelemphii.molynsi.events.PlayerJoinListener;
import me.adelemphii.molynsi.infection.InfectionManager;
import me.adelemphii.molynsi.utils.DisplayManager;
import me.adelemphii.molynsi.utils.SQLite;
import me.adelemphii.molynsi.utils.UserManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.logging.Level;

public final class Molynsi extends JavaPlugin {

    private Connection connection;
    private SQLite SQLite;

    private UserManager userManager;
    private InfectionManager infectionManager;
    private DisplayManager displayManager;

    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {

        commandManager = new PaperCommandManager(this);
        userManager = new UserManager();
        saveDefaultConfig();

        commandManager.registerCommand(new CommandMolynsi());
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

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

        infectionManager = new InfectionManager(this);
        displayManager = new DisplayManager(this);

    }

    @Override
    public void onDisable() {
        getLogger().info("Commencing save to database..");
        SQLite.saveToPlayerTable();

        if(connection != null) SQLite.disconnect();
        getLogger().info("Successfully disconnected from database.");
    }

    public DisplayManager getDisplayManager() {
        return displayManager;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public me.adelemphii.molynsi.utils.SQLite getSQLite() {
        return SQLite;
    }

    public void setSQLite(me.adelemphii.molynsi.utils.SQLite SQLite) {
        this.SQLite = SQLite;
    }

    public InfectionManager getInfectionManager() {
        return infectionManager;
    }

    public void setInfectionManager(InfectionManager infectionManager) {
        this.infectionManager = infectionManager;
    }

    public void setDisplayManager(DisplayManager displayManager) {
        this.displayManager = displayManager;
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }

    public void setCommandManager(PaperCommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
