package me.xjcyan1de.cyanbot.tasks;

import me.xjcyan1de.cyanbot.Bot;

public abstract class Task {

    private Bot bot;

    public Task(Bot bot) {
        this.bot = bot;
    }

    public Bot getBot() {
        return bot;
    }

    public abstract void run(String[] args);
}
