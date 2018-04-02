package me.xjcyan1de.cyanbot.events;

import me.xjcyan1de.cyanbot.Bot;

/**
 * Игрок с сервера написал сообщение
 */
public class ServerPlayerChatEvent extends BotEvent {
    private String sender;
    private String message;

    public ServerPlayerChatEvent(Bot bot, String sender, String message) {
        super(bot);
        this.sender = sender;
        this.message = message;
    }

    /**
     * Текст сообщения
     */
    public String getMessage() {
        return message;
    }

    /**
     * Отправитель сообщения (игрок на сервере)
     */
    public String getSender() {
        return sender;
    }
}
