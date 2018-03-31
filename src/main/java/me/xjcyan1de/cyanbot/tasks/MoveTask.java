package me.xjcyan1de.cyanbot.tasks;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.world.Location;

public class MoveTask extends Task {

    public MoveTask(Player player) {
        super(player);
    }

    @Override
    public void run(String... args) {
        Player player = getPlayer();
        Location location = player.getLoc();
        for (double z = location.getZ(); z < location.getZ() + 5; z++) {
            z = +0.2;
            player.sendPacket(new ClientPlayerPositionRotationPacket(true, location.getX(), location.getY(), z, 0, 0));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
