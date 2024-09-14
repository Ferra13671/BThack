package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.impl.Modules.MOVEMENT.ElytraFlight;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketEntity.class)
public class MixinFireWorkRocketEntity implements Mc {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
    public Vec3d modifyGetRotationVector(LivingEntity instance) {
        if (instance == mc.player) {
            Client.fireWorkManager.updateFireWorkTick();
        }
        if (Client.getModuleByName("ElytraFlight").isEnabled() && Client.fireWorkManager.isUsingFireWork()) {
            float yaw = instance.getYaw();
            float pitch = instance.getPitch();
            ElytraFlight elytraFlight = (ElytraFlight) Client.getModuleByName("ElytraFlight");

            switch (ElytraFlight.mode.getValue()) {
                case "Bounce" -> pitch = elytraFlight.bouncePitchRotate(pitch);
                case "Control" -> {
                    yaw = elytraFlight.controlYawRotate(yaw);
                    pitch = elytraFlight.controlPitchRotate(pitch);
                }
            }

            return instance.getRotationVector(pitch, yaw);
        }
        return instance.getRotationVector();
    }
}
