package me.xjcyan1de.cyanbot.listeners;

import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import me.xjcyan1de.cyanbot.gui.MainFrame;

public class ChatToGuiListener extends PacketAdapter {

    private MainFrame mainFrame;

    public ChatToGuiListener(MainFrame mainFrame) {
        super(ServerChatPacket.class);
        this.mainFrame = mainFrame;
    }

    @Override
    public void onReceived(PacketReceivedEvent event) {
        final ServerChatPacket packet = event.getPacket();

        mainFrame.getChat().append("\n" + packet.getMessage().getFullText());
        mainFrame.getChat().setCaretPosition(mainFrame.getChat().getDocument().getLength());
    }
}
