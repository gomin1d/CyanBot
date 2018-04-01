package me.xjcyan1de.cyanbot.events;

import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.listeners.event.BaseCancellable;

public class BotChatEvent extends BaseCancellable {
    private Bot bot;
    private String message;

    public BotChatEvent(Bot bot, String message) {
        this.bot = bot;
        this.message = message;
    }

    public Bot getBot() {
        return bot;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
