package me.xjcyan1de.cyanbot;

import me.xjcyan1de.cyanbot.gui.MainFrame;
import me.xjcyan1de.cyanbot.logger.LoggerInstaller;
import me.xjcyan1de.cyanbot.utils.Schedule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws IOException {
        final Logger logger = LoggerInstaller.create("CyanBot", "CyanBot.log");

        PlayerManager manager = new PlayerManager(logger);

        MainFrame mainFrame = new MainFrame(manager, logger);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
