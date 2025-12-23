package com.ferra13671.bthack.mixins;

import com.ferra13671.bthack.BThackClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void modifyInit(GameConfig gameConfig, CallbackInfo ci) {
        BThackClient.getINSTANCE().getLogger().info("Mixins test!");
    }
}
