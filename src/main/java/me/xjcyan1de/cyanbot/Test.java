package me.xjcyan1de.cyanbot;

import me.xjcyan1de.cyanbot.utils.ReflectUtils;

import java.io.PrintStream;
import java.lang.invoke.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntBinaryOperator;


@SuppressWarnings("unchecked")
public class Test  {

    public static void main(String... arg) throws Throwable {

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
