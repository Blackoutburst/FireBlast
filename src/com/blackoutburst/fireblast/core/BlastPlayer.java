package com.blackoutburst.fireblast.core;

import com.blackoutburst.fireblast.main.Main;
import com.blackoutburst.fireblast.utils.Board;
import org.bukkit.entity.Player;

public class BlastPlayer {

    protected Player player;
    protected Board board;
    protected boolean alive;

    public BlastPlayer(Player player) {
        this.player = player;
        this.alive = true;
    }

    public Player getPlayer() {
        return player;
    }
    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
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
}
