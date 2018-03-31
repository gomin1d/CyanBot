package me.xjcyan1de.cyanbot.handlers;

import me.xjcyan1de.cyanbot.Handler;
import me.xjcyan1de.cyanbot.Player;

public class DebugHandler implements Handler {

    private Player player;
    private boolean enable;

    public DebugHandler(Player player, boolean enable) {
        this.player = player;
        this.enable = enable;
    }

    @Override
    public void onUpdate() {
        if (enable) {
            System.out.printf("loc: %8.3f %8.3f %8.3f  speed: %8.3f %8.3f %8.3f  solid: %s%n", player.getLoc().getX(), player.getLoc().getY(), player.getLoc().getZ(),
                    player.getSpeed().getX(), player.getSpeed().getY(), player.getSpeed().getZ(),
                    player.getSolidBlock());
        }
    }
}
