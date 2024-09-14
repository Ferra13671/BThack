package com.ferra13671.BThack.Events.Events.Entity;

import com.ferra13671.BThack.Events.MegaEvents.Event;

public class AirStrafingSpeedEvent extends Event {

    private float airStrafingSpeed;
    private final float defaultSpeed;

    public AirStrafingSpeedEvent(float airStrafingSpeed) {
        this.airStrafingSpeed = airStrafingSpeed;
        defaultSpeed = airStrafingSpeed;
    }

    public float getSpeed()
    {
        return airStrafingSpeed;
    }

    public void setSpeed(float airStrafingSpeed)
    {
        this.airStrafingSpeed = airStrafingSpeed;
    }

    public float getDefaultSpeed()
    {
        return defaultSpeed;
    }
}
