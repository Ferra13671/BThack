package com.bt.BTbot.impl.AntiAFK.Doing.GotoN;

import com.bt.BTbot.api.Utils.Generate.GenerateIntNumber;
import com.bt.BTbot.impl.AntiAFK.Start.StartAntiAFK;
import com.bt.BTbot.impl.AntiAFK.Start.StartAntiAFKThread;

public class PrepareGotoN extends Thread{
    public static int newX;
    public static int newZ;
    public static boolean negative;
    public void run() {
        if (StartAntiAFKThread.startDoing) return;
        StartAntiAFKThread.startDoing = true;
        double r = StartAntiAFK.walkRadius;
        int radius = (int) r;
        int a = GenerateIntNumber.generate(1, radius);
        int b = GenerateIntNumber.generate(1, radius);
        newX = a;
        newZ = b;
        negative = GenerateIntNumber.generate(0,1) != 0;
        new GotoN().start();
    }
}
