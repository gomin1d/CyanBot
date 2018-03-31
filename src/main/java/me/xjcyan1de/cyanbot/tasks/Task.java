package me.xjcyan1de.cyanbot.tasks;

import me.xjcyan1de.cyanbot.Player;

public abstract class Task {

    private Player player;

    public Task(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract void run(String[] args);
}
