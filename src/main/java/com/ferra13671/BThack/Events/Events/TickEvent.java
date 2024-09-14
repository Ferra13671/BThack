package com.ferra13671.BThack.Events.Events;

import com.ferra13671.BThack.Events.MegaEvents.Event;

public class TickEvent extends Event {
    public final Type type;
    public final Phase phase;

    public TickEvent(Type type, Phase phase) {
        this.type = type;
        this.phase = phase;
    }

    /*
    public static class PlayerTickEvent extends TickEvent {
        public final Player player;

        public PlayerTickEvent(Phase phase, Player player) {
            super(Type.PLAYER, phase);
            this.player = player;
        }
    }

     */

    public static class ClientTickEvent extends TickEvent {

        public ClientTickEvent(Phase phase) {
            super(Type.CLIENT, phase);
        }
    }

    public static class WorldTickEvent extends TickEvent {

        public WorldTickEvent(Phase phase) {
            super(Type.WORLD, phase);
        }
    }

    public enum Phase {
        START,
        END;

        Phase() {
        }
    }

    public enum Type {
        WORLD,
        PLAYER,
        CLIENT,
        SERVER,
        RENDER;

        Type() {
        }
    }
}
