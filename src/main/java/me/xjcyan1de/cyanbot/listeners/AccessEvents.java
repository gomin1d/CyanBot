package me.xjcyan1de.cyanbot.listeners;

import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.events.PlayerChatEvent;
import me.xjcyan1de.cyanbot.listeners.event.EventHandler;
import me.xjcyan1de.cyanbot.listeners.event.Listener;

public class AccessEvents implements Listener {
    private Bot bot;

    public AccessEvents(Bot bot) {
        this.bot = bot;
    }

    @EventHandler
    public void on(PlayerChatEvent event) {
        final String message = event.getMessage();
        if (message.equals(bot.getAccessKey())) {
            bot.setAccessKey(null);
            bot.getAccessPlayers().add(event.getSender());
            bot.sendMessage(event.getSender() + ", вы авторизированы!");
        }
    }
}
