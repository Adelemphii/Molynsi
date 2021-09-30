package me.adelemphii.molynsi.utils;

import me.adelemphii.molynsi.commands.BorderCommand;
import me.adelemphii.molynsi.commands.HelpCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {

    String permissionPrefix = "molysni.";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(cmd.getName().equalsIgnoreCase("molynsi")) {
            // /molysni - the help-information command
            if(args.length < 1) {
                return new HelpCommand().run(sender);
            }

            if(args[0].equalsIgnoreCase("border")) {
                if(args.length < 2) return false;
                if (hasPermission(sender, permissionPrefix + "setborder")) {
                    return new BorderCommand().run(sender, args);
                }
            } else {
                return new HelpCommand().run(sender);
            }
        }

        return false;
    }

    private boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission(permission);
    }

}
