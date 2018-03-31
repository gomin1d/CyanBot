package me.xjcyan1de.cyanbot;

import me.xjcyan1de.cyanbot.gui.MainFrame;
import me.xjcyan1de.cyanbot.logger.LoggerInstaller;

import java.io.IOException;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws IOException {
        final Logger logger = LoggerInstaller.create("CyanBot", "CyanBot.log");

        PlayerManager manager = new PlayerManager(logger);

        MainFrame dialog = new MainFrame(manager, logger);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
