package me.xjcyan1de.cyanbot.handlers;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.world.Location;
import me.xjcyan1de.cyanbot.world.Vector;

public class UpdatePositionHandler implements Handler {
    private Location loc;
    private Vector speed;
    private Bot bot;

    private Location prev;

    public UpdatePositionHandler(Bot bot) {
        this.bot = bot;
        this.loc = bot.getLoc();
        this.speed = bot.getSpeed();
        this.prev = new Location(loc);
    }

    @Override
    public void onUpdate() {
        loc.add(speed);

        // не отправляем, если чанк не прогружен
        if (bot.getWorld().hasChunkAt(loc)) {
            ClientPlayerPositionRotationPacket packet = new ClientPlayerPositionRotationPacket(bot.isOnGround(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            bot.sendPacket(packet);
        }

        prev.set(loc);
    }
}
