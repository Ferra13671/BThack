package com.ferra13671.bthack.events;

import com.ferra13671.MegaEvents.event.Event;
import com.ferra13671.bthack.utils.KeyAction;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MouseInputEvent extends Event<MouseInputEvent> {
    public final int button;
    public final KeyAction action;
}
