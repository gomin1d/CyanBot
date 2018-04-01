package me.xjcyan1de.cyanbot.listeners.event;

import com.sun.istack.internal.NotNull;
import me.xjcyan1de.cyanbot.utils.ReflectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Система эвентов
 */
public class EventSystem {

    private Logger logger;

    private List<RegisterEvent> events = new ArrayList<>();

    public EventSystem(Logger logger) {
        this.logger = logger;
    }

    /**
     * Зарегистрировать ивенты
     *
     * @param listener класс с EventHandler'aми
     */
    public void registerLisneter(@NotNull Listener listener) {
        final int prevSize = events.size();
        for (Method method : listener.getClass().getMethods()) {
            final EventHandler eventHandler = method.getAnnotation(EventHandler.class);
            if (eventHandler != null) {
                try {
                    if (method.getParameterCount() != 1) {
                        throw new IllegalArgumentException("Количетсво аргументов в методе-event'е " + method + " должно быть равно 1");
                    }
                    if (Modifier.isStatic(method.getModifiers())) {
                        throw new IllegalArgumentException("Метод-event должен быть не статическим " + method);
                    }

                    final Consumer<Object> consumer = ReflectUtils.createConsumer(listener, method);

                    events.add(new RegisterEvent(method.getParameterTypes()[0], consumer, listener, eventHandler, method));
                    System.out.println("REG: " + method);
                } catch (Exception e) {
                    logger.severe("Ошибка регистрации эвента " + method);
                    e.printStackTrace();
                }
            }
        }
        if (prevSize != events.size()) {
            // сортируем по приоритету, чем больше приоритет, тем позже выполнится
            events.sort(Comparator.comparingInt(o -> o.getHandler().priority()));
        }
    }

    /**
     * Удалить зарегистрированные ивенты
     *
     * @param listener класс с EventHandler'aми
     */
    public void unregisterLisneter(@NotNull Object listener) {
        events.removeIf(registerEvent -> registerEvent.getListener().equals(listener));
    }

    /**
     * Зарегистрированные ивенты
     *
     * @return дата-класс с листенером
     */
    public Collection<RegisterEvent> getRegisterEvents() {
        return events;
    }

    /**
     * Зарегистрированные классы с EventHandler'aми
     */
    public List<Object> getListeners() {
        return events.stream()
                .map(RegisterEvent::getListener)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <T> T callEvent(T event) {
        Objects.requireNonNull(event, "event не может быть null");
        final boolean cancellable = event instanceof Cancellable;

        for (RegisterEvent registerEvent : events) {
            if (registerEvent.getClassEvent().isInstance(event)) {
                if (cancellable && registerEvent.getHandler().ignore().hasIgnore((Cancellable) event)) {
                    continue;
                }
                try {
                    registerEvent.getConsumer().accept(event);
                } catch (Throwable e) {
                    logger.severe("Ошибка оработки event'a " + registerEvent.getMethod());
                    e.printStackTrace();
                }
            }
        }
        return event;
    }

    /**
     * Зарегистрированный event
     */
    public static class RegisterEvent {
        private final Class classEvent;
        private final Consumer consumer;
        private final Listener listener;
        private final EventHandler handler;
        private final Method method;

        public RegisterEvent(Class classEvent, Consumer consumer, Listener listener, EventHandler handler, Method method) {
            this.classEvent = classEvent;
            this.consumer = consumer;
            this.listener = listener;
            this.handler = handler;
            this.method = method;
        }

        public Class getClassEvent() {
            return classEvent;
        }

        public Consumer getConsumer() {
            return consumer;
        }

        public Listener getListener() {
            return listener;
        }

        public Method getMethod() {
            return method;
        }

        public EventHandler getHandler() {
            return handler;
        }
    }
}
