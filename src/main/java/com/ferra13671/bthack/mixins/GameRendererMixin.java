package com.ferra13671.bthack.mixins;

import com.ferra13671.bthack.render.BThackRenderSystem;
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
}
