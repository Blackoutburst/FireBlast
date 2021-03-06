package com.blackoutburst.fireblast.core;

import com.blackoutburst.fireblast.main.Main;
import com.blackoutburst.fireblast.nms.NMSParticles;
import com.blackoutburst.fireblast.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

public class BlastProjectile {

    protected Location location;
    protected Vector direction;
    protected boolean alive;
    protected int tick;

    private int circle = 0;

    public BlastProjectile (Location location, Vector direction) {
        this.location = location;
        this.direction = direction;
        this.alive = true;
        this.tick = 50;
    }

    private boolean hitBlock() {
        return (!location.getWorld().getBlockAt(location).getType().equals(Material.AIR));
    }

    private void explode() {
        final float xloc = (float) this.location.getX();
        final float yloc = (float) this.location.getY();
        final float zloc = (float) this.location.getZ();

        alive = false;

        for (int x = location.getBlockX() - 1; x <= location.getBlockX() + 1; x++)
            for (int y = location.getBlockY() - 1; y <= location.getBlockY() + 1; y++)
                for (int z = location.getBlockZ() - 1; z <= location.getBlockZ() + 1; z++)
                    location.getWorld().getBlockAt(new Location(location.getWorld(), x, y, z)).setType(Material.AIR);
        location.getWorld().playSound(location, Sound.EXPLODE, 3, 1);
        for (final BlastPlayer bp : Main.players)
            NMSParticles.send(bp.player, NMSParticles.ParticleType.EXPLOSION_LARGE, xloc, yloc, zloc, 0, 0, 0, 1);
    }

    private void trail() {
        final float xloc = (float) this.location.getX();
        final float yloc = (float) this.location.getY();
        final float zloc = (float) this.location.getZ();

        for (final BlastPlayer bp : Main.players) {
            NMSParticles.send(bp.player, NMSParticles.ParticleType.FIREWORKS_SPARK, xloc, yloc, zloc, 0, 0, 0, 1);
            if (circle > 3) {
                Utils.createCircle(bp.player, location);
            }
        }
        if (circle > 3) {
            circle = 0;
        }
        circle++;
    }

    public void update() {
        tick--;

        if (tick <= 0)
            alive = false;

        for (int i = 0; i < 4; i++) {
            location.add(direction);
            trail();
            if (hitBlock())
                explode();
        }
    }
}
