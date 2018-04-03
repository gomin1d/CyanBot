package me.xjcyan1de.cyanbot.gui;

import me.xjcyan1de.cyanbot.Main;

import javax.swing.*;
import java.awt.*;

public enum BotStatus {
    CONNECTED("Подключен", Color.decode("#4CAF50")),
    DISCONECTED("Не подключен", Color.decode("#FF5252"));

    private final String text;
    private final Color color;

    BotStatus(String text, Color color) {
        this.text = text;
        this.color = color;
    }

    public void setStatus() {
        final JTextField status = Main.getMainFrame().getStatus();
        status.setText(text);
        status.setBackground(color);
    }
}
