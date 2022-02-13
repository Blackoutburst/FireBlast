package com.blackoutburst.fireblast.core;

import com.blackoutburst.fireblast.main.Main;
import com.blackoutburst.fireblast.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlastWand {

    public static void leftClick(PlayerInteractEvent event, BlastPlayer bp) {
        bp.setDashCooldown(Main.DASH_COOLDOWN);
        event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(2));
        event.getPlayer().getLocation().getWorld().playSound(event.getPlayer().getLocation(), Sound.BAT_TAKEOFF, 2, 0.5f);
    }

    public static void rightClick(PlayerInteractEvent event, BlastPlayer bp) {
        bp.setShootCooldown(Main.SHOOT_COOLDOWN);
        Location loc = event.getPlayer().getLocation().clone();
        loc.setY(loc.getY() + event.getPlayer().getEyeHeight());
        Main.projectiles.add(new BlastProjectile(loc, event.getPlayer().getLocation().getDirection().clone()));
        event.getPlayer().getLocation().getWorld().playSound(event.getPlayer().getLocation(), Sound.IRONGOLEM_HIT, 2, 2);
        Utils.giveItem(bp.getPlayer(), true);
    }

}
