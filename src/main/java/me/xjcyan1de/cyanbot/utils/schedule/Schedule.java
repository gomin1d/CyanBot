package me.xjcyan1de.cyanbot.utils.schedule;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Schedule {

    private static Timer timer = new java.util.Timer();

    public static TimerTask later(TimerTask timerTask, long delay) {
        return later(timerTask, delay, TimeUnit.MILLISECONDS);
    }

    public static TimerTask later(Runnable runnable, long delay) {
        return later(runnable, delay, TimeUnit.MILLISECONDS);
    }

    public static TimerTask later(Runnable runnable, long delay, TimeUnit timeUnit) {
        return later(runnableToTimer(runnable), delay, timeUnit);
    }

    public static TimerTask later(TimerTask timerTask, long delay, TimeUnit timeUnit) {
        timer.schedule(
                timerTask,
                timeUnit.toMillis(delay)
        );
        return timerTask;
    }


    public static TimerTask timer(TimerTask timerTask, long delay, long period) {
        return timer(timerTask, delay, period, TimeUnit.MILLISECONDS);
    }

    public static TimerTask timer(Runnable runnable, long delay, long period) {
        return timer(runnable, delay, period, TimeUnit.MILLISECONDS);
    }

    public static Timer getTimer() {
        return timer;
    }

    public static TimerTask timer(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        return timer(runnableToTimer(runnable), delay, period, timeUnit);
    }

    private static TimerTask runnableToTimer(Runnable runnable) {
        return new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    public static TimerTask timer(TimerTask timerTask, long delay, long period, TimeUnit timeUnit) {
        timer.scheduleAtFixedRate(timerTask, timeUnit.toMillis(delay), timeUnit.toMillis(period));
        return timerTask;
    }

    public static void cancel(TimerTask timerTask) {
        if (timerTask != null) {
            try {
                timerTask.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}