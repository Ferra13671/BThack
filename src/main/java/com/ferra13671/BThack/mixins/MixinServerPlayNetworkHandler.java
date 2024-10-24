package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Events.Entity.UseEntityEvent;
import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/server/network/ServerPlayNetworkHandler$1", priority = Integer.MAX_VALUE)
public abstract class MixinServerPlayNetworkHandler implements PlayerInteractEntityC2SPacket.Handler {

    @Shadow
    @Final
    ServerPlayNetworkHandler field_28963;

    @Shadow
    @Final
    Entity field_28962;

    @Inject(method = "interactAt(Lnet/minecraft/util/Hand;Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "HEAD"), cancellable = true)
    public void modifyInteractAt(Hand hand, Vec3d hitPosition, CallbackInfo ci) {
        PlayerEntity player = field_28963.player;
        World world = player.getEntityWorld();

        //EntityHitResult hitResult = new EntityHitResult(field_28962, hitPosition.add(field_28962.getX(), field_28962.getY(), field_28962.getZ()));

        Event event = new UseEntityEvent(player, world, hand, field_28962);
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "interact(Lnet/minecraft/util/Hand;)V", at = @At(value = "HEAD"), cancellable = true)
    public void modifyInteract(Hand hand, CallbackInfo ci) {
        PlayerEntity player = field_28963.player;
        World world = player.getEntityWorld();

        Event event = new UseEntityEvent(player, world, hand, field_28962);
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
