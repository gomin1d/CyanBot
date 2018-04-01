package me.xjcyan1de.cyanbot.listeners.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация эвента
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
    /**
     * Приоритет выполениния event'a относительно других
     * @return чем больше, тем позже выполнится
     * @see EventPriority
     */
    int priority() default EventPriority.NORMAL;

    /**
     * Если event реализрует Cancellable, то в случае отмены event'a, этот handler не будет вызван
     * @return тип игнорирования event'a
     */
    EventIgnore ignore() default EventIgnore.NOT_IGNORE;
}
