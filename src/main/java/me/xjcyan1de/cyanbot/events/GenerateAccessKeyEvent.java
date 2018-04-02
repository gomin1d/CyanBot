package me.xjcyan1de.cyanbot.events;

import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.listeners.event.BaseCancellable;

public class GenerateAccessKeyEvent extends BaseCancellable {
    private Bot bot;
    private String key;

    public GenerateAccessKeyEvent(Bot bot, String key) {
        this.bot = bot;
        this.key = key;
    }

    public Bot getBot() {
        return bot;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
