package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.Entity.JumpHeightEvent;
import com.ferra13671.BThack.api.Events.Player.PlayerTraverRotEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements Mc {

    @Shadow @Final public float randomSmallSeed;

    @Shadow @Final public float randomLargeSeed;

    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract float getJumpBoostVelocityModifier();

    @Shadow public abstract void remove(RemovalReason reason);

    @Inject(method = "isBaby", at = @At("HEAD"), cancellable = true)
    public void modifyIsBaby(CallbackInfoReturnable<Boolean> cir) {
        if (!Module.nullCheck())
            if (this.randomSmallSeed == mc.player.randomSmallSeed && this.randomLargeSeed == mc.player.randomLargeSeed)
                if (ModuleList.babyModel.isEnabled())
                    cir.setReturnValue(true);
    }

    @Inject(method = "getJumpVelocity", at = @At("TAIL"), cancellable = true)
    public void modifyGetJumpVelocity(CallbackInfoReturnable<Float> cir) {
        if ((Object) this != mc.player) return;
        JumpHeightEvent event = new JumpHeightEvent(0.42F * getJumpVelocityMultiplier() + getJumpBoostVelocityModifier());

        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled())
            cir.setReturnValue(0f);
        else
            cir.setReturnValue(event.getJumpHeight());
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
    public Vec3d modifyGetRot(LivingEntity instance) {
        if (instance != mc.player) return this.getRotationVector();

        PlayerTraverRotEvent event = new PlayerTraverRotEvent(instance.yaw, instance.pitch, false);
        BThack.EVENT_BUS.activate(event);
        return this.getRotationVector(event.pitch, event.yaw);
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getPitch()F", ordinal = 0))
    public float modifyGetPitch(LivingEntity instance) {
        if (instance != mc.player) return this.getPitch();

        PlayerTraverRotEvent event = new PlayerTraverRotEvent(instance.yaw, instance.pitch, false);
        BThack.EVENT_BUS.activate(event);
        return event.pitch;
    }
}
