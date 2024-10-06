package com.ferra13671.BThack.Events.Events.Player;

import com.ferra13671.BThack.Events.MegaEvents.Event;

public class PlayerTraverRotEvent extends Event {

    public float yaw;
    public float pitch;

    public final boolean firework;

    public PlayerTraverRotEvent(float yaw, float pitch, boolean firework) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.firework = firework;
    }
}
