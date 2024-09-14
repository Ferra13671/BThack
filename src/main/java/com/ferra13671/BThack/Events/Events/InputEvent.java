package com.ferra13671.BThack.Events.Events;

import com.ferra13671.BThack.Events.MegaEvents.Event;

public class InputEvent extends Event {
    protected int key;
    public InputEvent() {
    }

    public static class KeyInputEvent extends InputEvent {
        private final Action action;

        public KeyInputEvent(int key, Action action) {
            this.key = key;
            this.action = action;
        }

        public Action getAction() {
            return this.action;
        }

        public int getKey() {
            return key;
        }

        public enum Action {
            PRESS,
            RELEASE,
            JAMMED
        }
    }

    public static class MouseInputEvent extends InputEvent {
        public MouseInputEvent() {
        }
    }
}