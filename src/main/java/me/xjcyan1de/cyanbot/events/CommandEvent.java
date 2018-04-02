package me.xjcyan1de.cyanbot.events;

import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.listeners.event.BaseCancellable;

import java.util.Arrays;

/**
 * Игрок пишет боту команду
 */
public class CommandEvent extends BaseCancellable {
    private Bot bot;
    private String sender;
    private String command;
    private String[] args;

    public CommandEvent(Bot bot, String sender, String command, String[] args) {
        this.bot = bot;
        this.sender = sender;
        this.command = command;
        this.args = args;
    }

    public Bot getBot() {
        return bot;
    }

    /**
     * Имя команды
     * @return имя (если игрок напишет "CyanBot скажи лох", тогда команда - "скажи")
     */
    public String getCommand() {
        return command;
    }

    /**
     * Аргументы команды
     * @return аргументы (если игрок напишет "CyanBot скажи лох пидор", тогда аргументы - "лох пидор")
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * Получить аргумент по индексу
     * @param index индекс (с нуля)
     * @return аргумент
     */
    public String arg(int index) {
        return args[0];
    }

    /**
     * <p>Проверить аргумент по индексу</p>
     * <p>Например если аргумент под index 0 -> "скажи":</p>
     * <ul>
     *     <li>has(0, "скажи") -> true</li>
     *     <li>has(0, "скажи", "говори") -> true</li>
     *     <li>has(0, "лолкек") -> false</li>
     * </ul>
     * <p><b>Проверка идет не учитывая регистр</b></p>
     * <p><b>Вместо <code>IndexOutOfBoundsException</code> метод просто вернет <code>null</code></b></p>
     * @param index индекс (с нуля)
     * @param equal разные варииации аргумента
     */
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

    /**
     * Если ли игрока доступ к боту
     * @return true, если да
     */
    public boolean hasAccessSender() {
        return bot.hasAccess(this.sender);
    }

    /**
     * <p>Ник отправителя сообщения</p>
     * @return ник
     */
    public String getSender() {
        return sender;
    }

    /**
     * Получить аргументы команды через пробел
     * @param start индекс первого элемента
     */
    public String getArguments(int start) {
        StringBuilder s = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            s.append(i == start ? args[i] : ' ' + args[i]);
        }
        return s.toString();
    }

    /**
     * <p>Проверить соответсвие команды</p>
     * <p><b>Проверка идет не учитывая регистр</b></p>
     * @param command команда
     */
    public boolean hasCommand(String command) {
        return this.command.equalsIgnoreCase(command);
    }

    @Override
    public String toString() {
        return "CommandEvent{" +
                "bot=" + bot +
                ", sender='" + sender + '\'' +
                ", command='" + command + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }

    /**
     * Если ли указанное количество аргументов
     */
    public boolean hasSize(int size) {
        return args.length >= size;
    }
}
