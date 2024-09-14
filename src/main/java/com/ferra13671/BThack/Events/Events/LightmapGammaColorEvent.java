package com.ferra13671.BThack.Events.Events;

import com.ferra13671.BThack.Events.MegaEvents.Event;

public class LightmapGammaColorEvent extends Event {

    public int gammaColor;

    public LightmapGammaColorEvent(int gammaColor) {
        this.gammaColor = gammaColor;
    }
}
