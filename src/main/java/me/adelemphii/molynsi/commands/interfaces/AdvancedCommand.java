package me.adelemphii.molynsi.commands.interfaces;

import org.bukkit.command.CommandSender;

public interface AdvancedCommand {

    boolean run(CommandSender sender, String[] args);

    void runSubHelp(CommandSender sender);
}
