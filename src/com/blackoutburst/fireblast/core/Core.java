package com.blackoutburst.fireblast.core;

import com.blackoutburst.fireblast.main.Main;
import com.blackoutburst.fireblast.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
        Main.gameTime = 0;
        Main.gameRunning = true;
        for (BlastPlayer bp : Main.players) {
            resetPlayer(bp.getPlayer());
            Utils.giveItem(bp.getPlayer());
        }
    }

    public static void endGame() {
        Main.gameRunning = false;
        for (BlastPlayer bp : Main.players) {
            resetPlayer(bp.getPlayer());
        }
    }

    public static void checkEndGame() {
        int aliveCount = 0;

        for (BlastPlayer bp : Main.players) {
            if (bp.alive) aliveCount++;
        }
        if (aliveCount <= 1) {
            endGame();
        }
    }

    public static void gameTimer() {
        new BukkitRunnable(){
            @Override
            public void run(){
                try {
                    if (Main.gameRunning) {
                        gameTimerFunc();
                    }
                } catch(Exception ignored) {}
            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0L, 20L);
    }

    private static void gameTimerFunc() {
        Main.gameTime++;

        final int minutes = Main.gameTime / 60;
        final int seconds = Main.gameTime % 60;
        final String time = String.format("%d:%02d", minutes, seconds);

        for (BlastPlayer p : Main.players) {
            p.getBoard().set(13, "Time: §a"+time);
        }
    }

}
