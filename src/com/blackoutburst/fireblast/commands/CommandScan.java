package com.blackoutburst.fireblast.commands;

import com.blackoutburst.fireblast.core.BlockMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandScan {

    private final List<BlockMap> blocks = new ArrayList<>();

    private void saveLocation(String worldName) {
        YamlConfiguration file = YamlConfiguration.loadConfiguration(new File("plugins/FireBlast/"+worldName+".yml"));

        for (int i = 0; i < blocks.size(); i++) {
            file.set("loc."+i+".world", worldName);
            file.set("loc."+i+".x", blocks.get(i).getLocation().getBlockX());
            file.set("loc."+i+".y", blocks.get(i).getLocation().getBlockY());
            file.set("loc."+i+".z", blocks.get(i).getLocation().getBlockZ());
            file.set("loc."+i+".mat", blocks.get(i).getMaterial().toString());
            file.set("loc."+i+".data", blocks.get(i).getData());
        }
        try {
            file.save(new File("plugins/FireBlast/"+worldName+".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length < 6) {
            sender.sendMessage("§cInvalid usage try §e/scan x1 y1 z1 x2 y2 z2");
            return;
        }

        int x1 = Integer.min(Integer.parseInt(args[0]), Integer.parseInt(args[3]));
        int y1 = Integer.min(Integer.parseInt(args[1]), Integer.parseInt(args[4]));
        int z1 = Integer.min(Integer.parseInt(args[2]), Integer.parseInt(args[5]));

        int x2 = Integer.max(Integer.parseInt(args[0]), Integer.parseInt(args[3]));
        int y2 = Integer.max(Integer.parseInt(args[1]), Integer.parseInt(args[4]));
        int z2 = Integer.max(Integer.parseInt(args[2]), Integer.parseInt(args[5]));

        Player player = (Player) sender;
        World world = player.getWorld();

        sender.sendMessage("§bStarting scan");

        File tmp = new File("plugins/FireBlast/"+world.getName()+".yml");
        if (tmp.exists()) tmp.delete();
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    Block b = world.getBlockAt(new Location(world, x, y, z));
                    blocks.add(new BlockMap(new Location(world, x, y, z), b.getType(), b.getData()));
                }
            }
        }
        saveLocation(world.getName());
        sender.sendMessage("§bScan complete, §a"+ world.getName()+" §bsaved");
    }

}
