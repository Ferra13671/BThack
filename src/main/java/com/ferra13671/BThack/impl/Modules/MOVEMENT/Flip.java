package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.util.math.Vec3d;

public class Flip extends Module {
    public Flip() {
        super("Flip",
                "lang.module.Flip",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        Vec3d velocity = mc.player.getVelocity();

        mc.player.setYaw(mc.player.getYaw() - 180);
        mc.player.setVelocity(-velocity.x, velocity.y, -velocity.z);

        toggle();
    }
}
