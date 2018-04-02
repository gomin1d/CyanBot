package me.xjcyan1de.cyanbot;

import me.xjcyan1de.cyanbot.gui.MainFrame;
import me.xjcyan1de.cyanbot.logger.BotLogger;
import me.xjcyan1de.cyanbot.utils.Schedule;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Logger;


@SuppressWarnings("unchecked")
public class Test  {

    public static void main(String... arg) throws Throwable {

    }

    public static void worldInfo() {
                /*Schedule.timer(()->{
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
        }, 1000, 1000);*/
    }

    public static void joinBots() {
        final BotManager manager = Main.getManager();
        final MainFrame mainFrame = Main.getMainFrame();
        final Logger logger = mainFrame.getLogger();

        Schedule.timer(() -> {
            String name = "CyanBot" + new Random().nextInt(10000);
            System.out.println("Join " + name);
            final Bot bot = new Bot(manager, mainFrame,
                    new BotLogger(name, logger), name, "lgn.z904095z.bget.ru", 25565);
            manager.connectBot(bot, "lgn.z904095z.bget.ru:25565");
        }, 1000, 1000);
    }

    public static void test2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Collection<Integer> set = new TreeSet<>(Comparator.comparingInt(i->i));
        for (int i = 0; i < 1000; i++) {
            set.add(new Random().nextInt(1000));
        }

        System.out.println(set);

        long time = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            for (Integer integer : set) {

            }
        }

        System.out.println(System.currentTimeMillis() - time);
    }

    public static void test1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Collection<Integer> set = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            set.add(new Random().nextInt(1000));
        }

        long time = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            for (Integer integer : set) {

            }
        }

        System.out.println(System.currentTimeMillis() - time);
    }
}
