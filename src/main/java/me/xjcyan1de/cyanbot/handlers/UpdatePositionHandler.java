package me.xjcyan1de.cyanbot.handlers;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.world.Location;
import me.xjcyan1de.cyanbot.world.Vector;

public class UpdatePositionHandler implements Handler {
    private Location loc;
    private Vector speed;
    private Player player;

    private Location prev;

    public UpdatePositionHandler(Player player) {
        this.player = player;
        this.loc = player.getLoc();
        this.speed = player.getSpeed();
        this.prev = new Location(loc);
    }

    @Override
    public void onUpdate() {
        loc.add(speed);

        // не отправляем, если чанк не прогружен
        if (player.getWorld().hasChunkAt(loc)) {
            ClientPlayerPositionRotationPacket packet = new ClientPlayerPositionRotationPacket(player.isOnGround(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            player.sendPacket(packet);
        }

        prev.set(loc);
    }

    @Override
    public void onStartBot() {

    }
}
