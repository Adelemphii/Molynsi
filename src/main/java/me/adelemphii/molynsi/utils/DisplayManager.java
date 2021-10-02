package me.adelemphii.molynsi.utils;

import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.utils.player.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class DisplayManager {

    Molynsi plugin;

    Map<Integer, User> userMap;
    int alivePlayersAmount;
    int deadPlayersAmount;
    int turnedInfected;
    int totalPlayers;


    ScoreboardManager scoreboardManager;
    Scoreboard scoreboard;
    Objective objective;
    BukkitTask updateScoreboard;

    public DisplayManager(Molynsi plugin) {
        this.plugin = plugin;
        this.userMap = plugin.getUsers();

        startRunnable();
    }

    // TODO: This whole setup is JANK as fuck and there's a weird flickering when I join the server, pls fix
    public void createScoreBoard(Player player) {
        scoreboardManager = plugin.getServer().getScoreboardManager();
        assert scoreboardManager != null;
        scoreboard = scoreboardManager.getNewScoreboard();
        objective = scoreboard.registerNewObjective("InfectionManager", "dummy",
                ChatColor.translateAlternateColorCodes('&', "&5&l<< &d&lPlayer List &5&l>>"));

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score totalPlayersDisplay = objective.getScore(ChatColor.AQUA + "» Total Players");
        totalPlayersDisplay.setScore(15);

        Team totalPlayersTeam = scoreboard.registerNewTeam("totalPlayers");
        totalPlayersTeam.addEntry(ChatColor.BLACK + "" + ChatColor.WHITE);
        if(totalPlayers == 0) totalPlayersTeam.setPrefix(ChatColor.AQUA + "0/");
        else totalPlayersTeam.setPrefix(ChatColor.AQUA + "" + totalPlayers);

        objective.getScore(ChatColor.BLACK + "" + ChatColor.WHITE).setScore(14);

        player.setScoreboard(scoreboard);
    }

    private void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        // TODO: its null idk man i dont care anymore do this later ig
        scoreboard.getTeam("totalPlayers").setPrefix(ChatColor.AQUA + "" + totalPlayers);
    }

    private void startRunnable() {
        updateScoreboard = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            AtomicInteger tempAliveCount = new AtomicInteger();
            AtomicInteger tempDeadCount = new AtomicInteger();
            AtomicInteger tempTurnedCount = new AtomicInteger();
            AtomicInteger tempTotalCount = new AtomicInteger();
            userMap.forEach((integer, user) -> {
                if(Bukkit.getPlayer(UUID.fromString(user.getUuid())) != null) {
                    tempTotalCount.getAndIncrement();

                    if (user.isAlive()) tempAliveCount.getAndIncrement();
                    else if (!user.isAlive() && !user.isTurned()) tempDeadCount.getAndIncrement();

                    if (user.isTurned()) tempTurnedCount.getAndIncrement();
                }
            });
            alivePlayersAmount = tempAliveCount.get();
            deadPlayersAmount = tempDeadCount.get();
            turnedInfected = tempTurnedCount.get();
            totalPlayers = tempTotalCount.get();

            Bukkit.getOnlinePlayers().forEach(this::updateScoreboard);

        }, 0, 400);
    }
}
