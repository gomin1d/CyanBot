package me.xjcyan1de.cyanbot.handlers;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import me.xjcyan1de.cyanbot.Player;


public class ChatHandler extends SessionAdapter implements Handler {
    Player player;

    public ChatHandler(Player player) {
        this.player = player;
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onStartBot() {
        System.out.println("sss");
        player.getClient().getSession().addListener(this);
    }

    @Override
    public void packetReceived(PacketReceivedEvent event) {
        if (!(event.getPacket() instanceof ServerChatPacket)) return;
        ServerChatPacket packet = event.getPacket();
        final String message = packet.getMessage().getFullText();
        System.out.println(player.getUsername() + " <- " + message);
        onMessage(message);
    }

    public void onMessage(String message) {
        final String[] split = message.split(" ");
        int readFrom = -1;
        for (int i = 0; i < split.length; i++) {
            if (split[i].startsWith("CyanBot,")) {
                readFrom = i + 1 < split.length ? i + 1 : -1;
                break;
            }
        }
        if (readFrom != -1) {
            command(split, readFrom);
        }
    }

    public void command(String[] split, int readFrom) {
        String[] args = new String[split.length - readFrom];
        System.arraycopy(split, readFrom, args, 0, args.length);

        switch (args[0].toLowerCase()) {
            case "напиши":
            case "скажи": {
                if (args.length > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    sb.setLength(sb.length() - 1);
                    player.sendMessage(sb.toString());
                }
                break;
            }
            default: {
                // player.sendMessage("Аргументы: "+Arrays.toString(args));
            }
        }

    }
}
