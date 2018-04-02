package me.xjcyan1de.cyanbot;

import me.xjcyan1de.cyanbot.config.Config;
import me.xjcyan1de.cyanbot.gui.MainFrame;
import me.xjcyan1de.cyanbot.logger.BotLogger;
import me.xjcyan1de.cyanbot.utils.schedule.Schedule;

import java.util.*;
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
            for (Bot bot : new ArrayList<>(botMap.values())) {
                if (bot.isClose()) {
                    logger.severe("Бот " + bot.getUsername() + " закрыл соедиение, удаляем его.");
                    this.onDisconnectBot(bot);
                }
            }
        }, 1, 1, TimeUnit.SECONDS);

        Schedule.timer(() -> {
            serverMap.values().forEach(this::checkReleaseResources);
        }, 10, 10, TimeUnit.SECONDS);
    }

    public Config getConfig() {
        return config;
    }

    public void connectBot(String ipText, String name, List<String> replaceCommand, MainFrame mainFrame) {
        service.submit(() -> {
            if (!botMap.containsKey(name)) {
                try {
                    final String[] ipPort = ipText.split(":");
                    Server server = serverMap.computeIfAbsent(ipText, key -> new Server(logger, ipText, this));
                    final Bot bot = new Bot(this, mainFrame, new BotLogger(name, logger), name,
                            ipPort[0], ipPort.length > 1 ? Integer.parseInt(ipPort[1]) : 25565,
                            server);
                    bot.setJoinCommands(replaceCommand);
                    botMap.put(bot.getUsername(), bot);
                    bot.startBot();
                } catch (Exception e) {
                    logger.severe("Ошибка при добавлении бота");
                    e.printStackTrace();
                }
            }
        });
    }

    public Bot getBot(String nameText) {
        return botMap.get(nameText);
    }

    public void onDisconnectBot(Bot bot) {
        Schedule.later(() -> disconnectBot(bot), 50);

        botMap.remove(bot.getUsername());
        bot.getWorld().onDisconnect(bot);

        this.checkReleaseResources(bot.getServer());
    }

    private void checkReleaseResources(Server server) {
        if (server.getBots().isEmpty()) {
            server.getWorld().clearAll();
        } else {
            server.getWorld().checkReleaseResources();
        }
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

    public void disconnectBot(Bot bot) {
        service.submit(() -> {
            if (!bot.isClose()) {
                bot.getClient().getSession().disconnect("Final");
            }
        });
    }
}
