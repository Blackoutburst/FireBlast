package com.blackoutburst.fireblast.main;

import com.blackoutburst.fireblast.commands.CommandEnd;
import com.blackoutburst.fireblast.commands.CommandReloadWorld;
import com.blackoutburst.fireblast.commands.CommandScan;
import com.blackoutburst.fireblast.commands.CommandStart;
import com.blackoutburst.fireblast.core.BlastPlayer;
import com.blackoutburst.fireblast.core.BlastProjectile;
import com.blackoutburst.fireblast.core.Core;
import com.blackoutburst.fireblast.utils.ScoreboardManager;
import com.blackoutburst.fireblast.utils.Utils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    public static List<BlastPlayer> players = new ArrayList<>();
    public static List<BlastProjectile> projectiles = new ArrayList<>();
    public static boolean gameRunning = false;
    public static int gameTime;

    public static Location spawn;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        new File("./plugins/FireBlast/").mkdirs();
        spawn = new Location(Bukkit.getWorld("world"), 0.5f, 5.0f, 0.5f, 0, 0);
        Core.gameTimer();
        Core.updater();
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

            Location loc = event.getPlayer().getLocation().clone();
            loc.setY(loc.getY() + event.getPlayer().getEyeHeight());

            projectiles.add(new BlastProjectile(loc, event.getPlayer().getLocation().getDirection().clone()));
        }
        if (gameRunning && event.getPlayer().getItemInHand().getType().equals(Material.BLAZE_ROD) && event.getAction() == Action.RIGHT_CLICK_AIR ||
                gameRunning && event.getPlayer().getItemInHand().getType().equals(Material.BLAZE_ROD) && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(2));
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(gameRunning);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(gameRunning);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(gameRunning);
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
            case "scan": new CommandScan().execute(sender, args); break;
            case "reloadworld": new CommandReloadWorld().execute(sender, args); break;
            default: return (true);
        }
        return (true);
    }
}
