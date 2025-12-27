package com.ferra13671.bthack.mixins;

import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.events.Render2DEvent;
import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At("TAIL"))
    public void modifyRenderTail(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        BThackRenderSystem.invokeRenderCalls();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/state/GuiRenderState;reset()V", shift = At.Shift.AFTER))
    public void modifyRenderBeforeGui(DeltaTracker deltaTracker, boolean tick, CallbackInfo ci) {
        BThackRenderSystem.prepareProjection();
        MinecraftPlugin.bindMainFramebuffer(true);
        MinecraftPlugin.unbindMinecraftSamplers();

        BThackClient.getInstance().getEventBus().activate(new Render2DEvent());
    }
}
