package me.xjcyan1de.cyanbot.handlers;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
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
            boolean pos = Double.compare(prev.getX(), loc.getX()) != 0
                    || Double.compare(prev.getY(), loc.getY()) != 0
                    || Double.compare(prev.getZ(), loc.getZ()) != 0;
            boolean rot = Float.compare(prev.getPitch(), loc.getPitch()) != 0
                    || Float.compare(prev.getYaw(), loc.getYaw()) != 0;

            if (pos && rot) {
                bot.sendPacket(new ClientPlayerPositionRotationPacket(bot.isOnGround(),
                        loc.getX(), loc.getY(), loc.getZ(),
                        loc.getYaw(), loc.getPitch()));
            } else if (pos) {
                bot.sendPacket(new ClientPlayerPositionPacket(bot.isOnGround(),
                        loc.getX(), loc.getY(), loc.getZ()));
            } else if (rot) {
                bot.sendPacket(new ClientPlayerRotationPacket(bot.isOnGround(),
                        loc.getYaw(), loc.getPitch()));
            } else {
                return;
            }

            prev.set(loc);
        }
    }
}
