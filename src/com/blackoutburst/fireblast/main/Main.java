package com.blackoutburst.fireblast.main;

import com.blackoutburst.fireblast.core.BlastPlayer;
import com.blackoutburst.fireblast.core.Core;
import com.blackoutburst.fireblast.utils.ScoreboardManager;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    public static List<BlastPlayer> players = new ArrayList<>();
    public static boolean gameRunning = false;

    public static Location spawn;


    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        spawn = new Location(Bukkit.getWorld("world"), 0.5f, 5.0f, 0.5f, 0, 0);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Core.resetPlayer(event.getPlayer());

        BlastPlayer bp = new BlastPlayer(event.getPlayer());

        ScoreboardManager.init(bp);
        players.add(bp);

        ScoreboardManager.updatePlayers();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        players.remove(BlastPlayer.getFromPlayer(event.getPlayer()));
        ScoreboardManager.updatePlayers();

        if (players.size() == 0) {
            gameRunning = false;
        }
    }

}
