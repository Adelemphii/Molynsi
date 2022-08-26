package me.adelemphii.molynsi.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import me.adelemphii.molynsi.utils.WorldBorderUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("mol|molynsi")
public class CommandMolynsi extends BaseCommand {

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
                /* border reset */
                .append(Component.text("+").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD))
                .append(Component.text(" /molynsi border reset <world> ").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text("- Reset the border in a world").color(NamedTextColor.WHITE))

                .append(Component.newline())
        );
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
