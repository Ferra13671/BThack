package com.ferra13671.bthack.mixins;

import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.events.MouseInputEvent;
import com.ferra13671.bthack.utils.KeyAction;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Inject(method = "onButton", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/FramerateLimitTracker;onInputReceived()V", shift = At.Shift.AFTER))
    public void modifyOnButton(long handle, MouseButtonInfo rawButtonInfo, int action, CallbackInfo ci) {
        if (action == 0 || action == 1)
            BThackClient.getInstance().getEventBus().activate(new MouseInputEvent(rawButtonInfo.button(), action == 1 ? KeyAction.Press : KeyAction.Release));
    }
}
