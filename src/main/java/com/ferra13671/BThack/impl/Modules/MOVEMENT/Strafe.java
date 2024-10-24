package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.StrafeUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.Vec3d;

public class Strafe extends Module {
    public Strafe() {
        super("Strafe",
                "lang.module.Strafe",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );
    }

    @EventSubscriber
    public void onPlayerTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player != null) {
            if (!mc.player.isFallFlying()) {
                Vec3d velocity = mc.player.getVelocity();

                double currentPlayerSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
                double yaw = Math.toRadians(StrafeUtils.getPlayerDirectionOnInput());
                mc.player.velocity.x = -Math.sin(yaw) * currentPlayerSpeed;
                mc.player.velocity.z = Math.cos(yaw) * currentPlayerSpeed;
            }
        }
    }
}
