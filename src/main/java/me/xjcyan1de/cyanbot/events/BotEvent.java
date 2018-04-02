package me.xjcyan1de.cyanbot.events;

import me.xjcyan1de.cyanbot.Bot;

/**
 * event с ботом
 */
public abstract class BotEvent {
    private Bot bot;

    protected BotEvent(Bot bot) {
        this.bot = bot;
    }

    public Bot getBot() {
        return bot;
    }
}
