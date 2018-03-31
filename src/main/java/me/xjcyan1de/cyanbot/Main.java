package me.xjcyan1de.cyanbot;

import me.xjcyan1de.cyanbot.config.Config;
import me.xjcyan1de.cyanbot.gui.Hover;
import me.xjcyan1de.cyanbot.gui.MainFrame;
import me.xjcyan1de.cyanbot.logger.LoggerInstaller;
import me.xjcyan1de.cyanbot.utils.Schedule;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws Exception {
        setUTF8();

        final Logger logger = LoggerInstaller.create("CyanBot", "CyanBot.log");

        PlayerManager manager = new PlayerManager(logger);

        final Config config = new Config("config.yml");

        MainFrame mainFrame = new MainFrame(config, manager, logger);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null); // по центу экрана
        mainFrame.setVisible(true);
    }

    private static void setUTF8() throws NoSuchFieldException, IllegalAccessException {
        System.setProperty("file.encoding","UTF-8");
        Field charset = Charset.class.getDeclaredField("defaultCharset");
        charset.setAccessible(true);
        charset.set(null, null);
    }
}
