package me.xjcyan1de.cyanbot.handlers;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.utils.Schedule;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class ChatHandler extends SessionAdapter implements Handler {
    Player player;
    List<String> accessPatterns = new LinkedList<>();
    String accessKey;

    public ChatHandler(Player player) {
        this.player = player;
        Schedule.later(this::generateAccessKey, 3000);
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onStartBot() {
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
            if (split[i].startsWith(player.getUsername() + ",")) {
                readFrom = i + 1 < split.length ? i + 1 : -1;
                break;
            }
        }
        if (readFrom != -1) {
            command(message, split, readFrom);
        }
    }

    public void command(String senderPattern, String[] split, int readFrom) {
        String[] args = new String[split.length - readFrom];
        System.arraycopy(split, readFrom, args, 0, args.length);

        if (!hasAccess(senderPattern)) {
            if ((args.length == 2 && args[0].equals("ключ") && args[1].equals(accessKey)) ||
                    args.length == 1 && args[0].equals(accessKey)) {
                int totalLenght = 0;
                for (String arg : args) totalLenght += arg.length();
                final String pattern = senderPattern.substring(0, senderPattern.length() - totalLenght - 1);
                accessPatterns.add(pattern);
                System.out.println("Добавлен патерн: " + pattern);
                player.sendMessage("Успешная авторизация!");
                generateAccessKey();
            }
        } else {
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
                case "deus": {
                    if (args.length == 2 && args[1].equalsIgnoreCase("vult")) {
                        player.sendMessage("Ave Maria!");
                    }
                    break;
                }
                case "слава": {
                    if (args.length == 2 && args[1].equalsIgnoreCase("украине")) {
                        player.sendMessage("ГЕРОЯМ СЛАВА!");
                    }
                    break;
                }
                default: {
                    // player.sendMessage("Аргументы: "+Arrays.toString(args));
                }
            }
        }
    }

    public boolean hasAccess(String pattern) {
        return accessPatterns.stream().anyMatch(pattern::startsWith);
    }

    public String generateAccessKey() {
        Random random = new Random();
        this.accessKey = String.valueOf(1000 + random.nextInt(8999));
        System.out.println("Сгенерирован новый ключ: " + accessKey);
        player.sendMessage("/tell XjCyan1de Ключ: " + accessKey);
        return accessKey;
    }
}
