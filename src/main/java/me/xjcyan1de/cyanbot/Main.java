package me.xjcyan1de.cyanbot;

import me.xjcyan1de.cyanbot.gui.MainFrame;
import me.xjcyan1de.cyanbot.logger.LoggerInstaller;
import me.xjcyan1de.cyanbot.utils.Schedule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws Exception {
        setUTF8();


        final Logger logger = LoggerInstaller.create("CyanBot", "CyanBot.log");

        PlayerManager manager = new PlayerManager(logger);

        MainFrame mainFrame = new MainFrame(manager, logger);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private static void setUTF8() throws NoSuchFieldException, IllegalAccessException {
        System.setProperty("file.encoding","UTF-8");
        Field charset = Charset.class.getDeclaredField("defaultCharset");
        charset.setAccessible(true);
        charset.set(null,null);
    }
}
