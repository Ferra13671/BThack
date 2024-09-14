package com.ferra13671.BThack.Events.Events.Render;

import com.ferra13671.BThack.Events.MegaEvents.Event;

public class RenderPortalEvent extends Event {

    public float nauseaStrength;

    public RenderPortalEvent(float nauseaStrength) {
        this.nauseaStrength = nauseaStrength;
    }
}
