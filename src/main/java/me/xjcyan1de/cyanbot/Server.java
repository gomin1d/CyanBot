package me.xjcyan1de.cyanbot;

import me.xjcyan1de.cyanbot.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Сервер
 */
public class Server {
    private List<PatternChat> chatFormat = new ArrayList<>(2); // формат чата (обычно 2-3 вариации)
    private World world;
    private String ipText;
    private BotManager botManager;

    public Server(Logger logger, String ipText, BotManager botManager) {
        this.botManager = botManager;
        this.world = new World(ipText, logger);
        this.ipText = ipText;
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

    public Collection<Bot> getBots() {
        return botManager.getBots().stream()
                .filter(bot -> bot.getServer().equals(this))
                .collect(Collectors.toList());
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
