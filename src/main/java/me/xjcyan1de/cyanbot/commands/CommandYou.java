package me.xjcyan1de.cyanbot.commands;

import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.commands.command.Command;
import me.xjcyan1de.cyanbot.events.CommandEvent;

import java.util.List;

/**
 * Команда "ты"
 */
public class CommandYou extends Command {

    public CommandYou() {
        super("ты", false);
    }

    @Override
    public void execute(CommandEvent event) {
        final Bot bot = event.getBot();
        if (event.has(0, "чей", "чей?")) {
            final List<String> players = bot.getAccessPlayers();
            if (players.isEmpty()) {
                bot.sendMessage("Бог мне хозяин");
            } else if (players.size() == 1) {
                bot.sendMessage("мой хозяин " + players.get(0));
            } else {
                bot.sendMessage("мои хозяины " + String.join(", ", players));
            }
        } else if(event.has(0, "кто")) {
            bot.sendMessage("я бот " + bot.getUsername() + ", а ты кто ебать?");
        }
    }
}
