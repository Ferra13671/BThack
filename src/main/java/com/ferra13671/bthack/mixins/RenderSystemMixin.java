package com.ferra13671.bthack.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {

    @Inject(method = "initRenderThread", at = @At("TAIL"))
    private static void modifyInitRenderThread(CallbackInfo ci) {
        LoggerFactory.getLogger("BTHACK TEST").info("Create render thread");
    }
}
