package me.xjcyan1de.cyanbot.listeners;

import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.PacketSendingEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Фильтрация пакетов
 */
public abstract class PacketAdapter extends SessionAdapter {
    private List<Class<? extends Packet>> classes;

    /**
     * Пакеты, который будем отслеживать
     */
    public PacketAdapter(List<Class<? extends Packet>> classes) {
        this.classes = classes;
    }

    /**
     * Пакеты, которые будемотслеживать
     */
    @SafeVarargs
    public PacketAdapter(Class<? extends Packet> ... classes) {
        this.classes = Arrays.asList(classes);
    }

    @Override
    public void packetSending(PacketSendingEvent event) {
        for (Class<? extends Packet> aClass : classes) {
            if (aClass.isInstance(event.getPacket())) {
                this.onSend(event);
                break;
            }
        }
    }

    @Override
    public void packetReceived(PacketReceivedEvent event) {
        for (Class<? extends Packet> aClass : classes) {
            if (aClass.isInstance(event.getPacket())) {
                this.onReceived(event);
                break;
            }
        }
    }

    /**
     * Вызывается, когда пакет отправляется
     */
    public void onSend(PacketSendingEvent event) {}

    /**
     * Вызывается, когда пакет принимается
     */
    public void onReceived(PacketReceivedEvent event) {}
}
