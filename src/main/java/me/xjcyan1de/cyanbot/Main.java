package me.xjcyan1de.cyanbot;

import me.xjcyan1de.cyanbot.config.Config;
import me.xjcyan1de.cyanbot.gui.MainFrame;
import me.xjcyan1de.cyanbot.logger.LoggerInstaller;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class Main {
    private static MainFrame mainFrame;
    private static BotManager manager;

    public static void main(String[] args) throws Throwable {
        setUTF8();

        final Logger logger = LoggerInstaller.create("CyanBot", "CyanBot.log");

        manager = new BotManager(logger);

        final Config config = new Config("config.yml");

        mainFrame = new MainFrame(config, manager, logger);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null); // по центу экрана
        mainFrame.setVisible(true);

        try {
            Test.main();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private static void setUTF8() throws NoSuchFieldException, IllegalAccessException {
        System.setProperty("file.encoding","UTF-8");
        Field charset = Charset.class.getDeclaredField("defaultCharset");
        charset.setAccessible(true);
        charset.set(null, null);
    }

    public static MainFrame getMainFrame() {
        return mainFrame;
    }

    public static BotManager getManager() {
        return manager;
    }
}
