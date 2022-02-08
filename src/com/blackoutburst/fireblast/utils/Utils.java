package com.blackoutburst.fireblast.utils;

import com.blackoutburst.fireblast.core.BlastPlayer;
import com.blackoutburst.fireblast.core.BlockMap;
import com.blackoutburst.fireblast.main.Main;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Utils {

    public static void reloadWorld(World w) {
        final List<BlockMap> blocks = new ArrayList<>();

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
    }

    public static int getNumberOfPlayerAlive() {
        int aliveCount = 0;

        for (BlastPlayer bp : Main.players) {
            if (bp.isAlive()) aliveCount++;
        }

        return aliveCount;
    }

    public static void giveItem(Player p) {
        ItemStack menu = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta menuMeta = menu.getItemMeta();

        p.getInventory().clear();
        menuMeta.setDisplayName("§6Fire Wand");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§eLeft click to shoot fireball");
        menuMeta.setLore(lore);
        menu.setItemMeta(menuMeta);
        p.getInventory().setItem(0, menu);
    }

    public static String centerText(String text) {
        int maxWidth = 60;
        int spaces = (int) Math.round((maxWidth - 1.4 * ChatColor.stripColor(text).length()) / 2);

        return StringUtils.repeat(" ", spaces) + text;
    }
}
