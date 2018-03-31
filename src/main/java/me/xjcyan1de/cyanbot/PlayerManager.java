package me.xjcyan1de.cyanbot;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PlayerManager {
    private Map<String, Player> playerMap = new HashMap<>();
    private Logger logger;

    public PlayerManager(Logger logger) {
        this.logger = logger;
    }

    public void connectPlayer(Player player, String host, int ip) {
        if (!playerMap.containsKey(player.getUsername())) {
            playerMap.put(player.getUsername(), player);
            player.connectServer(host, ip);
        }
    }

    public Player getPlayer(String nameText) {
        return playerMap.get(nameText);
    }

    public void disconnectPlayer(Player player) {
        playerMap.remove(player.getUsername());
    }
}
