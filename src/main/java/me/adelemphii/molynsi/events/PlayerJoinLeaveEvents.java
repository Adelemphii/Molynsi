package me.adelemphii.molynsi.events;

import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.utils.player.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerJoinLeaveEvents implements Listener {

    Molynsi plugin;
    public PlayerJoinLeaveEvents(Molynsi plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        Map<Integer, User> users = plugin.getUsers();
        User newUser = new User(event.getPlayer().getUniqueId().toString(), users.size() + 1,
                true, false, false, null, false, 20, 1.0f);

        AtomicBoolean match = new AtomicBoolean(false);
        users.forEach((id, user) -> {
            if(Objects.equals(user.getUuid(), event.getPlayer().getUniqueId().toString())) {
                match.set(true);
            }
        });

        if(!match.get()) {
            plugin.addUser(newUser);
            plugin.getLogger().info("Registered " + event.getPlayer().getName() + "'s user profile.");
        }
    }
}
