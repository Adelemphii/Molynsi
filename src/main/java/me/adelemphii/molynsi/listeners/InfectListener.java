package me.adelemphii.molynsi.listeners;

import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.infection.events.GameEndEvent;
import me.adelemphii.molynsi.infection.events.InfectionPrePulseEvent;
import me.adelemphii.molynsi.infection.events.PlayerInfectEvent;
import me.adelemphii.molynsi.infection.events.UserDeathEvent;
import me.adelemphii.molynsi.utils.ChatUtility;
import me.adelemphii.molynsi.utils.player.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class InfectListener implements Listener {

    private final HashMap<UUID, BukkitTask> infectSpreadTasks = new HashMap<>();
    private final Molynsi plugin;
    public InfectListener(Molynsi plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInfect(PlayerInfectEvent event) {
        Player player = event.getPlayer();

        if(plugin.getConfigManager().isDebug()) {
            if(event.getCause() != null) {
                player.sendMessage(ChatUtility.formatDebugMessage("You have been infected by " + event.getCause().getName()));
            } else {
                player.sendMessage(ChatUtility.formatDebugMessage("You have been infected by the SYSTEM"));
            }
        }

        if(infectSpreadTasks.containsKey(event.getPlayer().getUniqueId())) {
            return;
        }
        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            infectSpreadTasks.put(player.getUniqueId(), task);
            ThreadLocalRandom random = ThreadLocalRandom.current();

            User user = event.getUser();
            if(!user.isAlive()) {
                task.cancel();
                infectSpreadTasks.remove(player.getUniqueId());
                return;
            }

            List<Entity> entities = player.getNearbyEntities(5, 3, 5);
            for(Entity entity : entities) {
                if(entity instanceof Player nearby) {
                    User nearbyUser = plugin.getInfectionManager().getUsers().get(nearby.getUniqueId());
                    if(nearbyUser == null) {
                        return;
                    }

                    if(nearbyUser.isAlive()) {
                        if(nearbyUser.isInfected()) {
                            return;
                        }
                        int chance = random.nextInt(1, 101);
                        int needed = plugin.getConfigManager().getInfectChance();

                        if(plugin.getInfectionManager().isPeaceTime()) {
                            needed = plugin.getConfigManager().getInfectChanceDuringPeace();
                        }

                        InfectionPrePulseEvent infectionPrePulseEvent = new InfectionPrePulseEvent(nearby, player, nearbyUser,
                                chance, needed);
                        Bukkit.getPluginManager().callEvent(infectionPrePulseEvent);

                        if(!infectionPrePulseEvent.isCancelled()) {
                            chance = infectionPrePulseEvent.getChance();
                            needed = infectionPrePulseEvent.getNeeded();

                            if(plugin.getConfigManager().isDebug()) {
                                player.sendMessage(ChatUtility.formatDebugMessage("[INFP] Chance: " + chance));
                                player.sendMessage(ChatUtility.formatDebugMessage("[INFP] Needed: " + needed));
                            }

                            if(chance <= needed) {
                                if(plugin.getConfigManager().isDebug()) {
                                    player.sendMessage(ChatUtility.formatDebugMessage("You have infected " + nearby.getName()));
                                }

                                PlayerInfectEvent playerInfectEvent = new PlayerInfectEvent(nearby, player, nearbyUser);
                                Bukkit.getPluginManager().callEvent(playerInfectEvent);
                                if(!playerInfectEvent.isCancelled()) {
                                    nearbyUser.setInfected(true);
                                    nearbyUser.setTimeInfected(System.currentTimeMillis());
                                    plugin.getInfectionManager().addUser(nearbyUser);
                                }
                            }
                        }
                    }
                }
            }
        }, 0L, 20L * 5L);
    }

    @EventHandler
    public void onGameEnd(GameEndEvent event) {
        for(BukkitTask task : infectSpreadTasks.values()) {
            task.cancel();
        }
        infectSpreadTasks.clear();
    }

    @EventHandler
    public void onUserDeath(UserDeathEvent event) {
        BukkitTask task = infectSpreadTasks.remove(event.getUser().getUuid());
        if(task != null) {
            task.cancel();
        }
    }
}
