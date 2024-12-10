package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.Entity.JumpHeightEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
import com.ferra13671.BThack.mixins.accessor.ILivingEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.math.MathHelper;

public class Speed extends Module {

    public static NumberSetting speed;
    public static NumberSetting jumpHeight;

    public Speed() {
        super("Speed",
                "lang.module.Speed",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        speed = new NumberSetting("Speed", this, 0.25,0.1,1,false);
        jumpHeight = new NumberSetting("Jump Height", this, 0.11, 0.05, 0.42, false);

        initSettings(
                speed,
                jumpHeight
        );
    }

    @EventSubscriber
    public void onPlayerTick(ClientTickEvent e) {
        if (nullCheck()) return;

        ((ILivingEntity) mc.player).setJumpingCooldown(0);
        mc.options.jumpKey.setPressed(true);

        if (mc.player.onGround && mc.player.input.movementForward > 0 && !PlayerUtils.isInWater() && !mc.player.isInLava()) {
            double _speed = speed.getValue();

            mc.player.setSprinting(true);

            float yaw = mc.player.yaw * 0.0174532920F;

            mc.player.velocity.x -= MathHelper.sin(yaw) * (_speed / 5);
            mc.player.velocity.z += MathHelper.cos(yaw) * (_speed / 5);
        }
    }

    @EventSubscriber
    public void onJumpHeight(JumpHeightEvent e) {
        e.setJumpHeight((float) jumpHeight.getValue());
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.options.jumpKey.setPressed(false);
    }
}
