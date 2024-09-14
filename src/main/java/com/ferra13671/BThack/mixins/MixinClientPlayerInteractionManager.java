package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Events.Events.Block.AttackBlockEvent;
import com.ferra13671.BThack.Events.Events.Block.UseBlockEvent;
import com.ferra13671.BThack.Events.Events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.Events.MegaEvents.Event;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientPlayerInteractionManager.class, priority = Integer.MAX_VALUE)
public class MixinClientPlayerInteractionManager {

    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    private GameMode gameMode;

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    public void modifyAttackEntity(PlayerEntity player, Entity entity, CallbackInfo ci) {
        Event event = new AttackEntityEvent(player, player.getEntityWorld(), Hand.MAIN_HAND, entity);
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "interactBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;sendSequencedPacket(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/network/SequencedPacketCreator;)V"), cancellable = true)
    public void modifyInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> ci) {
        if (player.isSpectator()) return;

        Event event = new UseBlockEvent(player, player.getWorld(), hand, blockHitResult);
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            ci.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(method = "updateBlockBreakingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameMode;isCreative()Z", ordinal = 0), cancellable = true)
    public void method_2902(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> ci) {
        if (gameMode.isCreative()) {
            fabric_fireAttackBlockCallback(pos, direction, ci);
        }
    }

    @Unique
    private void fabric_fireAttackBlockCallback(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> ci) {
        Event event = new AttackBlockEvent(client.player, client.world, Hand.MAIN_HAND, pos, direction);
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            ci.setReturnValue(false);
        }
    }
}
