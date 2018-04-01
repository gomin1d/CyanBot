package me.xjcyan1de.cyanbot.handlers;

import me.xjcyan1de.cyanbot.Bot;

public class DebugHandler implements Handler {

    private Bot bot;

    public DebugHandler(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onUpdate() {
        if (bot.isDebug()) {
            System.out.printf("loc: %8.3f %8.3f %8.3f  speed: %8.3f %8.3f %8.3f  solid: %s%n", bot.getLoc().getX(), bot.getLoc().getY(), bot.getLoc().getZ(),
                    bot.getSpeed().getX(), bot.getSpeed().getY(), bot.getSpeed().getZ(),
                    bot.getSolidBlock());
        }
    }
}
