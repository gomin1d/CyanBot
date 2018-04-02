package me.xjcyan1de.cyanbot.gui;

import com.intellij.uiDesigner.core.GridLayoutManager;
import me.xjcyan1de.cyanbot.gui.commands.*;

import javax.swing.*;
import java.awt.*;

public class CommandListHandler {
    public static void createPanel(JPanel commandPanel, int selected) {
        commandPanel.removeAll();
        final Command command = Command.commandList.get(selected);
        commandPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        command.initPanel(commandPanel);
        commandPanel.updateUI();
    }

    public static void init(DefaultListModel<String> listModel) {
        new CommandChat().register(listModel);
        new CommandWalk().register(listModel);
        new CommandSpin().register(listModel);
        new CommandSelectSlot().register(listModel);
    }
}
