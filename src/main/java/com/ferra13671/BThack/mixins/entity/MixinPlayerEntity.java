package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Events.Player.PlayerTravelEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements Mc {

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
}
