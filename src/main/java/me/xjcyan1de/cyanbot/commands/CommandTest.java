package me.xjcyan1de.cyanbot.commands;

import me.xjcyan1de.cyanbot.commands.command.Command;
import me.xjcyan1de.cyanbot.events.CommandEvent;

/**
 * Команда для тестов
 */
public class CommandTest extends Command {

    public CommandTest() {
        super("test", true);
    }

    @Override
    public void execute(CommandEvent event) {
        event.getBot().sendMessage(event.getSender() + " проверка");
    }
}
