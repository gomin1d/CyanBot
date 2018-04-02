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
import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.utils.Schedule;
import me.xjcyan1de.cyanbot.world.Block;
import me.xjcyan1de.cyanbot.world.Chunk;
import me.xjcyan1de.cyanbot.world.Location;

public class PacketWorldListener extends SessionAdapter {

    private Bot bot;

    public PacketWorldListener(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void packetReceived(PacketReceivedEvent event) {
        try {
            Packet packetHandle = event.getPacket();
            if (packetHandle instanceof ServerJoinGamePacket) {
                ServerJoinGamePacket packet = (ServerJoinGamePacket) packetHandle;
                bot.setEntityId(packet.getEntityId());
            } else if (packetHandle instanceof ServerPlayerPositionRotationPacket) {
                ServerPlayerPositionRotationPacket packet = (ServerPlayerPositionRotationPacket) packetHandle;
                Location location = new Location(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
                bot.setLoc(location);

                //bot.sendMessage("Меня тпхнуло: "+location);

                //System.out.println(" \nТелепортируем : ["+location.getX()+" "+location.getY()+" "+location.getZ()+"]");
                bot.sendPacket(new ClientTeleportConfirmPacket(packet.getTeleportId()));
                //bot.groundHandler.move(location);
            } else if (packetHandle instanceof ServerChunkDataPacket) {
                ServerChunkDataPacket packet = (ServerChunkDataPacket) packetHandle;
                bot.getWorld().mergeChunk(bot, packet.getColumn());

            } else if (packetHandle instanceof ServerUnloadChunkPacket) {
                ServerUnloadChunkPacket packet = (ServerUnloadChunkPacket) packetHandle;
                Chunk chunk = bot.getWorld().getChunkAt(packet.getX(), packet.getZ());
                bot.getWorld().removeView(bot, chunk);

            } else if (packetHandle instanceof ServerPlayerHealthPacket) {
                ServerPlayerHealthPacket packet = (ServerPlayerHealthPacket) packetHandle;
                if (packet.getHealth() == 0) {
                    Schedule.later(() -> {
                        bot.sendPacket(new ClientRequestPacket(ClientRequest.RESPAWN));
                    }, 500);
                }
            } else if (packetHandle instanceof ServerBlockChangePacket) {
                ServerBlockChangePacket packet = (ServerBlockChangePacket) packetHandle;
                BlockState blockState = packet.getRecord().getBlock();
                if (blockState != null) {
                    Position position = packet.getRecord().getPosition();
                    Block block = bot.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());
                    if (block != null) {
                        block.setIdAndData(blockState.getId(), blockState.getData());
                        //System.out.println("Новый блок = " + block);
                    } else {
                        System.out.println("Чё за хуйня у нас блок == Null" + position.getX() + " " + position.getY() + " " + position.getZ());
                    }
                }
            } else {
                /*if (packetHandle instanceof ServerUpdateTimePacket ||
                        packetHandle instanceof ServerKeepAlivePacket ||
                        packetHandle instanceof ServerPlaySoundPacket ||
                        packetHandle instanceof ServerEntityEffectPacket

                        ) {
                } else {
                    //System.out.println(bot.getUsername()+" <- "+packetHandle);
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnected(DisconnectedEvent event) {
        bot.getWorld().removeViewAll(bot);

        System.out.println(bot.getUsername() + " ✖ " + Message.fromString(event.getReason()).getFullText());
        bot.stopBot();
        if (event.getCause() != null) {
            event.getCause().printStackTrace();
        }
    }
}
