package me.xjcyan1de.cyanbot.events;

import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.listeners.event.BaseCancellable;

public class PlayerChatEvent extends BaseCancellable {
    private Player player;
    private String message;

    public PlayerChatEvent(Player player, String message) {
        this.player = player;
        this.message = message;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
