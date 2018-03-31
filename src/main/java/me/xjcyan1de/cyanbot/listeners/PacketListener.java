package me.xjcyan1de.cyanbot.listeners;

import com.github.steveice10.mc.protocol.data.game.ClientRequest;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientRequestPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.*;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.utils.Schedule;
import me.xjcyan1de.cyanbot.world.Block;
import me.xjcyan1de.cyanbot.world.Chunk;
import me.xjcyan1de.cyanbot.world.Location;

public class PacketListener extends SessionAdapter {

    private Player player;

    public PacketListener(Player player) {
        this.player = player;
    }

    @Override
    public void packetReceived(PacketReceivedEvent event) {
        try {
            Packet packetHandle = event.getPacket();
            if (packetHandle instanceof ServerJoinGamePacket) {
                ServerJoinGamePacket packet = (ServerJoinGamePacket) packetHandle;
                player.setEntityId(packet.getEntityId());
                player.onJoin(packet);
            } else if (packetHandle instanceof ServerPlayerPositionRotationPacket) {
                ServerPlayerPositionRotationPacket packet = (ServerPlayerPositionRotationPacket) packetHandle;
                Location location = new Location(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
                player.setLoc(location);

                //player.sendMessage("Меня тпхнуло: "+location);

                //System.out.println(" \nТелепортируем : ["+location.getX()+" "+location.getY()+" "+location.getZ()+"]");
                player.sendPacket(new ClientTeleportConfirmPacket(packet.getTeleportId()));
                //player.groundHandler.move(location);
            } else if (packetHandle instanceof ServerChunkDataPacket) {
                ServerChunkDataPacket packet = (ServerChunkDataPacket) packetHandle;
                Chunk chunk = new Chunk(player.getWorld(), packet.getColumn());

                player.getWorld().mergeChunks(chunk);

                //System.out.println("Загрузили чанк: " + chunk.getX() + " " + chunk.getZ());
            } else if (packetHandle instanceof ServerUnloadChunkPacket) {
                ServerUnloadChunkPacket packet = (ServerUnloadChunkPacket) packetHandle;
                Chunk chunk = player.getWorld().getChunkAt(packet.getX(), packet.getZ());
                player.getWorld().removeChunk(chunk);
                //System.out.println("Удалили чанк: " + chunk.getX() + " " + chunk.getZ());
            } else if (packetHandle instanceof ServerPlayerHealthPacket) {
                ServerPlayerHealthPacket packet = (ServerPlayerHealthPacket) packetHandle;
                if (packet.getHealth() == 0) {
                    Schedule.later(() -> {
                        player.sendPacket(new ClientRequestPacket(ClientRequest.RESPAWN));
                    }, 500);
                }
            } else if (packetHandle instanceof ServerBlockChangePacket) {
                ServerBlockChangePacket packet = (ServerBlockChangePacket) packetHandle;
                BlockState blockState = packet.getRecord().getBlock();
                if (blockState != null) {
                    Position position = packet.getRecord().getPosition();
                    Block block = player.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());
                    if (block != null) {
                        block.setIdAndData(blockState.getId(), blockState.getData());
                        //System.out.println("Новый блок = " + block);
                    } else {
                        System.out.println("Чё за хуйня у нас блок == Null" + position.getX() + " " + position.getY() + " " + position.getZ());
                    }
                }
            } else {
                if (packetHandle instanceof ServerUpdateTimePacket ||
                        packetHandle instanceof ServerKeepAlivePacket ||
                        packetHandle instanceof ServerPlaySoundPacket ||
                        packetHandle instanceof ServerEntityEffectPacket

                        ) {
                } else {
                    //System.out.println(player.getUsername()+" <- "+packetHandle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnected(DisconnectedEvent event) {
        System.out.println(player.getUsername() + " ✖ " + Message.fromString(event.getReason()).getFullText());
        player.stopBot();
        if (event.getCause() != null) {
            event.getCause().printStackTrace();
        }
    }
}
