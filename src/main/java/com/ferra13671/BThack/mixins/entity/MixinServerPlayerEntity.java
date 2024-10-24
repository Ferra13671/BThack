package com.ferra13671.BThack.mixins.entity;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ServerPlayerEntity.class, priority = Integer.MAX_VALUE)
public class MixinServerPlayerEntity {

    /*
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void modifyAttack(Entity target, CallbackInfo info) {

        ServerPlayerEntity player = (ServerPlayerEntity) ((Object) this);

        Event event = new AttackEntityEvent(player, player.getEntityWorld(), Hand.MAIN_HAND, target);
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            info.cancel();
        }
    }

     */
}
