package com.ferra13671.BThack.Events.MegaEvents;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bebra_tyan(Blue_tyan)
 * @version 1
 */

public class EventBus {
    private final HashMap<Object, List<Method>> registeredMap = new HashMap<>();

    public static final boolean debugLogging = false;

    public void register(Object object) {

        registeredMap.put(object, Arrays.stream(object.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(EventSubscriber.class))
                .sorted(Comparator.comparing(method -> method.getAnnotation(EventSubscriber.class).priority()))
                .collect(Collectors.toList())
        );

        if (debugLogging) {
            MegaEvents.log("Registered '" + object + "' to the event manager.");
        }
    }

    public void unregister(Object object) {

        registeredMap.remove(object);

        if (debugLogging) {
            MegaEvents.log("Unregistered '" + object + "' from the event manager.");
        }
    }

    public <T extends Event> void activate(T event) {
        if (event.isCancelled()) return;

        HashMap<Object, List<Method>> registeredMap = this.registeredMap;

        try {
            registeredMap.forEach((object, methods) -> methods
                    .forEach(method -> {
                        if (method.getParameterTypes()[0] != event.getClass()) return;

                        try {
                            method.invoke(object, event);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            if (debugLogging) {
                                MegaEvents.log("Something went wrong when activation the event '" + event + "' with the phase '" + event.getPhase());
                            }

                            e.printStackTrace();
                        }
                    })
            );
        } catch (Exception ignored) {}

        if (debugLogging) {
            MegaEvents.log("Successfully activated '" + event + "' with the phase '" + event.getPhase() + "'!");
        }
    }

    public <T> T activate(T event) {

        HashMap<Object, List<Method>> registeredMap = this.registeredMap;

        try {
            registeredMap.forEach((object, methods) -> methods
                    .forEach(method -> {
                        if (method.getParameterTypes()[0] != event.getClass()) return;

                        try {
                            method.invoke(object, event);
                        } catch (IllegalAccessException | InvocationTargetException exception) {
                            if (debugLogging) {
                                MegaEvents.log("Something went wrong when activation an event.");
                            }

                            exception.printStackTrace();
                        }
                    })
            );
        } catch (Exception ignored) {}
        return event;
    }
}
