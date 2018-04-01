package me.xjcyan1de.cyanbot.gui;

import me.xjcyan1de.cyanbot.gui.commands.*;

import javax.swing.*;

public class CommandListHandler {
    public static void createPanel(JPanel commandPanel, int selected) {
        commandPanel.removeAll();
        final Command command = Command.commandList.get(selected);
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
