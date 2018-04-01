package me.xjcyan1de.cyanbot.events;

import me.xjcyan1de.cyanbot.Bot;

public class CommandEvent {
    private Bot bot;
    private String command;
    private String[] args;

    public CommandEvent(Bot bot, String command, String[] args) {
        this.bot = bot;
        this.command = command;
        this.args = args;
    }

    public Bot getBot() {
        return bot;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }

    public String arg(int index) {
        return args[0];
    }

    public boolean has(int index, String... equal) {
        if (index >= args.length) {
            return false;
        }
        for (String anEqual : equal) {
            if (anEqual.equalsIgnoreCase(args[index])) {
                return true;
            }
        }
        return false;
    }
}
