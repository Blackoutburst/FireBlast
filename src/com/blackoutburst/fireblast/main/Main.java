package com.blackoutburst.fireblast.main;

import com.blackoutburst.fireblast.commands.*;
import com.blackoutburst.fireblast.core.*;
import com.blackoutburst.fireblast.utils.ScoreboardManager;
import com.blackoutburst.fireblast.utils.Utils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    public static final int SHOOT_COOLDOWN = 10;
    public static final int DASH_COOLDOWN = 100;

    public static List<BlastPlayer> players = new ArrayList<>();
    public static List<BlastProjectile> projectiles = new ArrayList<>();
    public static List<Location> respawns = new ArrayList<>();
    public static boolean gameRunning = false;
    public static int gameTime;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        new File("./plugins/FireBlast/").mkdirs();
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
            Core.teleportToSpawnpoint(event.getPlayer());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        SpawnWand.leftClick(event);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        SpawnWand.rightClick(event);

        BlastPlayer bp = BlastPlayer.getFromPlayer(event.getPlayer());
        if (bp == null) return;

        final Material item = event.getPlayer().getItemInHand().getType();
        final Action action = event.getAction();
        final boolean holdWand = item.equals(Material.BLAZE_ROD) || item.equals(Material.STICK);
        final boolean leftClick = action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR;
        final boolean rightClick = action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR;

        if (Main.gameRunning && holdWand && bp.getDashCooldown() <= 0 && leftClick) {
            BlastWand.leftClick(event, bp);
        }

        if (Main.gameRunning && holdWand && bp.getShootCooldown() <= 0 && rightClick) {
            BlastWand.rightClick(event, bp);
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
            case "spawnwand": new CommandSpawnWand().execute(sender); break;
            default: return (true);
        }
        return (true);
    }
}
