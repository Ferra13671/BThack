package com.ferra13671.BThack.api.Managers;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Events.Events.PacketEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.util.math.MathHelper;

public class TPSManager {
    private long prevTime;
    private final float[] ticks = new float[20];
    private int currentTick;

    public TPSManager() {
        this.prevTime = -1;

        for (int i = 0, len = this.ticks.length; i < len; i++) {
            this.ticks[i] = 0.0f;
        }

        BThack.EVENT_BUS.register(this);
    }

    public float getTickRate() {
        int tickCount = 0;
        float tickRate = 0.0f;

        for (final float tick : this.ticks) {
            if (tick > 0.0f) {
                tickRate += tick;
                tickCount++;
            }
        }

        return MathHelper.clamp((tickRate / tickCount), 0.0f, 20.0f);
    }

    @EventSubscriber
    public void onPacket(PacketEvent.Receive e) {
        if (e.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            if (this.prevTime != -1) {
                this.ticks[this.currentTick % this.ticks.length] = MathHelper.clamp((20.0f / ((float) (System.currentTimeMillis() - this.prevTime) / 1000.0f)), 0.0f, 20.0f);
                this.currentTick++;
            }

            this.prevTime = System.currentTimeMillis();
        }
    }
}
