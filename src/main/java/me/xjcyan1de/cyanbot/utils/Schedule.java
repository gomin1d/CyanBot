package me.xjcyan1de.cyanbot.utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Schedule {

    private static Timer timer = new java.util.Timer();

    public static TimerTask later(Runnable runnable, long delay) {
        return later(runnable, delay, TimeUnit.MILLISECONDS);
    }

    public static TimerTask later(Runnable runnable, long delay, TimeUnit timeUnit) {
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
        timer.schedule(
                timerTask,
                timeUnit.toMillis(delay)
        );
        return timerTask;
    }

    public static TimerTask timer(Runnable runnable, long delay, long period) {
        return timer(runnable, delay, period, TimeUnit.MILLISECONDS);
    }

    public static Timer getTimer() {
        return timer;
    }

    public static TimerTask timer(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
        timer.scheduleAtFixedRate(timerTask, timeUnit.toMillis(delay), timeUnit.toMillis(period));
        return timerTask;
    }
}