package com.blackoutburst.fireblast.core;

import com.blackoutburst.fireblast.main.Main;
import com.blackoutburst.fireblast.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Core {

    public static void resetPlayer(BlastPlayer p) {
        p.getPlayer().setHealth(20);
        p.getPlayer().setSaturation(20);
        p.getPlayer().setFoodLevel(20);
        p.getPlayer().setGameMode(GameMode.SURVIVAL);
        p.getPlayer().getInventory().clear();
        p.getPlayer().teleport(Main.spawn);
        p.setAlive(true);
    }

    public static void startGame() {
        Main.gameTime = 0;
        Main.gameRunning = true;
        for (BlastPlayer bp : Main.players) {
            bp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 2, false, false));
            bp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100000, 1, false, false));
            resetPlayer(bp);
            Utils.giveItem(bp.getPlayer(), false);
        }
    }

    public static void endGame() {
        Main.projectiles.clear();
        Main.gameRunning = false;
        BlastPlayer winner = null;

        for (BlastPlayer bp : Main.players) {
            bp.getPlayer().removePotionEffect(PotionEffectType.SPEED);
            bp.getPlayer().removePotionEffect(PotionEffectType.JUMP);
            if (bp.isAlive()) winner = bp;
            resetPlayer(bp);
        }

        if (winner != null) {
            Utils.reloadWorld(winner.getPlayer().getWorld());

            final int minutes = Main.gameTime / 60;
            final int seconds = Main.gameTime % 60;
            final String time = String.format("%d:%02d", minutes, seconds);

            Bukkit.broadcastMessage("§6§m----------------------------------------");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(Utils.centerText("§cTime: §r"+time));
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(Utils.centerText(winner.getPlayer().getDisplayName()+" §ewon the game!"));
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§6§m----------------------------------------");
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

    public static void updater() {
        new BukkitRunnable(){
            @Override
            public void run(){
                if (Main.gameRunning) {
                    for (BlastPlayer bp : Main.players) {
                        if (bp.dashCooldown > 0)
                            bp.dashCooldown--;
                        if (bp.shootCooldown > 0)
                            bp.shootCooldown--;
                        if (bp.shootCooldown == 1)
                            Utils.giveItem(bp.getPlayer(), false);
                    }

                    int j = Main.projectiles.size();

                    for (int i = 0; i < j; i++) {
                        BlastProjectile bp = Main.projectiles.get(i);
                        bp.update();
                        if (!bp.alive) {
                            Main.projectiles.remove(bp);
                            j--;
                            i--;
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);
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
            p.getBoard().set(14, "Time: §a"+time);
        }
    }

}
