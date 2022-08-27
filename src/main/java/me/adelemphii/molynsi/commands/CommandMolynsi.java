package me.adelemphii.molynsi.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.utils.WorldBorderUtils;
import me.adelemphii.molynsi.utils.player.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@CommandAlias("mol|molynsi")
public class CommandMolynsi extends BaseCommand {

    private final Molynsi plugin;
    public CommandMolynsi(Molynsi plugin) {
        this.plugin = plugin;
    }

    @HelpCommand
    @CommandPermission("molynsi.use")
    public void onHelp(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
        player.sendMessage(Component.newline()
                .append(Component.text("      Molynsi").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.BOLD))
                .append(Component.newline())
                /* border set */
                .append(Component.text("+").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD))
                .append(Component.text(" /molynsi border set <player> <size> ").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text("- Set the border size based on the location of a player").color(NamedTextColor.WHITE))
                .append(Component.newline())
                /* border reset */
                .append(Component.text("+").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD))
                .append(Component.text(" /molynsi border reset <world> ").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text("- Reset the border in a world").color(NamedTextColor.WHITE))
                .append(Component.newline())

                /* start */
                .append(Component.text("+").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD))
                .append(Component.text(" /molynsi start ").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text("- Start the game").color(NamedTextColor.WHITE))
                .append(Component.newline())

                .append(Component.newline())
        );
    }

    @Subcommand("start")
    @CommandPermission("molynsi.admin")
    public void onStart(Player player) {
        boolean start = plugin.getInfectionManager().start(null);
        if(start) {
            player.sendMessage(Component.text("Game Started.").color(NamedTextColor.GREEN));
        }
    }

    @Subcommand("start dev")
    @CommandPermission("molynsi.admin")
    public void onDevStart(Player player, Player target) {
        boolean start = plugin.getInfectionManager().start(target);
        if(start) {
            player.sendMessage(Component.text("Game Started.").color(NamedTextColor.GREEN));
        }
    }

    @Subcommand("info")
    @CommandPermission("molynsi.admin")
    public void onInfo(Player player) {
        for(User user : plugin.getInfectionManager().getUsers().values()) {
            player.sendMessage(Component.text(user.toString()));
        }
    }

    @Subcommand("border set")
    @CommandPermission("molynsi.admin")
    public void setBorder(Player player, Player targetPlayer, int size) {
        WorldBorderUtils.setBorder(targetPlayer, size);

        for(Player p : targetPlayer.getWorld().getPlayers()) {
            p.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 1);
        }
        Bukkit.broadcast(player.displayName().color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD)
                .append(Component.text(" has set the border size to ")
                        .color(NamedTextColor.RED)
                        .decorate(TextDecoration.ITALIC))
                .append(Component.text(size)
                        .color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD)
                ));
    }

    @Subcommand("border reset")
    @CommandPermission("molynsi.admin")
    public void resetBorder(Player player, World world) {
        world.getWorldBorder().reset();

        for(Player p : world.getPlayers()) {
            p.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 1);
        }
        Bukkit.broadcast(player.displayName()
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD)
                        .append(Component.text(" has reset the border!")
                                .color(NamedTextColor.RED)
                                .decorate(TextDecoration.ITALIC)));
    }

}
