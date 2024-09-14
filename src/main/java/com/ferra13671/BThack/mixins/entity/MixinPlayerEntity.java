package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Events.Events.Entity.AirStrafingSpeedEvent;
import com.ferra13671.BThack.Events.Events.Player.PlayerTravelEvent;
import com.ferra13671.BThack.Events.MegaEvents.Event;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {

    @Shadow @Final private PlayerAbilities abilities;

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void modifyTick(CallbackInfo ci) {
        Event event = new PlayerTravelEvent();
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            move(MovementType.SELF, getVelocity());
            ci.cancel();
        }
    }

    @Inject(method = "getOffGroundSpeed", at = @At("HEAD"), cancellable = true)
    public void modifyGetOffGroundSpeed(CallbackInfoReturnable<Float> cir) {
        AirStrafingSpeedEvent event = new AirStrafingSpeedEvent(getOffGroundSpeed2());
        BThack.EVENT_BUS.activate(event);
        cir.setReturnValue(event.getSpeed());
    }

    @Unique
    public float getOffGroundSpeed2() {
        if (this.abilities.flying && !this.hasVehicle()) {
            return this.isSprinting() ? this.abilities.getFlySpeed() * 2.0F : this.abilities.getFlySpeed();
        } else {
            return this.isSprinting() ? 0.025999999F : 0.02F;
        }
    }
}
