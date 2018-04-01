package me.xjcyan1de.cyanbot.listeners.event;

/**
 * Отмена события
 */
public interface Cancellable {
    /**
     * Установка значение отмены события
     * @param cancel true, если отменить
     */
    void setCancelled(boolean cancel);

    /**
     * Отменено ли событие
     * @return true, если да
     */
    boolean isCancelled();
}
