package me.xjcyan1de.cyanbot.handlers;

import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.world.Block;
import me.xjcyan1de.cyanbot.world.Location;
import me.xjcyan1de.cyanbot.world.Vector;

@SuppressWarnings("WeakerAccess")
public class GroundHandler implements Handler {

    private Player player;
    private Vector speed;
    private Location loc;

    public GroundHandler(Player player) {
        this.player = player;
        this.loc = player.getLoc();
        this.speed = player.getSpeed();
    }

    @Override
    public void onUpdate() {

        Block solid = player.getSolidBlock();//Get the first solid block under the bot, this could be very far away
        if (solid == null) { //Make sure that the block isn't null
            player.setOnGround(false);//make sure the server knows we know we aren't onUpdate the ground
        } else {
            if (this.canDownMove(solid)) {
                player.setOnGround(false);//make sure the server knows we know we aren't onUpdate the ground
            } else {
                player.setOnGround(true);//and let the server know that we know that we are onUpdate the ground
            }
        }
    }

    private boolean canDownMove(Block solid) {
        return loc.getY() + speed.getY() > solid.getY() + 1;
    }
}
