package me.xjcyan1de.cyanbot.listeners;

import com.github.steveice10.packetlib.event.session.*;
import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.listeners.event.EventSystem;

/**
 * Базовые ивенты
 */
public class BotListener implements SessionListener {

    private EventSystem eventSystem;

    public BotListener(Bot bot, EventSystem eventSystem) {
        this.eventSystem = eventSystem;
    }

    @Override
    public void packetReceived(PacketReceivedEvent event) {
        eventSystem.callEvent(event); // сам ивент
        eventSystem.callEvent(event.getPacket()); // и пакет отдельно
    }

    @Override
    public void packetSending(PacketSendingEvent event) {
        eventSystem.callEvent(event); // сам ивент
        eventSystem.callEvent(event.getPacket()); // и пакет отдельно
    }

    @Override
    public void packetSent(PacketSentEvent event) { }

    @Override
    public void connected(ConnectedEvent event) {
        eventSystem.callEvent(event);
    }

    @Override
    public void disconnecting(DisconnectingEvent event) { }

    @Override
    public void disconnected(DisconnectedEvent event) {
        eventSystem.callEvent(event);
    }
}
