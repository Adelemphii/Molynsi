package me.adelemphii.molynsi.events;

import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.utils.player.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerJoinListener implements Listener {

    Molynsi plugin;
    public PlayerJoinListener(Molynsi plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        Map<UUID, User> users = plugin.getUserManager().getUsers();
        User newUser = new User(event.getPlayer().getUniqueId(),
                true, false, false, null, false, 20, 1.0f);

        boolean match = false;
        for(User user : users.values()) {
            if(user.getUuid() == event.getPlayer().getUniqueId()) {
                match = true;
                break;
            }
        }

        if(!match) {
            plugin.getUserManager().addUser(newUser);
            plugin.getLogger().info("Registered " + event.getPlayer().getName() + "'s user profile.");
        }
    }
}
