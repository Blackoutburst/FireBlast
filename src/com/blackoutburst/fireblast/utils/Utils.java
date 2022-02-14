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
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
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

    public static void giveItem(Player p, boolean onCooldown) {
        ItemStack menu = new ItemStack(onCooldown ? Material.STICK : Material.BLAZE_ROD, 1);
        ItemMeta menuMeta = menu.getItemMeta();

        p.getInventory().clear();
        menuMeta.setDisplayName("§6Fire Wand");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§eLeft click to shoot fireball");
        menuMeta.setLore(lore);
        menu.setItemMeta(menuMeta);
        p.getInventory().setItem(0, menu);
    }

    public static float map(float value, float min1, float max1, float min2, float max2) {
        return min2 + (value - min1) * (max2 - min2) / (max1 - min1);
    }

    public static String centerText(String text) {
        int maxWidth = 60;
        int spaces = (int) Math.round((maxWidth - 1.4 * ChatColor.stripColor(text).length()) / 2);

        return StringUtils.repeat(" ", spaces) + text;
    }

    public static void loadRespawn(String worldName) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("./plugins/FireBlast/"+worldName+"_spawn.yml"));

        Main.respawns.clear();
        Set<String> respawns = config.getConfigurationSection("loc").getKeys(false);

        for (String i : respawns) {
            String world = config.getString("loc."+i+".world");
            double x = config.getDouble("loc."+i+".x") + 0.5f;
            double y = config.getDouble("loc."+i+".y") + 0.5f;
            double z = config.getDouble("loc."+i+".z") + 0.5f;
            double yaw = config.getDouble("loc."+i+".yaw", 0);
            double pitch = config.getDouble("loc."+i+".pitch", 0);
            Main.respawns.add(new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch));
        }
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

    public static boolean isWand(PlayerInventory inv) {
        ItemStack item = inv.getItemInHand();

        return(item.getType() == Material.BLAZE_ROD &&
                item.getItemMeta().getDisplayName().equals("§6Spawn wand"));
    }

    public static void saveSpawns(String worldName) {
        File f = new File("plugins/FireBlast/"+worldName+"_spawn.yml");
        if (f.exists()) f.delete();

        YamlConfiguration file = YamlConfiguration.loadConfiguration(f);

        for (int i = 0; i < Main.respawns.size(); i++) {
            file.set("loc."+i+".world", worldName);
            file.set("loc."+i+".x", Main.respawns.get(i).getBlockX());
            file.set("loc."+i+".y", Main.respawns.get(i).getBlockY());
            file.set("loc."+i+".z", Main.respawns.get(i).getBlockZ());
            file.set("loc."+i+".yaw", Main.respawns.get(i).getYaw());
            file.set("loc."+i+".pitch", Main.respawns.get(i).getPitch());
        }
        try {
            file.save(new File("plugins/FireBlast/"+worldName+"_spawn.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isSpawn(Location loc) {
        for (Location s : Main.respawns) {
            if (s.getBlockX() == loc.getBlockX() &&
                    s.getBlockY() == loc.getBlockY() &&
                    s.getBlockZ() == loc.getBlockZ()) {
                return (true);
            }
        }
        return (false);
    }

    public static Location getSpawn(Location loc) {
        for (Location s : Main.respawns) {
            if (s.getBlockX() == loc.getBlockX() &&
                    s.getBlockY() == loc.getBlockY() &&
                    s.getBlockZ() == loc.getBlockZ()) {
                return (s);
            }
        }
        return (null);
    }

    public static void spawnParticleCubeCustom(Block b, Player p, EnumParticle part) {
        final PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
        final float nb = 0.2f;
        float x = b.getX();
        float y = b.getY();
        float z = b.getZ();

        for (float i = 0; i < 1; i += nb)
            connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x + i, y, z, 0, 0, 0, 0, 1));
        for (float i = 0; i < 1; i += nb)
            connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x, y + i, z, 0, 0, 0, 0, 1));
        for (float i = 0; i < 1; i += nb)
            connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x, y, z + i, 0, 0, 0, 0, 1));

        x = b.getX() + 1;
        y = b.getY() + 1;
        z = b.getZ() + 1;

        for (float i = 0; i < 1; i += nb)
            connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x - i, y, z, 0, 0, 0, 0, 1));
        for (float i = 0; i < 1; i += nb)
            connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x, y - i, z, 0, 0, 0, 0, 1));
        for (float i = 0; i < 1; i += nb)
            connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x, y, z - i, 0, 0, 0, 0, 1));

        x = b.getX() + 1;
        y = b.getY() + 1;
        z = b.getZ();

        for (float i = 0; i < 1; i += 0.2f)
            connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x - i, y, z, 0, 0, 0, 0, 1));
        for (float i = 0; i < 1; i += 0.2f)
            connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x, y - i, z, 0, 0, 0, 0, 1));

        x = b.getX() + 1;
        y = b.getY();
        z = b.getZ() + 1;

        for (float i = 0; i < 1; i += nb)
            connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x - i, y, z, 0, 0, 0, 0, 1));
        for (float i = 0; i < 1; i += nb)
            connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x, y, z - 1, 0, 0, 0, 0, 1));

        x = b.getX();
        y = b.getY() + 1;
        z = b.getZ() + 1;

        for (float i = 0; i < 1; i += nb)
            connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x, y - i, z, 0, 0, 0, 0, 1));
        for (float i = 0; i < 1; i += nb)
            connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x, y, z - 1, 0, 0, 0, 0, 1));

        x = b.getX() + 1;
        y = b.getY();
        z = b.getZ();

        for (float i = 0; i < 1; i += nb)
            connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x, y, z + i, 0, 0, 0, 0, 1));

        x = b.getX();
        y = b.getY() + 1;
        z = b.getZ();

        for (float i = 0; i < 1; i += nb)
            connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x, y, z + i, 0, 0, 0, 0, 1));
    }

    public static void spawnRotationLine(Location s, Player p) {
        final PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
        final EnumParticle part = EnumParticle.CRIT_MAGIC;
        final float nb = 0.1f;
        float x = (float)s.getX();
        float y = (float)s.getY();
        float z = (float)s.getZ();

        switch ((int)(s.getYaw())) {
            case 45:
                for (float i = 0; i <= 0.5f; i += nb)
                    connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x - i, y + 0.5f, z + i, 0, 0, 0, 0, 1));
                break;
            case 90:
                for (float i = 0; i <= 0.5f; i += nb)
                    connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x - i, y + 0.5f, z, 0, 0, 0, 0, 1));
                break;
            case 135:
                for (float i = 0; i <= 0.5f; i += nb)
                    connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x - i, y + 0.5f, z - i, 0, 0, 0, 0, 1));
                break;
            case 180:
                for (float i = 0; i <= 0.5f; i += nb)
                    connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x, y + 0.5f, z - i, 0, 0, 0, 0, 1));
                break;
            case 225:
                for (float i = 0; i <= 0.5f; i += nb)
                    connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x + i, y + 0.5f, z - i, 0, 0, 0, 0, 1));
                break;
            case 270:
                for (float i = 0; i <= 0.5f; i += nb)
                    connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x + i, y + 0.5f, z, 0, 0, 0, 0, 1));
                break;
            case 315:
                for (float i = 0; i <= 0.5f; i += nb)
                    connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x + i, y + 0.5f, z + i, 0, 0, 0, 0, 1));
                break;
            default:
                for (float i = 0; i <= 0.5f; i += nb)
                    connection.sendPacket(new PacketPlayOutWorldParticles(part, true, x, y + 0.5f, z + i, 0, 0, 0, 0, 1));
                break;

        }
    }

    public static void spawnParticleCube(Player p) {
        for (Location s : Main.respawns) {
            if (Math.pow(s.getX() - p.getLocation().getX(), 2) +
                    Math.pow(s.getY() - p.getLocation().getY(), 2) +
                    Math.pow(s.getZ() - p.getLocation().getZ(), 2) > 1000)
                continue;

            spawnParticleCubeCustom(p.getWorld().getBlockAt(s), p, EnumParticle.CRIT);
            spawnRotationLine(s, p);
        }
    }

    public static void spawnParticlesScheduler() {
        new BukkitRunnable(){
            @Override
            public void run(){
                try {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (isWand(p.getInventory())) {
                            spawnParticleCube(p);
                        }
                    }
                } catch(Exception ignored) {}
            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0L, 5L);
    }
}
