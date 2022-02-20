package com.blackoutburst.fireblast.utils;

import com.blackoutburst.fireblast.core.BlastPlayer;
import com.blackoutburst.fireblast.main.Main;
import com.blackoutburst.fireblast.nms.NMSScoreboard;

import java.util.ArrayList;

public class ScoreboardManager {

	public static void init(BlastPlayer bp) {
		NMSBoard board = new NMSBoard(bp.getPlayer(), "§cFire §6Blast");
		board.set(bp.getPlayer(), 15, "§b§m--------------------");
		board.set(bp.getPlayer(), 14, "Time: §a0:00");
		board.set(bp.getPlayer(), 13, " ");
		board.set(bp.getPlayer(), 2, "  ");
		board.set(bp.getPlayer(), 1, "§b§m-------------------- ");
		bp.setBoard(board);
	}
	
  	
  	public static void updatePlayers() {
  		for (int i = 0; i < 10; i++) {
  			if (i < Main.players.size()) {
	  			for (BlastPlayer bp : new ArrayList<>(Main.players)) {
					BlastPlayer q = Main.players.get(i);
	  				bp.getBoard().set(bp.getPlayer(),12 - i, q.isAlive() ? q.getPlayer().getDisplayName() : "§8§m"+q.getPlayer().getName());
	  			}
  			} else {
  				for (BlastPlayer bp : new ArrayList<>(Main.players)) {
  					bp.getBoard().set(bp.getPlayer(), 12 - i, "   ");
  				}
  			}
  		}
  	}
} 
