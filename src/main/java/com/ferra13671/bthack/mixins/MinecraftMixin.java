package com.ferra13671.bthack.mixins;

import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.events.TickEvent;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    public void modifyTick(CallbackInfo ci) {
        BThackClient.getInstance().getEventBus().activate(new TickEvent());
    }
}
