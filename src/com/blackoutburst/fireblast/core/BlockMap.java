package com.blackoutburst.fireblast.core;

public class BlockMap {
    protected int x;
    protected int y;
    protected int z;
    protected String material;
    protected byte data;

    public BlockMap(int x, int y, int z, String material, byte data) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.material = material;
        this.data = data;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getMaterial() {
        return material;
    }

    public byte getData() {
        return data;
    }
}
