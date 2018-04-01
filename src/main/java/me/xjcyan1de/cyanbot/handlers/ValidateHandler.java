package me.xjcyan1de.cyanbot.handlers;

import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.world.Location;
import me.xjcyan1de.cyanbot.world.Vector;

/**
 * Проверка валидности данных
 */
public class ValidateHandler implements Handler {
    private Location loc;
    private Vector speed;
    private Bot bot;

    public ValidateHandler(Bot bot) {
        this.bot = bot;
        this.loc = bot.getLoc();
        this.speed = bot.getSpeed();
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
