package me.xjcyan1de.cyanbot.gui.commands;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.gui.joystick.PointChangeEvent;
import me.xjcyan1de.cyanbot.gui.joystick.SimpleJoystick;
import me.xjcyan1de.cyanbot.utils.Schedule;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.TimerTask;

public class CommandWalk extends Command {

    private SimpleJoystick joystick;
    private double x;
    private double z;
    private HashMap<Player, TimerTask> walkTaskMap = new HashMap<>();

    public CommandWalk() {
        super("Идти");
    }

    @Override
    public void execute(JPanel commandPanel, Player... players) {
        joystick = new SimpleJoystick(150);
        joystick.setPreferredSize(new Dimension(100, 100));
        joystick.addChangeListener(e -> {
            final PointChangeEvent event = (PointChangeEvent) e;
            final Point p = event.p;
            double tempX = -p.getX() / 800;
            double tempZ = p.getY() / 800; //Шобы понимать пространство майна - z

            x = tempX;
            z = tempZ;


            if ((-0.0001 < x && x < 0.0001) && (-0.0001 < z && z < 0.0001)) {
                for (Player player : players) {
                    TimerTask walkTask = walkTaskMap.get(player);
                    if (walkTask != null) {
                        walkTask.cancel();
                    }
                    walkTaskMap.put(player, walkTask);
                }
            } else {
                for (Player player : players) {
                    TimerTask walkTask = walkTaskMap.get(player);
                    if (walkTask == null) {
                        walkTask = Schedule.timer(() -> {
                            player.getLoc().add(x, 0, z);
                        }, 50, 50);
                    }
                    walkTaskMap.put(player, walkTask);
                }
            }

        });

        commandPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        commandPanel.add(joystick, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }
}
