package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Events.Events.Render.RenderHandEvent;
import com.ferra13671.BThack.Events.Events.Render.TransformFirstPersonEvent;
import com.ferra13671.BThack.Events.MegaEvents.Event;
import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer implements Mc {

    @Shadow protected abstract void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

    //MainHand Render
    @Redirect(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 0))
    public void modifyRenderMainHand(HeldItemRenderer instance, AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        renderHandEventCheck(player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
    }

    //OffHand Render
    @Redirect(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 1))
    public void modifyRenderOffHand(HeldItemRenderer instance, AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        renderHandEventCheck(player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
    }


    //TransformFirstPerson
    @Inject(method = "applySwingOffset", at = @At("HEAD"), cancellable = true)
    public void modifyApplySwingOffsetPre(MatrixStack matrices, Arm arm, float swingProgress, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Pre(arm, matrices, TransformFirstPersonEvent.TransformType.SWING);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "applySwingOffset", at = @At("TAIL"))
    public void modifyApplySwingOffsetPost(MatrixStack matrices, Arm arm, float swingProgress, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Post(arm, matrices, TransformFirstPersonEvent.TransformType.SWING);
        BThack.EVENT_BUS.activate(event);
    }

    @Inject(method = "applyEquipOffset", at = @At("HEAD"), cancellable = true)
    public void modifyApplyEquipOffsetPre(MatrixStack matrices, Arm arm, float equipProgress, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Pre(arm, matrices, TransformFirstPersonEvent.TransformType.EQUIP);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "applyEquipOffset", at = @At("TAIL"))
    public void modifyApplyEquipOffsetPost(MatrixStack matrices, Arm arm, float equipProgress, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Post(arm, matrices, TransformFirstPersonEvent.TransformType.EQUIP);
        BThack.EVENT_BUS.activate(event);
    }

    @Inject(method = "applyBrushTransformation", at = @At("HEAD"), cancellable = true)
    public void modifyApplyBrushTransformationPre(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, float equipProgress, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Pre(arm, matrices, TransformFirstPersonEvent.TransformType.BRUSH);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "applyBrushTransformation", at = @At("TAIL"))
    public void modifyApplyBrushTransformationPost(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, float equipProgress, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Post(arm, matrices, TransformFirstPersonEvent.TransformType.BRUSH);
        BThack.EVENT_BUS.activate(event);
    }

    @Inject(method = "applyEatOrDrinkTransformation", at = @At("HEAD"), cancellable = true)
    public void modifyApplyEatOrDrinkTransformationPre(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Pre(arm, matrices, TransformFirstPersonEvent.TransformType.EAT);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "applyEatOrDrinkTransformation", at = @At("TAIL"))
    public void modifyApplyEatOrDrinkTransformationPost(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Post(arm, matrices, TransformFirstPersonEvent.TransformType.EAT);
        BThack.EVENT_BUS.activate(event);
    }

    @Inject(method = "renderArmHoldingItem", at = @At("HEAD"), cancellable = true)
    public void modifyRenderArmHoldingItemPre(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm, CallbackInfo ci) {
        Event event = new TransformFirstPersonEvent.Pre(arm, matrices, TransformFirstPersonEvent.TransformType.ARM);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Redirect(method = "renderArmHoldingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;renderLeftArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;)V", ordinal = 0))
    public void modifyRenderArmHoldingItemPostLeft(PlayerEntityRenderer instance, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player) {
        Event event = new TransformFirstPersonEvent.Post(Arm.LEFT, matrices, TransformFirstPersonEvent.TransformType.ARM);
        BThack.EVENT_BUS.activate(event);
        if (!event.isCancelled())
            instance.renderLeftArm(matrices, vertexConsumers, light, player);
    }
    @Redirect(method = "renderArmHoldingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;renderRightArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;)V", ordinal = 0))
    public void modifyRenderArmHoldingItemPostRight(PlayerEntityRenderer instance, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player) {
        Event event = new TransformFirstPersonEvent.Post(Arm.RIGHT, matrices, TransformFirstPersonEvent.TransformType.ARM);
        BThack.EVENT_BUS.activate(event);
        if (!event.isCancelled())
            instance.renderRightArm(matrices, vertexConsumers, light, player);
    }
    ///////////////////////////


    @Unique
    public void renderHandEventCheck(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (player == mc.player) {
            RenderHandEvent event = new RenderHandEvent(hand);
            BThack.EVENT_BUS.activate(event);
            if (!event.isCancelled()) {
                this.renderFirstPersonItem(player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
            }
            return;
        }
        this.renderFirstPersonItem(player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
    }
}
