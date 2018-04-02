package me.xjcyan1de.cyanbot.listeners;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.Server;
import me.xjcyan1de.cyanbot.config.Config;
import me.xjcyan1de.cyanbot.events.PlayerChatEvent;
import me.xjcyan1de.cyanbot.listeners.event.*;
import me.xjcyan1de.cyanbot.utils.Schedule;
import me.xjcyan1de.cyanbot.utils.StringUtilsMy;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Регистрируемся на сервере<br>
 * Определяем формат чата
 */
public class InitEvents implements Listener {

    private Bot bot;
    private List<String> testChatFormat;
    private List<String> testChatResponce;
    private String botNameLower;

    public InitEvents(Bot bot) {
        this.bot = bot;
        final Config config = bot.getBotManager().getConfig();
        this.testChatFormat = config.getOrSet("test-chat.format", Arrays.asList("Всем привет!", "!Привет, народ!"));
        this.testChatResponce = config.getOrSet("test-chat.responce", Stream.of("Всем привет!", "Привет, народ!")
                .map(String::toLowerCase)
                .collect(Collectors.toList()));
        this.botNameLower = bot.getUsername().toLowerCase();
    }

    @EventHandler
    public void on(ConnectedEvent event) {
        Schedule.later(() -> {
            for (String cmd : bot.getJoinCommands()) {
                bot.sendMessage(cmd);
            }
        }, 1000);

        // если мы еще не выявиил все паттерны
        // проверка чата, если на этом сервере мы еще не знаем формат чата
        if (bot.getServer().getChatFormat().size() < testChatResponce.size()) {
            Schedule.later(() -> {
                testChatFormat.forEach(bot::sendMessage);
            }, 2000);
        }
    }

    @EventHandler
    public void on(ServerChatPacket event) {
        final String fullTextLower = event.getMessage().getFullText().toLowerCase();

        // если мы еще не выявиил все паттерны
        // если это наше тестовое сообщение и в нем есть ник бота
        if (bot.getServer().getChatFormat().size() < testChatResponce.size()) {
            for (String responce : testChatResponce) {
                if (fullTextLower.contains(responce)
                        && StringUtils.containsIgnoreCase(fullTextLower, botNameLower)) {
                    final PatternChatBetween compile = PatternChatBetween.compile(fullTextLower, botNameLower, responce);
                    if (compile != null) {
                        bot.getServer().getChatFormat().add(compile);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(ServerChatPacket packet) {
        final String fullText = packet.getMessage().getFullText();
        final Server.ChatMessageData messageData = bot.getServer().parseChatMessage(fullText);
        if (messageData != null) {
            //сообщения бота не обрабатываем
            if (!messageData.getSender().equalsIgnoreCase(bot.getUsername())) {
                bot.getEventSystem().callEvent(new PlayerChatEvent(bot, messageData.getSender(), messageData.getMessage()));
            }
        }
    }


    @EventHandler
    public void onChat(PlayerChatEvent event) {
        System.out.println("игрок " + event.getSender() + " сообщение: " + event.getMessage());
    }

    private static class PatternChatBetween implements Server.PatternChat {

        private String beforeSender;
        private String betweenSenderAndMessage;
        private String afterMessage;

        @Nullable
        public static PatternChatBetween compile(String chatMessage, String sender, String message) {
            final String beforeSender = StringUtils.substringBetween(chatMessage, "", sender);
            final String betweenSenderAndMessage = StringUtils.substringBetween(chatMessage, sender, message);
            final String afterMessage = StringUtils.substringBetween(chatMessage, message, "");

            if (beforeSender == null || betweenSenderAndMessage == null || afterMessage == null) {
                return null;
            }
            return new PatternChatBetween(beforeSender, betweenSenderAndMessage, afterMessage);
        }

        private PatternChatBetween(String beforeSender, String betweenSenderAndMessage, String afterMessage) {
            this.beforeSender = beforeSender;
            this.betweenSenderAndMessage = betweenSenderAndMessage;
            this.afterMessage = afterMessage;
        }

        @Override
        public Server.ChatMessageData getMessageData(String chatMessage) {
            String sender = StringUtilsMy.substringBetweenIrnoreCase(chatMessage, beforeSender, betweenSenderAndMessage);
            String message = afterMessage.isEmpty()
                    ? StringUtilsMy.substringAfterLastIgnoreCase(chatMessage, betweenSenderAndMessage)
                    : StringUtilsMy.substringBetweenIrnoreCase(chatMessage, betweenSenderAndMessage, afterMessage);
            return sender == null || message == null ? null : new Server.ChatMessageData(sender, message);
        }
    }
}
