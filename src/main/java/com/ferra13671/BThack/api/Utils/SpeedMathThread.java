package com.ferra13671.BThack.api.Utils;


import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.api.Interfaces.Mc;

public final class SpeedMathThread extends Thread implements Mc {

    public static double speed = 0;
    public static boolean active = false;

    public void run() {
        active = true;
        while (Client.getModuleByName("HUD").isEnabled()) {
            if (mc.player != null && mc.world != null) {
                double oldX = mc.player.getX();
                double oldZ = mc.player.getZ();

                try {
                    sleep(200);
                } catch (InterruptedException ignored) {
                }

                if (mc.player != null && mc.world != null) {

                    double newX = mc.player.getX();
                    double newZ = mc.player.getZ();

                    double sX = Math.pow(newX - oldX, 2);
                    double sZ = Math.pow(newZ - oldZ, 2);

                    speed = (Math.sqrt(sX + sZ)) * 5;
                }
            } else {
                break;
            }
        }
        active = false;
        speed = 0;
    }
}
