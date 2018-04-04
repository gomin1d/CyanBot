package me.xjcyan1de.cyanbot.world;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.packetlib.packet.Packet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import me.xjcyan1de.cyanbot.Bot;

import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class World {

    private String name;
    private Map<ChunkCoord, Chunk> chunkMap = new HashMap<>();
    private Logger logger;
    private Set<GameProfile> gameProfiles = new HashSet<>();
    private Int2ObjectMap<Entity> entityMap = new Int2ObjectOpenHashMap<>();

    public World(String name, Logger logger) {
        this.name = name;
        this.logger = logger;
    }

    private ChunkCoord toKey(Chunk chunk) {
        return toKey(chunk.getX(), chunk.getZ());
    }

    private ChunkCoord toKey(int x, int z) {
        return new ChunkCoord(x, z);
    }

    public Chunk getChunkAt(int x, int z) {
        return chunkMap.get(toKey(x, z));
    }

    public void setBlockState(int x, int y, int z, BlockState state) {
        Chunk chunkAt = this.getChunkAt(x >> 4, z >> 4);
        chunkAt.setBlockState(y >> 4, x & 15, y & 15, z & 15, state);
    }

    public Block getBlockAt(Location location) {
        return getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public Block getBlockAt(int x, int y, int z) {
        if (y < 0 || y > 255) {
            return null;
        }
        int sectionY = y >> 4;
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        Chunk chunk = getChunkAt(chunkX, chunkZ);
        if (chunk != null) {
            return chunk.getBlockAt(sectionY, x - (chunkX << 4), y - (sectionY << 4), z - (chunkZ << 4));
        } else {
            return null;
        }
    }

    public boolean isChunkLoaded(Location location) {
        return getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4) != null;
    }

    public boolean hasChunkAt(Location loc) {
        return chunkMap.containsKey(toKey(loc.getBlockX() >> 4, loc.getBlockZ() >> 4));
    }

    public void onLoadChunk(Bot bot, Column column) {
        final ChunkCoord chunkCoord = toKey(column.getX(), column.getZ());
        final Chunk chunk = chunkMap.computeIfAbsent(chunkCoord, key -> new Chunk(this, key.x, key.z));
        chunk.addView(bot);

        chunk.merge(column);
    }

    public void onUnloadChunk(Bot bot, int x, int z) {
        final Chunk chunk = this.getChunkAt(x, z);
        if (chunk != null) {
            if (chunk.removeView(bot) && chunk.isEmptyView()) {
                chunkMap.remove(toKey(chunk));
            }
        }
    }

    public void onDisconnect(Bot bot) {
        chunkMap.values().removeIf(chunk ->
                chunk.removeView(bot) && chunk.isEmptyView());

        entityMap.values().removeIf(entity ->
                entity.removeView(bot) && entity.isEmptyView());
    }

    public void onSpawnEntity(Bot bot, Entity entity) {
        final Entity prev = entityMap.get(entity.getEntityId());
        if (prev != null) {
            entity.setView(prev.getView());
        }

        entityMap.put(entity.getEntityId(), entity);
        entity.addView(bot);
    }

    public void onRemoveEntities(Bot bot, int[] entityIds) {
        for (int entityId : entityIds) {
            final Entity entity = entityMap.get(entityId);
            if (entity != null) {
                if (entity.removeView(bot) && entity.isEmptyView()) {
                    entityMap.remove(entityId);
                }
            }
        }
    }

    public void checkReleaseResources() {
        chunkMap.values().removeIf(View::isEmptyView);
        entityMap.values().removeIf(View::isEmptyView);
    }

    public void clearAll() {
        this.chunkMap.clear();
        this.gameProfiles.clear();
        this.entityMap.clear();
    }

    public Map<ChunkCoord, Chunk> getChunkMap() {
        return chunkMap;
    }

    public Entity getPlayer(String name) {
        final GameProfile profile = gameProfiles.stream()
                .filter(gameProfile -> gameProfile.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
        if (profile == null) {
            return null;
        }

        return this.getEntityByUUID(profile.getId(), EntityType.PLAYER);
    }

    public Int2ObjectMap<Entity> getEntityMap() {
        return entityMap;
    }

    public Entity getEntityByUUID(UUID uuid) {
        return entityMap.values().stream()
                .filter(entity -> entity.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }

    public Entity getEntityByUUID(UUID uuid, EntityType type) {
        return entityMap.values().stream()
                .filter(entity -> entity.getType().equals(type) && entity.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }

    private static class ChunkCoord {

        private int x, z;

        private ChunkCoord(int x, int z) {
            this.x = x;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ChunkCoord that = (ChunkCoord) o;
            return x == that.x &&
                    z == that.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, z);
        }
    }

    public Set<GameProfile> getGameProfiles() {
        return gameProfiles;
    }
}
