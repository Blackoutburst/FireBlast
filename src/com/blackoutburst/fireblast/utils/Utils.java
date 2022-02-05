package com.blackoutburst.fireblast.utils;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Utils {

    public static void giveItem(Player p) {
        ItemStack menu = new ItemStack(Material.NETHER_STAR, 1);
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
