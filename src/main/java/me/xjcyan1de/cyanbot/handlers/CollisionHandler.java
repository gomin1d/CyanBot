package me.xjcyan1de.cyanbot.handlers;

import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.world.Block;
import me.xjcyan1de.cyanbot.world.Location;
import me.xjcyan1de.cyanbot.world.Vector;

public class CollisionHandler implements Handler {

    private Location loc;
    private Bot bot;
    private Vector speed;

    public CollisionHandler(Bot bot) {
        this.loc = bot.getLoc();
        this.bot = bot;
        this.speed = bot.getSpeed();
    }

    @Override
    public void onUpdate() {
        Block solid = bot.getSolidBlock();//Get the first solid block under the bot, this could be very far away
        if (solid != null) {
            if (loc.getY() + speed.getY() < solid.getY() + 1) {
                speed.setY(0);
                loc.setY(solid.getY() + 1);
            }
        }
    }
}
