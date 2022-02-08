package com.blackoutburst.fireblast.core;

import org.bukkit.Location;
import org.bukkit.Material;

public class BlockMap {
    protected Location location;
    protected Material material;
    protected byte data;

    public BlockMap(Location location, Material material, byte data) {
        this.location = location;
        this.material = material;
        this.data = data;
    }

    public Location getLocation() {
        return location;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getData() {
        return data;
    }
}
