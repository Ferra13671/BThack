package com.ferra13671.bthack.events;

import com.ferra13671.MegaEvents.event.Event;
import com.ferra13671.bthack.utils.KeyAction;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KeyInputEvent extends Event<KeyInputEvent> {
    public final int key;
    public final KeyAction action;
}
