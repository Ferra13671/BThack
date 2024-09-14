package com.ferra13671.BThack.api.Utils.Modules;


import com.ferra13671.BThack.api.Interfaces.Mc;

public final class StrafeUtils implements Mc {

    public static float getPlayerDirectionOnInput() {
        float yaw = mc.player.getYaw();
        float strafe = 45;
        if (mc.player.input.movementForward < 0) {
            strafe = -45;
            yaw += 180;
        }
        if (mc.player.input.movementSideways > 0) {
            yaw -= strafe;
            if (mc.player.input.movementForward == 0) {
                yaw -= 45;
            }
        } else if (mc.player.input.movementSideways < 0) {
            yaw += strafe;
            if (mc.player.input.movementForward == 0) {
                yaw += 45;
            }
        }
        return yaw;
    }

    public static float getPlayerDirectionOnKeybindings() {
        float yaw = mc.player.getYaw();
        float strafe = 45;
        if (mc.options.backKey.isPressed()) {
            strafe = -45;
            yaw += 180;
        }
        if (mc.options.leftKey.isPressed()) {
            yaw -= strafe;
            if (!mc.options.forwardKey.isPressed() && !mc.options.backKey.isPressed()) {
                yaw -= 45;
            }
        } else if (mc.options.rightKey.isPressed()) {
            yaw += strafe;
            if (!mc.options.forwardKey.isPressed() && !mc.options.backKey.isPressed()) {
                yaw += 45;
            }
        }
        return yaw;
    }
}
