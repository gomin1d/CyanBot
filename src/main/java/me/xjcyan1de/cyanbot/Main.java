package me.xjcyan1de.cyanbot;

import me.xjcyan1de.cyanbot.utils.Schedule;

import java.net.Proxy;

public class Main {
    private static final Proxy PROXY = Proxy.NO_PROXY;
    private static final Proxy AUTH_PROXY = Proxy.NO_PROXY;

    public static void main(String[] args) {
        Player player = new Player("CyanBot");
        player.startBot("mc.justvillage.ru", 25565);
        Schedule.later(() -> {
            player.sendMessage("/login test123");
        }, 1000);
    }
}
