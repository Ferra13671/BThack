package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.impl.Modules.RENDER.NoRender;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    public <T extends Entity> void modifyRenderLabelIfPresent(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (Client.getModuleByName("Nametags").isEnabled()) ci.cancel();
    }

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private <T extends Entity> void shouldRender(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (Client.getModuleByName("NoRender").isEnabled()) {
            if (NoRender.fallingBlocks.getValue() && entity instanceof FallingBlockEntity) {
                cir.setReturnValue(false);
                cir.cancel();
            }
            if (NoRender.armorStands.getValue() && entity instanceof ArmorStandEntity) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
