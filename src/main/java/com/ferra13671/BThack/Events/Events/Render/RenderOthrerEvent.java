package com.ferra13671.BThack.Events.Events.Render;

import com.ferra13671.BThack.Events.MegaEvents.Event;
import com.ferra13671.BThack.Events.MegaEvents.EventModifiers.EventPhase;

public class RenderOthrerEvent extends Event {

    public final OtherElement element;

    public RenderOthrerEvent(OtherElement element) {
        super(EventPhase.PRE);
        this.element = element;
    }

    public RenderOthrerEvent(OtherElement element, EventPhase phase) {
        super(phase);
        this.element = element;
    }

    public enum OtherElement {
        NAUSEA,
        OVERLAY,
        SCREEN
    }
}
