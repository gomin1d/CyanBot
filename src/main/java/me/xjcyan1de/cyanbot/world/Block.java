package me.xjcyan1de.cyanbot.world;

import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;

public class Block {

    private World world;

    private int id;
    private int data;
    private int x;
    private int y;
    private int z;
    private BoundBox boundBox;

    public Block(World world, int id, int data, int x, int y, int z) {
        this.world = world;
        this.id = id;
        this.data = data;
        this.x = x;
        this.y = y;
        this.z = z;
        this.boundBox = new BoundBox(1, 1, !getType().isTransparent());
        this.boundBox.update(new Vector(this.x + 0.5, this.y + 0.5, this.z + 0.5));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        world.setBlockState(x, y, z, new BlockState(id, this.data));
    }

    public void setIdAndData(int id, int data) {
        this.id = id;
        this.data = data;
        world.setBlockState(x, y, z, new BlockState(id, data));
    }


    public World getWorld() {
        return world;
    }

    public Material getType() {
        return Material.values[getId()];
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
        world.setBlockState(x, y, z, new BlockState(this.id, data));
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

    public BoundBox getBoundBox() {
        return boundBox;
    }

    @Override
    public String toString() {
        return "Block{" +
                "type=" + getType() +
                ", data=" + data +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
