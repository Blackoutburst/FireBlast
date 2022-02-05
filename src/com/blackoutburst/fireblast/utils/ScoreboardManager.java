package com.blackoutburst.fireblast.utils;

import com.blackoutburst.fireblast.core.BlastPlayer;
import com.blackoutburst.fireblast.main.Main;

import java.util.ArrayList;

public class ScoreboardManager {

	public static void clear(BlastPlayer p) {
		for (int i = 15; i >= 0; i--)
			p.getBoard().remove(i);
	}
	
	public static void init(BlastPlayer bp) {
		Board board = new Board(bp.getPlayer());
		
		board.setTitle("§6Quake");
		board.set(15, "§b§m--------------------"); 
		board.set(14, "Map: §aSpawn");
		board.set(13, "Time: §a0:00");
		board.set(12, " "); 
		board.set(2, "  ");
		board.set(1, "§b§m-------------------- "); 
		bp.setBoard(board);
	}
	
  	
  	public static void updatePlayers() {
  		for (int i = 0; i < 9; i++) {
  			if (i < Main.players.size()) {
	  			for (BlastPlayer bp : new ArrayList<>(Main.players)) {
					BlastPlayer q = Main.players.get(i);
	  				bp.getBoard().set(11 - i, q.isAlive() ? q.getPlayer().getDisplayName() : "   ");
	  			}
  			} else {
  				for (BlastPlayer bp : new ArrayList<>(Main.players)) {
  					bp.getBoard().set(11 - i, "   ");
  				}
  			}
  		}
  	}
} 
