package com.ferra13671.BThack.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicTracker.class)
public class MixinMusicTracker {


    @Shadow @Final private MinecraftClient client;

    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    public void modifyMusicPlay(MusicSound type, CallbackInfo ci) {
        if (client.player == null || client.world == null)
            ci.cancel();
    }
}
