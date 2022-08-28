package me.adelemphii.molynsi.listeners;

import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.infection.events.GameEndEvent;
import me.adelemphii.molynsi.infection.events.PlayerTurnEvent;
import me.adelemphii.molynsi.infection.events.UserDeathEvent;
import me.adelemphii.molynsi.utils.ChatUtility;
import me.adelemphii.molynsi.utils.player.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TurnListener implements Listener {

    private final Molynsi plugin;
    private final Map<UUID, BukkitTask> runningMessages = new HashMap<>();

    public TurnListener(Molynsi plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTurn(PlayerTurnEvent event) {
        List<String> messages = plugin.getConfigManager().getTurnMessages();

        if(plugin.getConfigManager().isDebug()) {
            event.getPlayer().sendMessage(ChatUtility.formatDebugMessage("You have turned into an undead."));
        }

        AtomicInteger messageCount = new AtomicInteger(0);
        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            runningMessages.put(event.getPlayer().getUniqueId(), task);
            if(messageCount.get() >= messages.size()) {
                task.cancel();
                event.getPlayer().playSound(event.getPlayer().getLocation(),
                        Sound.ENTITY_HUSK_CONVERTED_TO_ZOMBIE, 1, 1);
                event.getPlayer().sendMessage(Component.text("You have been ").color(NamedTextColor.GRAY)
                        .append(Component.text("turned")
                                .color(NamedTextColor.RED).decorate(TextDecoration.BOLD))
                        .append(Component.text(" into an undead.").
                                color(NamedTextColor.GRAY))
                        .append(Component.newline())
                        .append(Component.text("Attack players on sight. Eat their flesh. Turn them to your side.")
                                .color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC))
                        .append(Component.newline())
                        .append(Component.text("If you don't, you may grow feeble and weak..")
                                .color(NamedTextColor.RED).decorate(TextDecoration.BOLD))
                );
                return;
            }
            event.getPlayer().sendMessage(Component.text(messages.get(messageCount.get()))
                    .color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC));
            messageCount.getAndIncrement();
        }, 0, 20L * 2L);
    }

    @EventHandler
    public void onGameEnd(GameEndEvent event) {
        for(BukkitTask task : runningMessages.values()) {
            task.cancel();
        }
        runningMessages.clear();
    }

    @EventHandler
    public void onUserDeath(UserDeathEvent event) {
        BukkitTask task = runningMessages.remove(event.getPlayer().getUniqueId());
        if(task != null) {
            task.cancel();
        }
    }

}
