package com.ferra13671.BThack.mixins.gui_and_hud;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.client.gui.screen.DeathScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DeathScreen.class, priority = Integer.MAX_VALUE)
public class MixinDeathScreen implements Mc {

    @Shadow private int ticksSinceDeath;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void modifyTick(CallbackInfo ci) {
        if (this.ticksSinceDeath >= 20) {
            if (ModuleList.autoRespawn.isEnabled()) {
                mc.player.requestRespawn();
                mc.setScreen(null);
                ci.cancel();
            }
        }
    }
}
