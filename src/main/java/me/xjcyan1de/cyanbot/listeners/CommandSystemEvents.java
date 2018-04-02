package me.xjcyan1de.cyanbot.listeners;

import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.commands.command.CommandSystem;
import me.xjcyan1de.cyanbot.events.CommandEvent;
import me.xjcyan1de.cyanbot.listeners.event.EventHandler;
import me.xjcyan1de.cyanbot.listeners.event.EventIgnore;
import me.xjcyan1de.cyanbot.listeners.event.EventPriority;
import me.xjcyan1de.cyanbot.listeners.event.Listener;

/**
 * Обрабатывает команды
 */
public class CommandSystemEvents implements Listener {
    private Bot bot;
    private CommandSystem commandSystem;

    public CommandSystemEvents(Bot bot, CommandSystem commandSystem) {
        this.bot = bot;
        this.commandSystem = commandSystem;
    }

    @EventHandler(ignore = EventIgnore.CANCELLED, priority = EventPriority.HIGHEST)
    public void on(CommandEvent event) {
        commandSystem.dispatchCommand(event);
    }
}
