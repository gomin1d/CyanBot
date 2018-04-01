package me.xjcyan1de.cyanbot.listeners;

import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import me.xjcyan1de.cyanbot.gui.MainFrame;
import me.xjcyan1de.cyanbot.utils.Schedule;

import java.util.LinkedList;
import java.util.List;

public class ChatToGuiListener extends PacketAdapter {

    private MainFrame mainFrame;
    private static List<String> chatMessageChache = new LinkedList<>();

    public ChatToGuiListener(MainFrame mainFrame) {
        super(ServerChatPacket.class);
        this.mainFrame = mainFrame;
    }

    @Override
    public void onReceived(PacketReceivedEvent event) {
        final ServerChatPacket packet = event.getPacket();
        if (packet.getType() == MessageType.CHAT || packet.getType() == MessageType.SYSTEM) {
            final String message = packet.getMessage().getFullText();
            if (!chatMessageChache.contains(message)) {
                chatMessageChache.add(message);
                mainFrame.getChat().append(message + "\n");
                mainFrame.getChat().setCaretPosition(mainFrame.getChat().getDocument().getLength());
                Schedule.later(() -> chatMessageChache.remove(message), 3000);
            }
        }
    }
}
