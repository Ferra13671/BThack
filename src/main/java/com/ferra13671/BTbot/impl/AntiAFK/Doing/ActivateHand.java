package com.ferra13671.BTbot.impl.AntiAFK.Doing;

import com.ferra13671.BTbot.impl.AntiAFK.Start.StartAntiAFK;
import com.ferra13671.BTbot.impl.AntiAFK.Start.StartAntiAFKThread;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.ItemsUtils;
import net.minecraft.util.Hand;

public class ActivateHand extends Thread implements Mc {
    public void run() {
        if (StartAntiAFKThread.startDoing) return;
        StartAntiAFKThread.startDoing = true;
        ItemsUtils.useItem(Hand.MAIN_HAND, true);
        double d = StartAntiAFK.delay * 1000;
        long delay = (long) d;
        try {
            sleep(delay);
        } catch (InterruptedException ignored) {}
        StartAntiAFKThread.startDoing = false;
        new StartAntiAFKThread().start();
    }
}
