package com.ferra13671.BThack.api.Managers;

import com.ferra13671.BThack.api.Interfaces.Mc;

public class FireWorkManager implements Mc {
    public long lastFireWorkTick = 0;

    public void updateFireWorkTick() {
        lastFireWorkTick = System.currentTimeMillis();
    }

    public boolean isUsingFireWork() {
        return (System.currentTimeMillis() - lastFireWorkTick) < (mc.renderTickCounter.tickTime + 15);
    }

    public boolean timePassedFromLastFireWorkUse(int millis) {
        return (System.currentTimeMillis() - lastFireWorkTick) > (mc.renderTickCounter.tickTime + 15 + millis);
    }
}
