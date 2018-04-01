package me.xjcyan1de.cyanbot;

import me.xjcyan1de.cyanbot.config.Config;
import me.xjcyan1de.cyanbot.gui.MainFrame;
import me.xjcyan1de.cyanbot.logger.LoggerInstaller;
import me.xjcyan1de.cyanbot.utils.Schedule;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Main {
    private static MainFrame mainFrame;

    public static void main(String[] args) throws Exception {
        setUTF8();

        final Logger logger = LoggerInstaller.create("CyanBot", "CyanBot.log");

        BotManager manager = new BotManager(logger);

        final Config config = new Config("config.yml");

        Schedule.timer(()->{
            manager.getWorldMap().values()
                    .forEach(world -> {
                        System.out.println("world: ");
                        Map<Integer, Integer> map = new HashMap<>();
                        world.getChunkMap().values()
                                .forEach(chunk -> {
                                    final Integer integer = map.computeIfAbsent(chunk.getView().size(), ket -> 0);
                                    map.put(chunk.getView().size(), integer + 1);
                                });
                        map.forEach((count, size)->{
                            System.out.println("  " + count + ": " + size);
                        });
                    });
        }, 1000, 1000);

        mainFrame = new MainFrame(config, manager, logger);
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

    public static MainFrame getMainFrame() {
        return mainFrame;
    }
}
