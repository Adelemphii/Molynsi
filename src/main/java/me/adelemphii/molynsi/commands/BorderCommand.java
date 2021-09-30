package me.adelemphii.molynsi.commands;

import me.adelemphii.molynsi.commands.interfaces.AdvancedCommand;
import me.adelemphii.molynsi.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldBorder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class BorderCommand implements AdvancedCommand {

    WorldBorder worldBorder;

    @Override
    public boolean run(CommandSender sender, String[] args) {

        if(!(sender instanceof Player)) return runConsoleCommand(sender, args);

        // molysni border set <size>
        if(args[1].equalsIgnoreCase("set")) return setBorder(sender, args);
        // molysni border reset
        else if(args[1].equalsIgnoreCase("reset")) return resetBorder(sender);

        return true;
    }

    @Override
    public void runSubHelp(CommandSender sender) {
        // TODO: make a mini help command here
    }

    private boolean runConsoleCommand(CommandSender sender, String[] args) {
        if(args[1].equalsIgnoreCase("reset")) return resetBorder(sender);
        return setBorder(sender, args);
    }

    private boolean setBorder(CommandSender sender, String[] args) {
        if(args.length < 3) return false;

        if (!(sender instanceof Player)) {
            // molysni border <name>
            if (Bukkit.getPlayer(args[1]) == null) return false;

            Player player = Bukkit.getPlayer(args[1]);
            assert player != null;
            worldBorder = player.getWorld().getWorldBorder();

            worldBorder.setCenter(player.getLocation());

            double size = 100;
            try {
                size = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                Bukkit.getLogger().info(args[2] + " is not a valid number.");
            }

            worldBorder.setSize(size);

            String center = player.getLocation().getBlockX() + ", " + player.getLocation().getBlockZ();

            double divSize = size / 2f;

            Bukkit.broadcastMessage(ChatColor.DARK_RED + "[!] " + ChatColor.RED + ChatColor.ITALIC +
                    ChatColor.translateAlternateColorCodes('&',
                            "&f&o" + "CONSOLE" + "&c&o has set the world border's center to &f&o" + center +
                                    " &c&owith a size of &f&o" + divSize));
        } else {

            Player player = (Player) sender;
            worldBorder = player.getWorld().getWorldBorder();
            worldBorder.setCenter(player.getLocation());

            double size = 100;
            try {
                size = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                ChatUtils.errorMessage(player, args[2] + " is not a valid number.");
            }

            worldBorder.setSize(size);
            String center = player.getLocation().getBlockX() + ", " + player.getLocation().getBlockZ();
            double divSize = size / 2f;

            player.sendMessage(ChatColor.GREEN + "Border center: " + center + ", \nSize: " + size);

            Bukkit.broadcastMessage(ChatColor.DARK_RED + "[!] " + ChatColor.RED + ChatColor.ITALIC +
                    ChatColor.translateAlternateColorCodes('&',
                    "&f&o" + player.getDisplayName() + "&c&o has set the world border's center to &f&o" + center +
                            " &c&owith a size of &f&o" + divSize));
        }
        return true;
    }

    private boolean resetBorder(CommandSender sender) {

        worldBorder = Objects.requireNonNull(Bukkit.getWorld("world")).getWorldBorder();
        worldBorder.setCenter(0, 0);
        worldBorder.setSize(1000000000);
        sender.sendMessage(ChatColor.GREEN + "Border reset!");
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "[!] " + ChatColor.RED + ChatColor.ITALIC +
                sender.getName() + " has reset the world border!");
        return true;
    }
}
