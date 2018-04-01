package me.xjcyan1de.cyanbot.listeners;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.events.PlayerChatEvent;
import me.xjcyan1de.cyanbot.listeners.event.*;

public class ChatEvents implements Listener {

    private Player player;

    public ChatEvents(Player player) {
        this.player = player;
    }
/*
    @EventHandler(priority = EventPriority.LOW)
    public void onLow(ServerChatPacket packet) {
        System.out.println("EVENT LOW: " + packet.getMessage().getFullText());
    }

    @EventHandler()
    public void on(ServerChatPacket packet) {
        System.out.println("EVENT NORMAL: " + packet.getMessage().getFullText());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHi(ServerChatPacket packet) {
        System.out.println("EVENT HIGHEST: " + packet.getMessage().getFullText());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void on(PlayerChatEvent event) {
        if (event.getMessage().contains("123")) {
            event.setCancelled(true);
        }
    }
    @EventHandler(ignore = EventIgnore.IGNORE_CANCELLED)
    public void on1(PlayerChatEvent event) {
        System.out.println("сообщение отправляется " + event.getMessage());
    }
    @EventHandler(ignore = EventIgnore.IGNORE_NOT_CANCELLED)
    public void on2(PlayerChatEvent event) {
        System.out.println("сообщение не отправляется " + event.getMessage());
    }


    @EventHandler(ignore = EventIgnore.NOT_IGNORE)
    public void on3(PlayerChatEvent event) {
        System.out.println("сообщение не отправлется? isCancelled = " + event.isCancelled());
    }

    public static class TestEvent extends BaseCancellable {
        private String text;

        public TestEvent(String text) {
            this.text = text;
        }
    }*/
}
