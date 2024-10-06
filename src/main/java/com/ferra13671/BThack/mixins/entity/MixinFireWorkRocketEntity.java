package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Events.Events.Player.PlayerTraverRotEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
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
        if (instance != mc.player) return instance.getRotationVector();

        PlayerTraverRotEvent event = new PlayerTraverRotEvent(instance.yaw, instance.pitch, true);
        BThack.EVENT_BUS.activate(event);
        return instance.getRotationVector(event.pitch, event.yaw);
    }
}
