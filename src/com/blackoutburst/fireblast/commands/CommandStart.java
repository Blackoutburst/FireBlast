package com.blackoutburst.fireblast.commands;

import com.blackoutburst.fireblast.core.Core;
import com.blackoutburst.fireblast.main.Main;
import com.blackoutburst.fireblast.utils.Utils;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.Random;

public class CommandStart {

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cYou must specifie the world name!");
            return;

        }

        if (Main.gameRunning) {
            sender.sendMessage("§cYou must end the current game with §e/end§c before starting a new one!");
            return;
        }

        String finalWorldName = Utils.getWorldName(args);

        if (finalWorldName == null) {
            sender.sendMessage("§cThis world doesn't exist!");
            return;
        }

        Core.startGame(finalWorldName);
    }
}
