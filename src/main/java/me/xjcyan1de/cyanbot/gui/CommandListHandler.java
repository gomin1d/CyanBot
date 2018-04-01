package me.xjcyan1de.cyanbot.gui;

import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.gui.commands.Command;
import me.xjcyan1de.cyanbot.gui.commands.CommandChat;
import me.xjcyan1de.cyanbot.gui.commands.CommandSpin;
import me.xjcyan1de.cyanbot.gui.commands.CommandWalk;

import javax.swing.*;

public class CommandListHandler {
    public static void createPanel(JPanel commandPanel, int selected, Bot... bots) {
        commandPanel.removeAll();
        final Command command = Command.commandList.get(selected);
        command.execute(commandPanel, bots);
        commandPanel.updateUI();
    }

    public static void init(DefaultListModel<String> listModel) {
        new CommandChat().register(listModel);
        new CommandWalk().register(listModel);
        new CommandSpin().register(listModel);
    }
}
