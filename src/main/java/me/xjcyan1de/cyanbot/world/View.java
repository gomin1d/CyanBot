package me.xjcyan1de.cyanbot.world;

import me.xjcyan1de.cyanbot.Bot;

import java.util.HashSet;
import java.util.Set;

/**
 * то, что видят игроки
 */
public class View {
    private Set<Bot> bots = new HashSet<>(1); //view

    public void addView(Bot bot) {
        this.bots.add(bot);
    }

    public boolean removeView(Bot bot) {
        return this.bots.remove(bot);
    }

    public Set<Bot> getView() {
        return bots;
    }

    /**
     * Если на объект никто не смотрит
     */
    public boolean isEmptyView() {
        return bots.isEmpty();
    }

    public void setView(Set<Bot> bots) {
        this.bots = bots;
    }
}
