package me.xjcyan1de.cyanbot.utils;

import java.util.Timer;
import java.util.TimerTask;

public class Schedule {

    private static Timer timer = new java.util.Timer();

    public static void later(Runnable runnable, long time) {
        timer.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        runnable.run();
                    }
                },
                time
        );
    }

    public static TimerTask timer(Runnable runnable, long delay, long period) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
        timer.scheduleAtFixedRate(timerTask, delay, period);
        return timerTask;
    }
}