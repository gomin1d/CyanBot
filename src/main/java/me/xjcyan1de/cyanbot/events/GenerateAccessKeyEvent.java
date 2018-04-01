package me.xjcyan1de.cyanbot.events;

import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.listeners.event.BaseCancellable;
import me.xjcyan1de.cyanbot.listeners.event.EventHandler;
import me.xjcyan1de.cyanbot.listeners.event.Listener;

public class GenerateAccessKeyEvent extends BaseCancellable {
    private Player player;
    private String name;
    private String key;

    public GenerateAccessKeyEvent(Player player, String name, String key) {
        this.player = player;
        this.name = name;
        this.key = key;
    }

    public Player getPlayer() {
        return player;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }
}
