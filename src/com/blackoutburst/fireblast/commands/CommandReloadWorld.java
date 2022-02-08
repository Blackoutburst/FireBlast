package com.blackoutburst.fireblast.commands;

import com.blackoutburst.fireblast.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class CommandReloadWorld {


    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cYou must provide the world!");
            return;
        }

        World w = Bukkit.getWorld(args[0]);
        if (w == null) {
            sender.sendMessage("§cThe world §e"+args[0]+" §cdoesn't exist!");
            return;
        }
        Utils.reloadWorld(w);
        sender.sendMessage("§bWorld §a"+w.getName()+" §breloaded successfully!");
    }
}
