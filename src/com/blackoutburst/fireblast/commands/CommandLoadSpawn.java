package com.blackoutburst.fireblast.commands;

import com.blackoutburst.fireblast.main.Main;
import com.blackoutburst.fireblast.utils.Utils;
import org.bukkit.command.CommandSender;

import java.io.File;

public class CommandLoadSpawn {

	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("§cInvalid usage try §e/loadspawn <worldName>");
			return;
		}
		
		String finalWorldName = Utils.getWorldName(args);

		if (finalWorldName == null) {
			sender.sendMessage("§cThis world doesn't exist!");
			return;
		}
		
		Utils.loadRespawn(finalWorldName);
		sender.sendMessage("§bLoaded the §6"+ Main.respawns.size()+" §bspawnpoints found in §6"+finalWorldName);
	}
}
