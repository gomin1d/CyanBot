package me.xjcyan1de.cyanbot;

import me.xjcyan1de.cyanbot.config.Config;
import me.xjcyan1de.cyanbot.utils.Schedule;
import me.xjcyan1de.cyanbot.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BotManager {
    private ExecutorService service = Executors.newFixedThreadPool(2);

    private Map<String, Server> serverMap = new HashMap<>();

    private Map<String, Bot> botMap = new HashMap<>();
    private Logger logger;
    private Config config;

    public BotManager(Logger logger, Config config) {
        this.logger = logger;
        this.config = config;

        Schedule.timer(() -> {
            final Map<String, Bot> botMap = this.getBotMap();
            for (Bot bot : new ArrayList<>(botMap.values())) {
                if (bot.isClose()) {
                    botMap.remove(bot.getUsername());
                }
            }
        }, 1, 1, TimeUnit.SECONDS);

        Schedule.timer(()->{
            serverMap.values()
                    .stream()
                    .map(Server::getWorld)
                    .forEach(World::checkRemoveChunks);
        }, 10, 10, TimeUnit.SECONDS);
    }

    public Config getConfig() {
        return config;
    }

    public void connectBot(Bot bot, String ipText) {
        if (!botMap.containsKey(bot.getUsername())) {
            botMap.put(bot.getUsername(), bot);
            bot.setServer(serverMap.computeIfAbsent(ipText, key->new Server(new World())));
            service.submit(bot::startBot);
        }
    }

    public Bot getBot(String nameText) {
        return botMap.get(nameText);
    }

    public void disconnectBot(Bot bot) {
        botMap.remove(bot.getUsername());
        service.submit(()-> bot.getClient().getSession().disconnect("Final"));
    }

    public boolean isConnected(String name) {
        return botMap.containsKey(name) && !botMap.get(name).isClose();
    }

    public Map<String, Server> getServerMap() {
        return serverMap;
    }

    public Collection<Bot> getBots() {
        return botMap.values();
    }

    public Map<String, Bot> getBotMap() {
        return botMap;
    }
}
