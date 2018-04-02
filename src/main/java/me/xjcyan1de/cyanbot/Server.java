package me.xjcyan1de.cyanbot;

import me.xjcyan1de.cyanbot.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервер
 */
public class Server {
    private List<PatternChat> chatFormat = new ArrayList<>(2); // формат чата (обычно 2-3 вариации)
    private World world;

    public Server(World world) {
        this.world = world;
    }

    public List<PatternChat> getChatFormat() {
        return chatFormat;
    }

    public World getWorld() {
        return world;
    }

    public ChatMessageData parseChatMessage(String fullText) {
        for (PatternChat patternChat : chatFormat) {
            final ChatMessageData messageData = patternChat.getMessageData(fullText);
            if (messageData != null) {
                return messageData;
            }
        }
        return null;
    }

    public static class ChatMessageData {
        private String sender;
        private String message;

        public ChatMessageData(String sender, String message) {
            this.sender = sender;
            this.message = message;
        }

        public String getSender() {
            return sender;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * Паттерт сообщения в чате
     */
    public interface PatternChat {
        /**
         * Получить отправитиля сообщения
         */
        ChatMessageData getMessageData(String chatMessage);
    }
}
