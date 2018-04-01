package me.xjcyan1de.cyanbot.listeners.event;

/**
 * Разные методы игорирования event'a
 */
public enum EventIgnore {
    /**
     * Игнорировать события, если отменено
     */
    IGNORE_CANCELLED{
        @Override
        public boolean hasIgnore(Cancellable cancellable) {
            return cancellable.isCancelled();
        }
    },
    /**
     * Игрорировать событие, если не отменено
     */
    IGNORE_NOT_CANCELLED {
        @Override
        public boolean hasIgnore(Cancellable cancellable) {
            return !cancellable.isCancelled();
        }
    },
    /**
     * Не игнорировать события
     */
    NOT_IGNORE {
        @Override
        public boolean hasIgnore(Cancellable cancellable) {
            return false;
        }
    },

    ;

    public abstract boolean hasIgnore(Cancellable cancellable);
}
