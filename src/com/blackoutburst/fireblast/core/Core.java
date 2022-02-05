package com.blackoutburst.fireblast.core;

import com.blackoutburst.fireblast.main.Main;
import com.blackoutburst.fireblast.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Core {

    public static void resetPlayer(BlastPlayer p) {
        p.getPlayer().setHealth(20);
        p.getPlayer().setSaturation(20);
        p.getPlayer().setFoodLevel(20);
        p.getPlayer().setGameMode(GameMode.ADVENTURE);
        p.getPlayer().getInventory().clear();
        p.getPlayer().teleport(Main.spawn);
        p.setAlive(true);
    }

    public static void startGame() {
        Main.gameTime = 0;
        Main.gameRunning = true;
        for (BlastPlayer bp : Main.players) {
            resetPlayer(bp);
            Utils.giveItem(bp.getPlayer());
        }
    }

    public static void endGame() {
        Main.gameRunning = false;
        BlastPlayer winner = null;
        
        for (BlastPlayer bp : Main.players) {
            if (bp.isAlive()) winner = bp;
            resetPlayer(bp);
        }

        if (winner != null)
            Bukkit.broadcastMessage(winner.getPlayer().getDisplayName()+" §ewon the game!");
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
