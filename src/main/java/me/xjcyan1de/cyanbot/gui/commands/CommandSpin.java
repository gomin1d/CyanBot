package me.xjcyan1de.cyanbot.gui.commands;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.utils.Schedule;
import me.xjcyan1de.cyanbot.world.Location;

import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;

public class CommandSpin extends Command {

    private final JButton enableSpin;
    private final JSlider sliderSpin;
    private final JTextField valueSpin;
    private boolean enabled = false;
    private TimerTask timerTask;

    public CommandSpin() {
        super("Вращение");
        enableSpin = new JButton();
        enableSpin.setAlignmentX(1.0f);
        enableSpin.setDefaultCapable(true);
        enableSpin.setDoubleBuffered(false);
        enableSpin.setHorizontalTextPosition(0);
        enableSpin.setMargin(new Insets(10, 13, 2, 14));
        enableSpin.setText("Включить/Выключить");
        sliderSpin = new JSlider();
        sliderSpin.setMaximum(50);
        sliderSpin.setMinimum(-50);
        sliderSpin.setValue(0);
        sliderSpin.setToolTipText("Скорость вращения");
        valueSpin = new JTextField();
        valueSpin.setText("0");
    }

    @Override
    public void execute(JPanel commandPanel, Player player) {
        commandPanel.getRootPane().setDefaultButton(enableSpin);
        commandPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        commandPanel.add(enableSpin, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        commandPanel.add(sliderSpin, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        commandPanel.add(valueSpin, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));

        sliderSpin.addChangeListener(e -> {
            valueSpin.setText(String.valueOf(sliderSpin.getValue()));
        });

        valueSpin.addActionListener(e -> {
            final String actionCommand = e.getActionCommand();
            int value = 0;
            try {
                value = Integer.parseInt(actionCommand);
            } catch (NumberFormatException ignored) {
            }
            sliderSpin.setValue(value);
        });

        setListener(enableSpin, e -> {
            enabled = !enabled;
            if (enabled) {
                timerTask = Schedule.timer(() -> {
                    final Location loc = player.getLoc();
                    loc.setYaw(loc.getYaw() + sliderSpin.getValue());
                }, 0, 50);
            } else {
                if (timerTask != null) {
                    timerTask.cancel();
                }
            }
        });
    }
}
