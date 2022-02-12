package com.blackoutburst.fireblast.utils;

import com.blackoutburst.fireblast.core.BlastPlayer;
import com.blackoutburst.fireblast.core.BlockMap;
import com.blackoutburst.fireblast.main.Main;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.PlayerConnection;
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
import org.bukkit.util.Vector;

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
            int x = file.getInt("loc."+i+".x");
            int y = file.getInt("loc."+i+".y");
            int z = file.getInt("loc."+i+".z");
            String mat = file.getString("loc."+i+".mat");
            byte data = (byte) file.getInt("loc."+i+".data");
            blocks.add(new BlockMap(x, y, z, mat, data));
        }

        for (BlockMap b : blocks) {
            Block block = w.getBlockAt(new Location(w, b.getX(), b.getY(), b.getZ()));
            block.setType(Material.getMaterial(b.getMaterial()));
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

    public static void createCircle(PlayerConnection connection, Location location) {
        for (int i = 9; i > 0; i--) {
            final double angle = 2 * Math.PI * i / 9;
            final double x = Math.cos(angle) * 0.3f;
            final double y = Math.sin(angle) * 0.3f;

            Vector v = rotateAroundAxisX(new Vector(x, y, 0), location.getPitch());
            v = rotateAroundAxisY(v, location.getYaw());

            final Location temp = location.clone().add(v);

            connection.sendPacket(new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (float) temp.getX(), (float) temp.getY(), (float) temp.getZ(), 0, 0, 0, 0, 1));
        }
    }

    private static Vector rotateAroundAxisX(Vector v, double angle) {
        angle = Math.toRadians(angle);

        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        final double y = v.getY() * cos - v.getZ() * sin;
        final double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    private static Vector rotateAroundAxisY(Vector v, double angle) {
        angle = -angle;
        angle = Math.toRadians(angle);

        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        final double x = v.getX() * cos + v.getZ() * sin;
        final double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }
}
