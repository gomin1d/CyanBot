package me.xjcyan1de.cyanbot.listeners.event;

/**
 * Базовый event Cancellable: если лень самому прописывать isCancelled, setCancelled, то наследуйтесь от этого класса
 */
public class BaseCancellable implements Cancellable {

    private boolean cancelled;

    public BaseCancellable() {
    }

    public BaseCancellable(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
