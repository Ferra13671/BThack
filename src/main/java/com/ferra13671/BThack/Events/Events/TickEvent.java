package com.ferra13671.BThack.Events.Events;

import com.ferra13671.BThack.Events.MegaEvents.Event;

public class TickEvent extends Event {
    public final Type type;

    public TickEvent(Type type) {
        this.type = type;
    }

    public static class ClientTickEvent extends TickEvent {

        public ClientTickEvent() {
            super(Type.CLIENT);
        }
    }

    public static class WorldTickEvent extends TickEvent {

        public WorldTickEvent() {
            super(Type.WORLD);
        }
    }

    public enum Type {
        WORLD,
        CLIENT
    }
}
