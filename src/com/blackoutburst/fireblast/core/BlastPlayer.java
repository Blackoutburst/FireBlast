package com.blackoutburst.fireblast.core;

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

}
