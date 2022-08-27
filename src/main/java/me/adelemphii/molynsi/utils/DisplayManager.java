package me.adelemphii.molynsi.utils;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.utils.player.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class DisplayManager {

    private final Molynsi plugin;

    private final Map<UUID, User> userMap;
    private int alivePlayersAmount;
    private int deadPlayersAmount;
    private int turnedInfected;
    private int totalPlayers;

    private final Team aliveTeam;
    private final Team turnedTeam;
    private final Team deadTeam;

    BukkitTask updateScoreboard;

    public DisplayManager(Molynsi plugin) {
        this.plugin = plugin;
        this.userMap = plugin.getInfectionManager().getUsers();

        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        aliveTeam = sb.getTeam("aliveTeam") != null
                ? sb.getTeam("aliveTeam") : sb.registerNewTeam("aliveTeam");
        turnedTeam = sb.getTeam("turnedTeam") != null
                ? sb.getTeam("turnedTeam") : sb.registerNewTeam("turnedTeam");
        deadTeam = sb.getTeam("deadTeam") != null
                ? sb.getTeam("deadTeam") : sb.registerNewTeam("deadTeam");

        assert aliveTeam != null;
        aliveTeam.prefix(Component.empty().color(NamedTextColor.GREEN));
        assert turnedTeam != null;
        turnedTeam.prefix(Component.empty().color(NamedTextColor.RED));
        assert deadTeam != null;
        deadTeam.prefix(Component.empty().color(NamedTextColor.GRAY));

        startRunnable();
    }

    private void startRunnable() {
        updateScoreboard = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            AtomicInteger tempAliveCount = new AtomicInteger();
            AtomicInteger tempDeadCount = new AtomicInteger();
            AtomicInteger tempTurnedCount = new AtomicInteger();
            AtomicInteger tempTotalCount = new AtomicInteger();
            userMap.forEach((integer, user) -> {
                if(Bukkit.getPlayer(user.getUuid()) != null) {
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

        }, 0, 20L * 5L);
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

    public void updatePlayerTeam(Player player) {
        plugin.getInfectionManager().getUsers().forEach((integer, user) -> {
            if(user.getUuid().equals(player.getUniqueId())) {
                if(user.isTurned()) {
                    turnedTeam.addEntry(player.getName());
                    player.playerListName(player.displayName().color(NamedTextColor.RED));
                    player.displayName(player.displayName().color(NamedTextColor.RED));
                } else if(user.isAlive()) {
                    aliveTeam.addEntry(player.getName());
                    player.playerListName(player.displayName().color(NamedTextColor.GREEN));
                    player.displayName(player.displayName().color(NamedTextColor.GREEN));
                } else {
                    deadTeam.addEntry(player.getName());
                    player.playerListName(player.displayName().color(NamedTextColor.GRAY));
                    player.displayName(player.displayName().color(NamedTextColor.GRAY));
                }
            }
        });

    }
}
