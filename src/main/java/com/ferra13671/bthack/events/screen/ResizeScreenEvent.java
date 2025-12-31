package com.ferra13671.bthack.events.screen;

import com.ferra13671.MegaEvents.event.Event;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResizeScreenEvent extends Event<ResizeScreenEvent> {
    public final int width;
    public final int height;
}
