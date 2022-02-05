package com.blackoutburst.fireblast.main;

import com.blackoutburst.fireblast.commands.CommandEnd;
import com.blackoutburst.fireblast.commands.CommandStart;
import com.blackoutburst.fireblast.core.BlastPlayer;
import com.blackoutburst.fireblast.core.Core;
import com.blackoutburst.fireblast.utils.ScoreboardManager;
import com.blackoutburst.fireblast.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    public static List<BlastPlayer> players = new ArrayList<>();
    public static boolean gameRunning = false;
    public static int gameTime;

    public static Location spawn;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        spawn = new Location(Bukkit.getWorld("world"), 0.5f, 5.0f, 0.5f, 0, 0);
        Core.gameTimer();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BlastPlayer bp = new BlastPlayer(event.getPlayer());
        Core.resetPlayer(bp);
        ScoreboardManager.init(bp);
        players.add(bp);
        ScoreboardManager.updatePlayers();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getPlayer().getLocation().getBlockY() < -20) {
            if (gameRunning) {
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
                BlastPlayer bp = BlastPlayer.getFromPlayer(event.getPlayer());
                if (bp != null) bp.setAlive(false);
                Bukkit.broadcastMessage(event.getPlayer().getDisplayName()+" §efell into the void! §a"+Utils.getNumberOfPlayerAlive()+" player remaining!");
                Core.checkEndGame();
            }
            event.getPlayer().teleport(spawn);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (gameRunning && event.getPlayer().getItemInHand().getType().equals(Material.BLAZE_ROD) && event.getAction() == Action.LEFT_CLICK_AIR ||
                gameRunning && event.getPlayer().getItemInHand().getType().equals(Material.BLAZE_ROD) && event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Fireball fireball = event.getPlayer().launchProjectile(Fireball.class);
            fireball.setShooter(null);
            fireball.setDirection(fireball.getDirection().multiply(5));
            fireball.setIsIncendiary(false);
            fireball.setYield(2);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(gameRunning);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player)
            ((Player)event.getEntity()).setHealth(20);
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
            event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        players.remove(BlastPlayer.getFromPlayer(event.getPlayer()));
        ScoreboardManager.updatePlayers();

        if (players.size() == 0) {
            gameRunning = false;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName().toLowerCase()) {
            case "start": new CommandStart().execute(); break;
            case "end": new CommandEnd().execute(); break;
            default: return (true);
        }
        return (true);
    }
}
