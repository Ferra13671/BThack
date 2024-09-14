package com.ferra13671.BThack.Events.Events.Render;

import com.ferra13671.BThack.Events.MegaEvents.Event;

public class RenderSpyglassEvent extends Event {

    public float scale;

    public RenderSpyglassEvent(float scale) {
        this.scale = scale;
    }
}
