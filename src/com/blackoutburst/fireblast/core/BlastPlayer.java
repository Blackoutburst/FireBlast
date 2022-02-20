package com.blackoutburst.fireblast.core;

import com.blackoutburst.fireblast.main.Main;
import com.blackoutburst.fireblast.utils.NMSBoard;
import org.bukkit.entity.Player;

public class BlastPlayer {

    protected Player player;
    protected NMSBoard board;
    protected boolean alive;
    protected int shootCooldown;
    protected int dashCooldown;

    public BlastPlayer(Player player) {
        this.player = player;
        this.alive = true;
        shootCooldown = Main.SHOOT_COOLDOWN;
        dashCooldown = Main.DASH_COOLDOWN;
    }

    public Player getPlayer() {
        return player;
    }
    public NMSBoard getBoard() {
        return board;
    }

    public void setBoard(NMSBoard board) {
        this.board = board;
    }

    public static BlastPlayer getFromPlayer(Player p) {
        for (BlastPlayer bp : Main.players) {
            if (bp.player.getUniqueId().equals(p.getUniqueId())) {
                return (bp);
            }
        }
        return (null);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getShootCooldown() {
        return shootCooldown;
    }

    public int getDashCooldown() {
        return dashCooldown;
    }

    public void setShootCooldown(int shootCooldown) {
        this.shootCooldown = shootCooldown;
    }

    public void setDashCooldown(int dashCooldown) {
        this.dashCooldown = dashCooldown;
    }
}
