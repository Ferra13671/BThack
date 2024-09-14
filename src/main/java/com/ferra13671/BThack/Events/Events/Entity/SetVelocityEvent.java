package com.ferra13671.BThack.Events.Events.Entity;

import com.ferra13671.BThack.Events.MegaEvents.Event;
import net.minecraft.util.math.Vec3d;

public class SetVelocityEvent extends Event {

    private Vec3d velocity;

    public SetVelocityEvent(Vec3d velocity) {
        this.velocity = velocity;
    }

    public Vec3d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec3d value) {
        velocity = value;
    }
}
