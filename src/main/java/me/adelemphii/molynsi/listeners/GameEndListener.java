package me.adelemphii.molynsi.listeners;

import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.infection.events.GameEndEvent;
import me.adelemphii.molynsi.utils.player.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class GameEndListener implements Listener {

    private final Molynsi plugin;
    public GameEndListener(Molynsi plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGameEnd(GameEndEvent event) {
        Map<UUID, User> userMap = event.getParticipants();

        for(User user : userMap.values()) {
            Player player = Bukkit.getPlayer(user.getUuid());
            if(player != null) {
                if(player.getGameMode() == GameMode.SPECTATOR) {
                    player.setGameMode(GameMode.SURVIVAL);
                }
                player.getInventory().clear();
                player.setTotalExperience(0);
            }
        }

        AtomicInteger countdown = new AtomicInteger(5);
        Bukkit.getScheduler().runTaskLater(plugin, task -> {
            if(countdown.get() == 0) {
                for(User user : userMap.values()) {
                    Player player = Bukkit.getPlayer(user.getUuid());
                    if(player!= null) {
                        player.sendMessage(Component.text("You have been teleported to spawn.").color(NamedTextColor.AQUA));
                        player.teleport(player.getWorld().getSpawnLocation());
                    }
                }
            }
            countdown.getAndDecrement();
        }, 20L);
    }
}
