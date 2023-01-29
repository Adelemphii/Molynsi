package me.adelemphii.molynsi.infection;

import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.infection.events.GameEndEvent;
import me.adelemphii.molynsi.infection.events.PeaceTimeEndEvent;
import me.adelemphii.molynsi.infection.events.PlayerInfectEvent;
import me.adelemphii.molynsi.infection.events.PlayerTurnEvent;
import me.adelemphii.molynsi.utils.ChatUtility;
import me.adelemphii.molynsi.utils.player.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages the backend of the game.
 */
public class InfectionManager {

    private final Molynsi plugin;

    private BukkitTask gameScheduler;
    private long gameTimeSeconds = 0;
    private boolean gameRunning = false;
    private boolean peaceTime = true;
    private Map<UUID, User> userMap;

    public InfectionManager(Molynsi plugin) {
        this.plugin = plugin;
    }

    public boolean start(Player target) {
        if(gameRunning || (gameScheduler != null && !gameScheduler.isCancelled())) {
            return false;
        }
        gameRunning = true;
        runCountdown(plugin.getConfigManager().getCountdownTimer(), target);
        return true;
    }

    private void runCountdown(int timer, Player target) {
        AtomicInteger timerTemp = new AtomicInteger(timer);
        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            if(timerTemp.get() == 0) {
                task.cancel();
                if(!rollInfection(target)) {
                    gameRunning = false;
                    gameScheduler = null;
                    return;
                }

                for(UUID uuid : userMap.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);

                    if(player != null) {
                        Title.Times times = Title.Times.of(Duration.ofSeconds(2), Duration.ofSeconds(5), Duration.ofSeconds(3));
                        player.showTitle(Title.title(Component.text("Best of Luck :)")
                                        .color(NamedTextColor.GREEN),
                                Component.text("Survive.").color(NamedTextColor.RED), times));
                        player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 1, 1);
                    }
                }
                startGameTask();
                return;
            }
            Bukkit.broadcast(Component.text("The game will begin in... " + timerTemp.get() + " seconds.").color(NamedTextColor.GREEN));
            for(UUID uuid : userMap.keySet()) {
                Player player = Bukkit.getPlayer(uuid);

                if(player != null) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                }
            }
            timerTemp.getAndDecrement();
        }, 0L, 20L);
    }

    private boolean rollInfection(Player target) {
        if(target != null) {
            User user = userMap.get(target.getUniqueId());
            if(user != null) {
                PlayerInfectEvent playerInfectEvent = new PlayerInfectEvent(target, null, user);
                Bukkit.getPluginManager().callEvent(playerInfectEvent);

                user.setInfected(true);
                user.setTimeInfected(System.currentTimeMillis());
                userMap.put(target.getUniqueId(), user);
                return true;
            }
            return false;
        }
        if(Bukkit.getOnlinePlayers().isEmpty()) {
            Bukkit.broadcast(Component.text("Game cancelled due to all players leaving.").color(NamedTextColor.RED));
            return false;
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        List<UUID> infected = new ArrayList<>();

        int requiredAmount = userMap.size() > 10 ? 2 : 1;

        int choice = random.nextInt(userMap.size());

        User[] users = userMap.values().toArray(new User[0]);
        Player player = Bukkit.getPlayer(users[choice].getUuid());
        UUID uuid = users[choice].getUuid();

        if(player != null) {
            if(userMap.get(uuid).isInfected()) {
                infected.add(uuid);
            }
            if(infected.size() < requiredAmount) {
                if(plugin.getConfigManager().isDebug()) {
                    player.sendMessage(ChatUtility.formatDebugMessage("You have been chosen to be infected."));
                }
                PlayerInfectEvent playerInfectEvent = new PlayerInfectEvent(player, null, userMap.get(uuid));
                Bukkit.getPluginManager().callEvent(playerInfectEvent);

                infected.add(uuid);
                User user = userMap.get(uuid);
                user.setInfected(true);
                user.setTimeInfected(System.currentTimeMillis());
                userMap.put(uuid, user);
            }
        }

        if(infected.size() < requiredAmount) {
            if(plugin.getConfigManager().isDebug()) {
                Bukkit.broadcast(ChatUtility.formatDebugMessage(infected.size() + " is less than the required amount of " + requiredAmount));
            }
            return rollInfection(null);
        }
        return true;
    }

    private void startGameTask() {
        gameTimeSeconds = 0;

        gameScheduler = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            gameTimeSeconds++;

            String winReason = checkWinConditions();
            if(winReason != null) {
                GameEndEvent gameEndEvent = new GameEndEvent(userMap);
                List<User> users = new ArrayList<>();
                switch (winReason) {
                    case "INFECTED" -> {
                        Bukkit.broadcast(Component.text("All players have been turned.").color(NamedTextColor.RED)
                                .append(Component.newline())
                                .append(Component.text("The infected have won.")).color(NamedTextColor.GREEN));
                        Bukkit.getPluginManager().callEvent(gameEndEvent);
                    }
                    case "ALIVE" -> {
                        Bukkit.broadcast(Component.text("All infected have been killed.").color(NamedTextColor.GREEN)
                                .append(Component.newline())
                                .append(Component.text("The survivors have won.")).color(NamedTextColor.GREEN));
                        Bukkit.getPluginManager().callEvent(gameEndEvent);
                    }
                    case "OFFLINE" -> {
                        Bukkit.broadcast(Component.text("All players have left..").color(NamedTextColor.GREEN)
                                .append(Component.newline())
                                .append(Component.text("Nobody won.")).color(NamedTextColor.GREEN));
                        Bukkit.getPluginManager().callEvent(gameEndEvent);
                    }
                }
                gameScheduler.cancel();

                for (User user : userMap.values()) {
                    user.setDefaults();
                    users.add(user);
                }

                users.forEach(user -> userMap.put(user.getUuid(), user));
                peaceTime = true;
                gameRunning = false;
                users.clear();
                return;
            }

            if(peaceTime && gameTimeSeconds >= plugin.getConfigManager().getPeaceTimeSeconds()) {
                peaceTime = false;
                Bukkit.getPluginManager().callEvent(new PeaceTimeEndEvent());

                for(UUID uuid : userMap.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if(player != null) {
                        player.sendMessage(Component.text("Peace time has ended. Players can now turn.")
                                .color(NamedTextColor.RED)
                                .decorate(TextDecoration.BOLD)
                                .append(Component.newline())
                                .append(Component.text("PvP is now enabled.")
                                        .color(NamedTextColor.GREEN)
                                        .decorate(TextDecoration.ITALIC)));
                        player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
                    }
                }
            } else {
                List<User> turned = new ArrayList<>();
                for(User user : userMap.values()) {
                    Player player = Bukkit.getPlayer(user.getUuid());
                    if(player != null) {
                        if(!user.isTurned() && user.isInfected() && System.currentTimeMillis() - user.getTimeInfected()
                                > plugin.getConfigManager().getInfectedTurnTimeSeconds() * 1000L) {
                            PlayerTurnEvent playerTurnEvent = new PlayerTurnEvent(player, null, user);
                            Bukkit.getPluginManager().callEvent(playerTurnEvent);

                            if(!playerTurnEvent.isCancelled()) {
                                user.setTurned(true);
                                turned.add(user);
                            }
                        }
                    }
                }
                for(User user : turned) {
                    userMap.put(user.getUuid(), user);
                }
            }

        }, 0L, 20L);
    }

    private String checkWinConditions() {
        boolean allOffline = true;
        for(Player player : Bukkit.getOnlinePlayers()) {
            User user = userMap.get(player.getUniqueId());
            if(user != null) {
                allOffline = false;
            }
        }

        if(allOffline) {
            return "OFFLINE";
        }

        boolean allInfected = true;
        boolean allAlive = true;
        for(User user : userMap.values()) {
            if(!user.isInfected() && user.isAlive()) {
                allInfected = false;
            }
            if(user.isAlive() && user.isInfected()) {
                allAlive = false;
            }
        }
        if(allInfected) {
            return "INFECTED";
        }
        if(allAlive) {
            return "ALIVE";
        }
        return null;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    /**
     * Get a map of all the participating users
     * @return Map of all users
     */
    public Map<UUID, User> getUsers() {
        return userMap;
    }

    /**
     * Add a user to the participant map.
     * @param user User to add
     */
    public void addUser(User user) {
        userMap.put(user.getUuid(), user);
    }

    /**
     * Set the participant map
     * @param users Map to set
     */
    public void setUsers(Map<UUID, User> users) {
        this.userMap = users;
    }

    /**
     * Get the length of the game in seconds.
     * @return Length of game in seconds.
     */
    public long getGameTimeSeconds() {
        return gameTimeSeconds;
    }

    /**
     * Get if the game is in Peace Time
     * @return Whether the game is in Peace Time
     */
    public boolean isPeaceTime() {
        return peaceTime;
    }
}
