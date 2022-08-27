package me.adelemphii.molynsi.listeners;

import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.infection.events.PlayerTurnEvent;
import me.adelemphii.molynsi.infection.events.UserDeathEvent;
import me.adelemphii.molynsi.utils.ChatUtility;
import me.adelemphii.molynsi.utils.player.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerDeathListener implements Listener {

    private final Molynsi plugin;

    public PlayerDeathListener(Molynsi plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(event.isCancelled()) {
            return;
        }
        Player player = event.getEntity();
        User user = plugin.getInfectionManager().getUsers().get(player.getUniqueId());

        if(user == null) {
            return;
        }

        if(user.isAlive() && user.isInfected()) {
            UserDeathEvent userDeathEvent = new UserDeathEvent(player, null, user);
            Bukkit.getPluginManager().callEvent(userDeathEvent);

            if(!userDeathEvent.isCancelled()) {
                user.setAlive(false);
                user.setTurned(false);
                user.setInfected(false);
                plugin.getInfectionManager().addUser(user);

                player.setGameMode(GameMode.SPECTATOR);

                player.showTitle(Title.title(Component.text("You have died.").color(NamedTextColor.RED),
                        Component.text("RIP.").color(NamedTextColor.RED)));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeathByEntity(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(player.getHealth() > event.getFinalDamage()) {
                return;
            }

            User user = plugin.getInfectionManager().getUsers().get(player.getUniqueId());
            if(user == null) {
                return;
            }

            if(event.getDamager() instanceof Player damager) {
                User damagerUser = plugin.getInfectionManager().getUsers().get(damager.getUniqueId());
                if(damagerUser == null) {
                    return;
                }

                if(damagerUser.isTurned()) {
                    PlayerTurnEvent playerTurnEvent = new PlayerTurnEvent(player, damager, user);
                    Bukkit.getPluginManager().callEvent(playerTurnEvent);

                    if(!playerTurnEvent.isCancelled()) {
                        user.setInfected(true);
                        user.setTurned(true);
                        event.setCancelled(true);

                        player.setHealth(1);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5, 1));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 7, 1));
                    }

                    plugin.getInfectionManager().addUser(user);
                    return;
                }

                if(user.isAlive()) {
                    UserDeathEvent userDeathEvent = new UserDeathEvent(player, damager, user);
                    Bukkit.getPluginManager().callEvent(userDeathEvent);

                    if(!userDeathEvent.isCancelled()) {
                        user.setAlive(false);
                        user.setTurned(false);
                        user.setInfected(false);
                        plugin.getInfectionManager().addUser(user);

                        player.setGameMode(GameMode.SPECTATOR);
                        player.showTitle(Title.title(Component.text("You have died.").color(NamedTextColor.RED),
                                Component.text("RIP.").color(NamedTextColor.RED)));

                        for(ItemStack item : player.getInventory().getContents()) {
                            if(item == null || item.getType() == Material.AIR) {
                                continue;
                            }
                            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
                        }
                        for(ItemStack item : player.getInventory().getArmorContents()) {
                            if(item == null) {
                                continue;
                            }
                            if(item.getType() != Material.AIR) {
                                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
                            }
                        }

                        player.getInventory().clear();
                        ExperienceOrb orb = (ExperienceOrb) player.getWorld().spawnEntity(player.getLocation(),
                                EntityType.EXPERIENCE_ORB);
                        orb.setExperience(player.getTotalExperience());

                        if(plugin.getConfigManager().isDebug()) {
                            player.sendMessage(ChatUtility.formatDebugMessage("You were killed by " + damager.getName()));
                        }
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
