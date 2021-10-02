package me.adelemphii.molynsi.commands;

import me.adelemphii.molynsi.commands.interfaces.BasicCommand;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements BasicCommand {
    @Override
    public boolean run(CommandSender sender) {

        if(sender instanceof Player) {
            Player player = (Player) sender;
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "\n" +
                        "         &5&lMolynsi&r &7" +
                        "\n" +
                        "\n   &a&l+ &d/molynsi border set <size> -&f Set the border size" +
                        "\n   &a&l+ &d/molynsi border <user> <size> (CONSOLE ONLY) -&Set the border center and size based on location of a player" +
                        "\n   &a&l+ &d/molynsi border reset -&f Reset the border size"));
        sender.sendMessage("");
        return true;
    }
}