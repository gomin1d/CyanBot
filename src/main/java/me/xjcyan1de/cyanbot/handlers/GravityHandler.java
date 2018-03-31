package me.xjcyan1de.cyanbot.handlers;

import me.xjcyan1de.cyanbot.Handler;
import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.world.Vector;

public class GravityHandler implements Handler {

    final double A = 0.08;
    private Player player;
    private Vector speed;

    public GravityHandler(Player player) {
        this.player = player;
        this.speed = player.getSpeed();
    }


    @Override
    public void onUpdate() {
        if (player.isOnGround()) {
            speed.setY(0);
        } else {
            if (speed.getY() > -3.3) {
                speed.add(0, -A, 0);
                if (speed.getY() < -3.3) {
                    speed.setY(-3.3);
                }
            }
        }
    }
}
