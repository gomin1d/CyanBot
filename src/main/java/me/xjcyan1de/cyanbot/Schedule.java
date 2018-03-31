package me.xjcyan1de.cyanbot;

import java.util.TimerTask;

public class Schedule {

    public static void later(Runnable runnable, long time) {
        new java.util.Timer().schedule(
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
        new java.util.Timer().scheduleAtFixedRate(timerTask, delay, period);
        return timerTask;
    }
}