package com.ferra13671.BThack.impl.Modules.MOVEMENT;


import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.ILivingEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class NoJumpDelay extends Module {
    public NoJumpDelay() {
        super("NoJumpDelay",
                "lang.module.NoJumpDelay",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );

    }

    @EventSubscriber
    public void onUpdate(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        ((ILivingEntity) mc.player).setJumpingCooldown(0);
    }
}