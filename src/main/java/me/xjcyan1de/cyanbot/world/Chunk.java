package me.xjcyan1de.cyanbot.world;

import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import me.xjcyan1de.cyanbot.Bot;

import java.util.*;

public class Chunk {

    private World world;

    private int x;
    private int z;
    private com.github.steveice10.mc.protocol.data.game.chunk.Chunk[] sections;

    private Set<Bot> bots = new HashSet<>(); //view

    public Chunk(World world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
        this.sections = new com.github.steveice10.mc.protocol.data.game.chunk.Chunk[16];
    }

    public Block getBlockAt(int section, int sectionX, int sectionY, int sectionZ) {
        if (sections[section] != null) {
            BlockState blockState = sections[section].getBlocks().get(sectionX, sectionY, sectionZ);
            return new Block(world, blockState.getId(), blockState.getData(), (this.x << 4) + sectionX, (section << 4) + sectionY, (this.z << 4) + sectionZ);
        } else {
            return null;
        }
    }

    public void addView(Bot bot) {
        this.bots.add(bot);
    }
    
    public void removeView(Bot bot) {
        this.bots.remove(bot);
    }

    public Set<Bot> getView() {
        return bots;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public void setBlockState(int sectionY, int offX, int offY, int offZ, BlockState state) {
        if (sections[sectionY] != null) {
            sections[sectionY].getBlocks().set(offX, offY, offZ, state);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Chunk chunk = (Chunk) o;
        return x == chunk.x &&
                z == chunk.z &&
                Objects.equals(world, chunk.world);
    }

    public com.github.steveice10.mc.protocol.data.game.chunk.Chunk[] getSections() {
        return sections;
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, z);
    }

    @Override
    public String toString() {
        return "Chunk{" +
                ", x=" + x +
                ", z=" + z +
                '}';
    }

    public void merge(Column column) {
        for (int i = 0; i < 16; i++) {
            if (column.getChunks()[i] != null) {
                sections[i] = column.getChunks()[i];
            }
        }
    }
}
