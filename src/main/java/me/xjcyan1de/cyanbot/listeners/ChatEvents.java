package me.xjcyan1de.cyanbot.listeners;

import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.events.CommandEvent;
import me.xjcyan1de.cyanbot.listeners.event.*;

public class ChatEvents implements Listener {

    private Bot bot;

    public ChatEvents(Bot bot) {
        this.bot = bot;
    }


    @EventHandler
    public void on(CommandEvent event) {

    }
}
