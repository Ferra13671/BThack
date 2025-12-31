package com.ferra13671.bthack.mixins;

import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.events.Render2DEvent;
import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.screen.BThackScreen;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "render", at = @At("TAIL"))
    public void modifyRenderTail(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        if (minecraft.screen instanceof BThackScreen screen) {
            BThackRenderSystem.prepareProjection();
            MinecraftPlugin.bindMainFramebuffer(true);
            MinecraftPlugin.unbindMinecraftSamplers();
            CometRenderer.applyDefaultBlend();

            screen.render((int) ((IMouseHandler) minecraft.mouseHandler).bthack$$$getXPos(), (int) ((IMouseHandler) minecraft.mouseHandler).bthack$$$getYPos());
        }

        BThackRenderSystem.invokeRenderCalls();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/state/GuiRenderState;reset()V", shift = At.Shift.AFTER))
    public void modifyRenderBeforeGui(DeltaTracker deltaTracker, boolean tick, CallbackInfo ci) {
        BThackRenderSystem.prepareProjection();
        MinecraftPlugin.bindMainFramebuffer(true);
        MinecraftPlugin.unbindMinecraftSamplers();
        CometRenderer.applyDefaultBlend();

        BThackRenderSystem.BLUR_PROVIDER.drawBlur();
        BThackClient.getInstance().getEventBus().activate(new Render2DEvent());
    }
}
