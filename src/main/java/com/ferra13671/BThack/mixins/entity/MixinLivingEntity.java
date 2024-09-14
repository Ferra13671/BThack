package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Events.Events.Entity.EntityMotionEvent;
import com.ferra13671.BThack.Events.Events.Entity.JumpHeightEvent;
import com.ferra13671.BThack.Events.MegaEvents.Event;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.impl.Modules.MOVEMENT.ElytraFlight;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
        if (mc.player != null && mc.world != null) {
            if (this.randomSmallSeed == mc.player.randomSmallSeed && this.randomLargeSeed == mc.player.randomLargeSeed) {
                if (Client.getModuleByName("BabyModel").isEnabled()) {
                    cir.setReturnValue(true);
                }
            }
        }
    }

    @Inject(method = "tickMovement", at = @At("TAIL"), cancellable = true)
    public void modifyTickMovement(CallbackInfo ci) {
        Event event = new EntityMotionEvent();
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "getJumpVelocity", at = @At("TAIL"), cancellable = true)
    public void modifyGetJumpVelocity(CallbackInfoReturnable<Float> cir) {
        JumpHeightEvent event = new JumpHeightEvent(0.42F * this.getJumpVelocityMultiplier() + this.getJumpBoostVelocityModifier());

        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            cir.setReturnValue(0f);
        } else {
            cir.setReturnValue(event.getJumpHeight());
        }
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
    public Vec3d modifyGetRot(LivingEntity instance) {
        if (Client.getModuleByName("ElytraFlight").isEnabled()) {
            float yaw = instance.yaw;
            float pitch = instance.pitch;
            ElytraFlight elytraFlight = (ElytraFlight) Client.getModuleByName("ElytraFlight");

            switch (ElytraFlight.mode.getValue()) {
                case "Bounce" -> pitch = elytraFlight.bouncePitchRotate(pitch);
                case "Control" -> {
                    yaw = elytraFlight.controlYawRotate(yaw);
                    pitch = elytraFlight.controlPitchRotate(pitch);
                }
            }
            return this.getRotationVector(pitch, yaw);
        }
        return this.getRotationVector();
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getPitch()F", ordinal = 0))
    public float modifyGetPitch(LivingEntity instance) {
        if (Client.getModuleByName("ElytraFlight").isEnabled()) {
            float pitch = instance.pitch;
            ElytraFlight elytraFlight = (ElytraFlight) Client.getModuleByName("ElytraFlight");

            switch (ElytraFlight.mode.getValue()) {
                case "Bounce" -> pitch = elytraFlight.bouncePitchRotate(pitch);
                case "Control" -> pitch = elytraFlight.controlPitchRotate(pitch);
            }

            return pitch;
        }
        return this.getPitch();
    }
}
