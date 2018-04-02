package me.xjcyan1de.cyanbot.commands.command;

import me.xjcyan1de.cyanbot.events.CommandEvent;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Команда, которую может выполнять бот
 */
public abstract class Command {
    private String name;
    private boolean onlyAccess;
    private List<String> aliases;

    /**
     * Команда, которую может выполнять бот
     * @param name имя команды
     * @param onlyAccess true, если команду обрабатывать только для авторизированных пользователей
     * @param aliases алиасы команды
     */
    public Command(String name, boolean onlyAccess, String... aliases) {
        this.name = name;
        this.onlyAccess = onlyAccess;
        this.aliases = Arrays.asList(aliases);
    }

    public abstract void execute(CommandEvent event);

    public String getName() {
        return name;
    }

    public boolean isOnlyAccess() {
        return onlyAccess;
    }

    public List<String> getAliases() {
        return aliases;
    }
}
