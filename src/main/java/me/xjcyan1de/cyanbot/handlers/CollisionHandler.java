package me.xjcyan1de.cyanbot.handlers;

import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.world.Block;
import me.xjcyan1de.cyanbot.world.Location;
import me.xjcyan1de.cyanbot.world.Vector;

public class CollisionHandler implements Handler {

    private Location loc;
    private Player player;
    private Vector speed;

    public CollisionHandler(Player player) {
        this.loc = player.getLoc();
        this.player = player;
        this.speed = player.getSpeed();
    }

    @Override
    public void onUpdate() {
        Block solid = player.getSolidBlock();//Get the first solid block under the bot, this could be very far away
        if (solid != null) {
            if (loc.getY() + speed.getY() < solid.getY() + 1) {
                speed.setY(0);
                loc.setY(solid.getY() + 1);
            }
        }
    }
}
