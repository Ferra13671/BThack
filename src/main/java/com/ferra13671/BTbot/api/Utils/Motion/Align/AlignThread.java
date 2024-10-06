package com.ferra13671.BTbot.api.Utils.Motion.Align;


import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import net.minecraft.util.math.Vec3d;

class AlignThread extends Thread implements Mc {
    boolean isMoving = false;

    private final double needX;
    private final double needZ;
    private final double minX;
    private final double maxX;
    private final double minZ;
    private final double maxZ;

    public AlignThread(double needX, double needZ, double minX, double maxX, double minZ, double maxZ) {
        this.needX = needX;
        this.needZ = needZ;
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    @Override
    public synchronized void start() {
        isMoving = true;
        super.start();
    }

    @Override
    public void run() {
        isMoving = true;
        float yaw = mc.player.yaw;

        while (!check()) {
            rotate();
            mc.player.input.movementForward = 1;
            //mc.options.forwardKey.setPressed(true);

            Thread.yield();
        }
        mc.player.input.movementForward = 0;
        //mc.options.forwardKey.setPressed(false);
        mc.player.yaw = yaw;
        isMoving = false;
    }



    private void rotate() {
        mc.player.yaw = AimBotUtils.rotations(new Vec3d(needX, mc.player.getY(), needZ))[0];
    }

    private boolean check() {
        return (mc.player.getX() > minX && mc.player.getX() < maxX) && (mc.player.getZ() > minZ && mc.player.getZ() < maxZ);
    }
}
