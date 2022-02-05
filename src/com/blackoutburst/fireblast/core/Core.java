package com.blackoutburst.fireblast.core;

import com.blackoutburst.fireblast.main.Main;
import com.blackoutburst.fireblast.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Core {

    public static void resetPlayer(Player p) {
        p.setHealth(20);
        p.setSaturation(20);
        p.setFoodLevel(20);
        p.setGameMode(GameMode.ADVENTURE);
        p.getInventory().clear();
        p.teleport(Main.spawn);
    }

    public static void startGame() {
        for (BlastPlayer bp : Main.players) {
            resetPlayer(bp.getPlayer());
            Utils.giveItem(bp.getPlayer());
        }
    }

    public static void endGame() {
        for (BlastPlayer bp : Main.players) {
            resetPlayer(bp.getPlayer());
        }
    }

}
