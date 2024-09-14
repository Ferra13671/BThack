package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Events.Events.Block.AttackBlockEvent;
import com.ferra13671.BThack.Events.Events.Block.UseBlockEvent;
import com.ferra13671.BThack.Events.MegaEvents.Event;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayerInteractionManager.class, priority = Integer.MAX_VALUE)
public class MixinServerPlayerInteractionManager {

    @Shadow @Final protected ServerPlayerEntity player;

    @Shadow protected ServerWorld world;

    @Inject(at = @At("HEAD"), method = "interactBlock", cancellable = true)
    public void modifyInteractBlock(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> ci) {
        Event event = new UseBlockEvent(player, world, hand, blockHitResult);
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            ci.setReturnValue(ActionResult.FAIL);
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "processBlockBreakingAction", cancellable = true)
    public void startBlockBreak(BlockPos pos, PlayerActionC2SPacket.Action playerAction, Direction direction, int worldHeight, int i, CallbackInfo ci) {
        if (playerAction != PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) return;

        Event event = new AttackBlockEvent(player, world, Hand.MAIN_HAND, pos, direction);
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
