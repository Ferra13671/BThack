package com.ferra13671.BThack.Events.MegaEvents;

import com.ferra13671.BThack.Events.MegaEvents.EventModifiers.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Bebra_tyan(Blue_tyan)
 * @version 1
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventSubscriber {
    int priority() default EventPriority.DEFAULT;
}