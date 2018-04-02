package me.xjcyan1de.cyanbot.world;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

/**
 * Ентити
 */
public class Entity extends View {
    private int entityId;
    private UUID uuid;

    private double x;
    private double y;
    private double z;

    private float yaw;
    private float pitch;
    private EntityMetadata metadata[];

    private EntityType type;

    private float headYaw;
    private double motX;
    private double motY;
    private double motZ;

    private boolean onGround= true;

    public Entity(int entityId, UUID uuid, double x, double y, double z, float yaw, float pitch,
                  EntityMetadata[] metadata, EntityType type) {
        this.entityId = entityId;
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.metadata = metadata;
        this.type = type;
    }

    public Entity(ServerSpawnPlayerPacket packet) {
        this(packet.getEntityId(), packet.getUUID(),
                packet.getX(), packet.getY(), packet.getZ(),
                packet.getYaw(), packet.getPitch(), packet.getMetadata(),
                EntityType.PLAYER);
    }

    public Entity(ServerSpawnMobPacket packet) {
        this(packet.getEntityId(), packet.getUUID(),
                packet.getX(), packet.getY(), packet.getZ(),
                packet.getYaw(), packet.getPitch(), packet.getMetadata(),
                EntityType.valueOf(packet.getType().name()));
    }

    public Entity(ServerSpawnObjectPacket packet) {
        this(packet.getEntityId(), packet.getUUID(),
                packet.getX(), packet.getY(), packet.getZ(),
                packet.getYaw(), packet.getPitch(), null,
                EntityType.valueOf(packet.getType().name()));
    }

    public int getEntityId() {
        return entityId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    @Nullable
    public EntityMetadata[] getMetadata() {
        return metadata;
    }

    public void setMetadata(EntityMetadata[] metadata) {
        this.metadata = metadata;
    }

    public EntityType getType() {
        return type;
    }

    public float getHeadYaw() {
        return headYaw;
    }

    public double getMotX() {
        return motX;
    }

    public double getMotY() {
        return motY;
    }

    public double getMotZ() {
        return motZ;
    }

    public void setHeadYaw(float headYaw) {
        this.headYaw = headYaw;
    }

    public void setMotX(double motX) {
        this.motX = motX;
    }

    public void setMotZ(double motZ) {
        this.motZ = motZ;
    }

    public void setMotY(double motY) {
        this.motY = motY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return entityId == entity.entityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "entityId=" + entityId +
                ", type=" + type +
                '}';
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw % 360.0F;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch % 360.0F;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void addLoc(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }
}
