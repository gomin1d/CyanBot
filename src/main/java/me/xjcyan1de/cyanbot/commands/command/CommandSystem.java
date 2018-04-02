package me.xjcyan1de.cyanbot.commands.command;

import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.events.CommandEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Система команд
 */
public class CommandSystem {
    private final Bot bot;
    private final Logger logger;

    private List<Command> commands = new ArrayList<>();

    public CommandSystem(Bot bot, Logger logger) {
        this.bot = bot;
        this.logger = logger;
    }

    public void registerCommand(Command command) {
        this.commands.add(command);
    }

    public void dispatchCommand(CommandEvent event) {
        commands.stream()
                .filter(command -> command.getName().equalsIgnoreCase(event.getCommand())
                        || command.getAliases().stream()
                        .anyMatch(alias -> event.getCommand().equalsIgnoreCase(alias)))
                .filter(command -> !command.isOnlyAccess() || event.hasAccessSender())
                .findFirst().ifPresent(command -> {
            try {
                command.execute(event);
            } catch (Exception e) {
                logger.severe("Ошибка при обработке команды " + event);
                e.printStackTrace();
            }
        });
    }
}
