package me.xjcyan1de.cyanbot.handlers;

import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.world.Location;
import me.xjcyan1de.cyanbot.world.Vector;

/**
 * Проверка валидности данных
 */
public class ValidateHandler implements Handler {
    private Location loc;
    private Vector speed;
    private Player player;

    public ValidateHandler(Player player) {
        this.player = player;
        this.loc = player.getLoc();
        this.speed = player.getSpeed();
    }


    @Override
    public void onUpdate() {
        if (!speed.isZero()) {
            if (speed.size() > 8) {
                speed.setSize(8);
            }
        }
    }
}
