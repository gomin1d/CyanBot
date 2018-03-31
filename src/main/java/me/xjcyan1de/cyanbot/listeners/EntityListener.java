package me.xjcyan1de.cyanbot.listeners;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;

public class EntityListener extends SessionAdapter {
    @Override
    public void packetReceived(PacketReceivedEvent event) {
        final Packet packet = event.getPacket();
        try {
            if (packet instanceof ServerSpawnMobPacket) {

            } else if (packet instanceof ServerSpawnPlayerPacket) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
