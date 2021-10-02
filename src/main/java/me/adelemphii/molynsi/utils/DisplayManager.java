package me.adelemphii.molynsi.utils;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
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

    Team aliveTeam;
    Team turnedTeam;
    Team deadTeam;

    BukkitTask updateScoreboard;

    public DisplayManager(Molynsi plugin) {
        this.plugin = plugin;
        this.userMap = plugin.getUsers();

        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        if(sb.getTeam("aliveTeam") == null) aliveTeam = sb.registerNewTeam("aliveTeam");
        else aliveTeam = sb.getTeam("aliveTeam");
        if(sb.getTeam("turnedTeam") == null) turnedTeam = sb.registerNewTeam("turnedTeam");
        else turnedTeam = sb.getTeam("turnedTeam");
        if(sb.getTeam("deadTeam") == null) deadTeam = sb.registerNewTeam("deadTeam");
        else deadTeam = sb.getTeam("deadTeam");

        aliveTeam.setPrefix(ChatColor.GREEN + "");
        turnedTeam.setPrefix(ChatColor.RED + "");
        deadTeam.setPrefix(ChatColor.GRAY + "");

        startRunnable();
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

            Bukkit.getOnlinePlayers().forEach(player -> {
                this.createScoreBoard(player);
                this.updatePlayerTeam(player);
            });

        }, 0, 200);
    }

    public void createScoreBoard(Player player) {
        BPlayerBoard board = Netherboard.instance().getBoard(player);
        if(board == null){
            board = Netherboard.instance().createBoard(player,
                    ChatColor.translateAlternateColorCodes('&', "&5&l<< &d&lPlayer List &5&l>>"));
        }

        board.set(" ", 5);
        board.set(ChatColor.AQUA + "» Total Players: " + totalPlayers, 4);
        board.set(ChatColor.AQUA + "» Alive Players: " + alivePlayersAmount, 3);
        board.set(ChatColor.AQUA + "» Dead Players: " + deadPlayersAmount, 2);
        board.set(ChatColor.AQUA + "» Turned Players: " + turnedInfected, 1);



    }

    private void updatePlayerTeam(Player player) {
        plugin.getUsers().forEach((integer, user) -> {
            if(user.getUuid().equals(player.getUniqueId().toString())) {
                if(user.isTurned()) {
                    turnedTeam.addEntry(player.getName());
                    player.setPlayerListName(ChatColor.RED + player.getDisplayName());
                    player.setDisplayName(ChatColor.RED + player.getDisplayName());
                }
                else if(user.isAlive()) {
                    aliveTeam.addEntry(player.getName());
                    player.setPlayerListName(ChatColor.GREEN + player.getDisplayName());
                    player.setDisplayName(ChatColor.GREEN + player.getDisplayName());
                }
                else {
                    deadTeam.addEntry(player.getName());
                    player.setPlayerListName(ChatColor.GRAY + player.getDisplayName());
                    player.setDisplayName(ChatColor.GRAY + player.getDisplayName());
                }
            }
        });

    }
}
