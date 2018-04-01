package me.xjcyan1de.cyanbot.listeners;

import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.BotManager;

import java.util.logging.Logger;

public class CloseConnectionListener extends SessionAdapter {

    private Bot bot;
    private BotManager botManager;
    private Logger logger;

    public CloseConnectionListener(Bot bot, BotManager botManager, Logger logger) {
        this.bot = bot;
        this.botManager = botManager;
        this.logger = logger;
    }

    @Override
    public void disconnecting(DisconnectingEvent event) {
        botManager.disconnectBot(bot);
        logger.info("Disconnect: " + event.getReason() + (event.getCause() != null ? (
                "(" + event.getCause().getClass().getName() + ": " + event.getCause().getMessage() + ")"
                ) : ""));
    }
}
