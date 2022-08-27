package me.adelemphii.molynsi.listeners;

import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.utils.player.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageListener implements Listener {

    private final Molynsi plugin;

    public PlayerDamageListener(Molynsi plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player victim)) {
            return;
        }
        if(!(event.getDamager() instanceof Player damager)) {
            return;
        }

        User victimUser = plugin.getInfectionManager().getUsers().get(victim.getUniqueId());
        User damagerUser = plugin.getInfectionManager().getUsers().get(damager.getUniqueId());
        if(victimUser == null || damagerUser == null) {
            event.setCancelled(true);
            return;
        }
        boolean peaceTime = plugin.getInfectionManager().isPeaceTime();
        if(peaceTime) {
            event.setCancelled(true);
        }
    }
}
