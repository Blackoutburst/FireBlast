package com.blackoutburst.fireblast.commands;

import com.blackoutburst.fireblast.core.BlockMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandReloadWorld {

    private final List<BlockMap> blocks = new ArrayList<>();

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

        YamlConfiguration file = YamlConfiguration.loadConfiguration(new File("plugins/FireBlast/"+w.getName()+".yml"));
        Set<String> blc = file.getConfigurationSection("loc").getKeys(false);
        for (String i : blc) {
            World world = Bukkit.getWorld(file.getString("loc."+i+".world"));
            double x = file.getDouble("loc."+i+".x") + 0.5f;
            double y = file.getDouble("loc."+i+".y") + 0.5f;
            double z = file.getDouble("loc."+i+".z") + 0.5f;
            Material mat = Material.getMaterial(file.getString("loc."+i+".mat"));
            byte data = (byte) file.getInt("loc."+i+".data");
            blocks.add(new BlockMap(new Location(world, x, y, z), mat, data));
        }

        for (BlockMap b : blocks) {
            Block block = w.getBlockAt(new Location(w, b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ()));
            block.setType(b.getMaterial());
            block.setData(b.getData());
        }
        sender.sendMessage("§bWorld §a"+w.getName()+" §breloaded successfully!");
    }
}
