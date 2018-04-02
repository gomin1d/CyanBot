package me.xjcyan1de.cyanbot.events;

import me.xjcyan1de.cyanbot.Bot;

/**
 * <p>Игрок с сервера написал сообщение</p>
 * <p><b>Сообщения бота не учитываются!</b></p>
 * <p><b>Сообщения других ботов учитываются!</b></p>
 */
public class PlayerChatEvent extends BotEvent {
    private String sender;
    private String message;

    public PlayerChatEvent(Bot bot, String sender, String message) {
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
