package me.xjcyan1de.cyanbot.handlers;

/**
 * Логика бота
 */
public interface Handler {

    void onUpdate();

    /**
     * Нужно ли срабатывать
     * @return true, если да
     */
    default boolean canRun() {
        return true;
    }

    void onStartBot();
}
