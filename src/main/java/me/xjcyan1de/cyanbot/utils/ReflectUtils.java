package me.xjcyan1de.cyanbot.utils;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public class ReflectUtils {

    private static MethodHandles.Lookup lookup = MethodHandles.lookup();

    /**
     * Засунуть вызов метода в consumer
     *
     * @param impl   объект, в котором находится метод
     * @param method метод
     * @return consumer
     */
    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> createConsumer(Object impl, Method method) {
        final Class<?> firstParam = method.getParameterTypes()[0];
        MethodType consumeString = MethodType.methodType(void.class, firstParam);
        return Try.unchecked(() -> (Consumer<T>) LambdaMetafactory.metafactory(lookup, "accept",
                MethodType.methodType(Consumer.class, impl.getClass()),
                consumeString.changeParameterType(0, Object.class),
                lookup.unreflect(method), consumeString).getTarget().invoke(impl));
    }
}
