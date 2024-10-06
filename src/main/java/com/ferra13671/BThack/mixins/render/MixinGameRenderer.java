package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.impl.Modules.RENDER.NoOverlay;
import com.ferra13671.BThack.impl.Modules.RENDER.NoRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Shadow protected abstract void renderNausea(DrawContext context, float distortionStrength);

    @Shadow @Final MinecraftClient client;

    //Hurt Render
    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    public void modifyTiltViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (Client.getModuleByName("NoOverlay").isEnabled() && NoOverlay.hurtCam.getValue())
            ci.cancel();
    }

    //Overlays Render
    @Redirect(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameOverlayRenderer;renderOverlays(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;)V", ordinal = 0))
    public void modifyRenderOverlays(MinecraftClient client, MatrixStack matrices) {
        if (Client.getModuleByName("NoRender").isEnabled() && NoRender.overlay.getValue()) {
        } else
            InGameOverlayRenderer.renderOverlays(this.client, matrices);
    }
}
