package me.adelemphii.molynsi.infection;

import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.utils.player.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class InfectionManager {

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

    public InfectionManager(Molynsi plugin) {
        this.plugin = plugin;
        this.userMap = plugin.getUsers();

        startRunnable();

        createScoreBoard();
        defineTeams();
        populateUserNames();
    }

    // TODO: This whole setup is JANK as fuck and there's a weird flickering when I join the server, pls fix
    private void createScoreBoard() {
        scoreboardManager = plugin.getServer().getScoreboardManager();
        scoreboard = scoreboardManager.getNewScoreboard();
        objective = scoreboard.registerNewObjective("InfectionManager", "dummy",
                ChatColor.translateAlternateColorCodes('&', "&5&l<< &d&lPlayer List &5&l>>"));

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score spacer = objective.getScore(ChatColor.DARK_PURPLE + "");
        spacer.setScore(5);
        Score alivePlayers = objective.getScore(ChatColor.AQUA + "Alive Players: " + alivePlayersAmount);
        alivePlayers.setScore(4);
        Score deadPlayers = objective.getScore(ChatColor.AQUA + "Dead Players: " + deadPlayersAmount);
        deadPlayers.setScore(3);
        Score infectedPlayers = objective.getScore(ChatColor.AQUA + "Turned Players: " + turnedInfected);
        infectedPlayers.setScore(2);
        Score totalPlayersScore = objective.getScore(ChatColor.AQUA + "Total Players: " + totalPlayers);
        totalPlayersScore.setScore(1);

        Bukkit.getOnlinePlayers().forEach(player -> player.setScoreboard(scoreboard));
    }

    private void defineTeams() {

    }

    private void populateUserNames() {

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
            createScoreBoard();

        }, 400, 0);
    }

}
