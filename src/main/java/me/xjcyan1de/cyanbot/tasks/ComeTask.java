package me.xjcyan1de.cyanbot.tasks;

import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.utils.schedule.Schedule;

import java.util.TimerTask;

public class ComeTask extends TimerTask {

    private Bot bot;
    private String to;

    @Override
    public void run() {

    }

    public void start() {
        Schedule.getTimer().schedule(this, 50, 50);
    }
}
