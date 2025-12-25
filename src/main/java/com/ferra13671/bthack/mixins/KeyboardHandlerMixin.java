package com.ferra13671.bthack.mixins;

import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.events.KeyInputEvent;
import com.ferra13671.bthack.utils.KeyAction;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @Inject(method = "keyPress", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/FramerateLimitTracker;onInputReceived()V", shift = At.Shift.AFTER))
    public void modifyKeyPress(long handle, int action, KeyEvent event, CallbackInfo ci) {
        if (action == 0 || action == 1)
            BThackClient.getInstance().getEventBus().activate(new KeyInputEvent(event.key(), action == 1 ? KeyAction.Press : KeyAction.Release));
    }
}
