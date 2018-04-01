package me.xjcyan1de.cyanbot.gui.commands;

import me.xjcyan1de.cyanbot.Player;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public abstract class Command {
    public static List<Command> commandList = new LinkedList<>();
    public String title;

    public Command(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public abstract void execute(JPanel jPanel, Player player);

    public void register(DefaultListModel<String> listModel) {
        listModel.addElement(getTitle());
        commandList.add(this);
    }
}
