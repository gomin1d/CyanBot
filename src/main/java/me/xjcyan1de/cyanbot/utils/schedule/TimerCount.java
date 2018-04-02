package me.xjcyan1de.cyanbot.utils.schedule;

import java.util.TimerTask;

/**
 * Таймер, который выполняется указанное кол-во раз
 */
public class TimerCount extends TimerTask {
    private final int count;
    private int i = 0;

    private final Runnable runnable;


    /**
     * @param count сколько раз срабатывать таймеру
     */
    public TimerCount(Runnable runnable, int count) {
        this.count = count;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        try {
            runnable.run();
        } finally {
            if (++i >= count) {
                this.cancel();
            }
        }
    }
}
