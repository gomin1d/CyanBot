package me.xjcyan1de.cyanbot.listeners;

import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.PlayerManager;

import java.util.logging.Logger;

public class CloseConnectionListener extends SessionAdapter {

    private Player player;
    private PlayerManager playerManager;
    private Logger logger;

    public CloseConnectionListener(Player player, PlayerManager playerManager, Logger logger) {
        this.player = player;
        this.playerManager = playerManager;
        this.logger = logger;
    }

    @Override
    public void disconnecting(DisconnectingEvent event) {
        playerManager.disconnectPlayer(player);
        logger.info("Disconnect: " + event.getReason() + (event.getCause() != null ? (
                "(" + event.getCause().getClass().getName() + ": " + event.getCause().getMessage() + ")"
                ) : ""));
    }
}
