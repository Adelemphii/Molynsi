package me.adelemphii.molynsi.listeners;

import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.utils.player.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Map;
import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final Molynsi plugin;
    public PlayerJoinListener(Molynsi plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        Map<UUID, User> users = plugin.getInfectionManager().getUsers();
        User newUser = new User(event.getPlayer().getUniqueId(),
                true, false, false, null);

        boolean match = false;
        for(User user : users.values()) {
            if(user.getUuid() == event.getPlayer().getUniqueId()) {
                match = true;
                break;
            }
        }

        if(!match) {
            plugin.getInfectionManager().addUser(newUser);
            plugin.getLogger().info("Registered " + event.getPlayer().getName() + "'s user profile.");
        }
    }
}
