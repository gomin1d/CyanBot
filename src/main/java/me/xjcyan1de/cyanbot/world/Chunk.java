package me.xjcyan1de.cyanbot.world;

import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;

import java.util.Objects;

public class Chunk {

    private World world;

    private int x;
    private int z;
    private com.github.steveice10.mc.protocol.data.game.chunk.Chunk[] sections;

    public Chunk(World world, Column column) {
        this.world = world;
        this.x = column.getX();
        this.z = column.getZ();
        this.sections = column.getChunks();
        if (x == 0 && z == 0) {
            int sectionY = 0;
            for (com.github.steveice10.mc.protocol.data.game.chunk.Chunk section : sections) {
                if (section != null) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            for (int y = 0; y < 16; y++) {
                                BlockState blockState = section.getBlocks().get(x, y, z);

                                Block block = new Block(this.world, blockState.getId(), blockState.getData(), (this.x << 4) + x, (sectionY << 4) + y, (this.z << 4) + z);
//								if (block.getId() != 0){
//									System.out.println("ID="+block.getId()+" ["+x+" "+y+" "+z+"] > "+sectionY);
//									System.out.println("   ["+block.getX()+" "+block.getY()+" "+block.getZ()+"]");
//
//									//[15 16 15]
//								}
                            }
                        }
                    }
                }
                sectionY++;
            }
        }
    }

    public Block getBlockAt(int section, int sectionX, int sectionY, int sectionZ) {
        if (sections[section] != null) {
            BlockState blockState = sections[section].getBlocks().get(sectionX, sectionY, sectionZ);
            return new Block(world, blockState.getId(), blockState.getData(), (this.x << 4) + sectionX, (section << 4) + sectionY, (this.z << 4) + sectionZ);
        } else {
            return null;
        }
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
}
