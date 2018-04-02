package me.xjcyan1de.cyanbot.listeners;

import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntryAction;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.*;
import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.listeners.event.EventHandler;
import me.xjcyan1de.cyanbot.listeners.event.Listener;
import me.xjcyan1de.cyanbot.world.Entity;
import me.xjcyan1de.cyanbot.world.World;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PacketWorldEntityEvents implements Listener {
    private Bot bot;
    private World world;

    public PacketWorldEntityEvents(Bot bot) {
        this.bot = bot;
        this.world = bot.getWorld();
    }

    /*@EventHandler пока это не ловим
    public void on(ServerSpawnGlobalEntityPacket packet) {
        System.out.println(packet);
    }*/

    @EventHandler
    public void on(ServerSpawnPlayerPacket packet) {
        world.onSpawnEntity(bot, new Entity(packet));
    }

    @EventHandler
    public void on(ServerSpawnMobPacket packet) {
        world.onSpawnEntity(bot, new Entity(packet));
    }

    @EventHandler
    public void on(ServerPlayerListEntryPacket packet) {
        if (packet.getAction().equals(PlayerListEntryAction.ADD_PLAYER)) {
            world.getGameProfiles().addAll(Stream.of(packet.getEntries())
                    .map(PlayerListEntry::getProfile)
                    .collect(Collectors.toList()));
        } else if (packet.getAction().equals(PlayerListEntryAction.REMOVE_PLAYER)) {
            world.getGameProfiles().removeAll(Stream.of(packet.getEntries())
                    .map(PlayerListEntry::getProfile)
                    .collect(Collectors.toList()));
        }
    }

    @EventHandler
    public void on(ServerEntityMetadataPacket packet) {
        final Entity entity = world.getEntityMap().get(packet.getEntityId());
        if (entity != null) {
            entity.setMetadata(packet.getMetadata());
        }
    }

    @EventHandler
    public void on(ServerEntityTeleportPacket packet) {
        final Entity entity = world.getEntityMap().get(packet.getEntityId());
        if (entity != null) {
            entity.setX(packet.getX());
            entity.setY(packet.getY());
            entity.setZ(packet.getZ());
            entity.setPitch(packet.getPitch());
            entity.setYaw(packet.getYaw());
        }
    }

    @EventHandler
    public void on(ServerEntityPositionRotationPacket packet) {
        this.onMove(packet, true, true);
    }

    @EventHandler
    public void on(ServerEntityRotationPacket packet) {
        this.onMove(packet, false, true);
    }

    @EventHandler
    public void on(ServerEntityPositionPacket packet) {
        this.onMove(packet, true, false);
    }

    public void onMove(ServerEntityMovementPacket packet, boolean pos, boolean rot) {
        final Entity entity = world.getEntityMap().get(packet.getEntityId());
        if (entity != null) {
            if (pos) {
                entity.addLoc(
                        packet.getMovementX(),
                        packet.getMovementY(),
                        packet.getMovementZ());

                entity.setOnGround(packet.isOnGround());
            }

            if (rot) {
                entity.setYaw(packet.getYaw());
                entity.setPitch(packet.getPitch());
            }
        }
    }
    
    /*@EventHandler пока это не ловим
    public void on(ServerSpawnExpOrbPacket packet) {
        System.out.println(packet);
    }*/

    @EventHandler
    public void on(ServerSpawnObjectPacket packet) {
        world.onSpawnEntity(bot, new Entity(packet));
    }

    /*@EventHandler пока это не ловим
    public void on(ServerSpawnPaintingPacket packet) {
        System.out.println(packet);
    }*/

    @EventHandler
    public void on(ServerEntityDestroyPacket packet) {
        bot.getServer().getWorld().onRemoveEntities(bot, packet.getEntityIds());
    }
}
