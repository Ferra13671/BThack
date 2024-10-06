package com.ferra13671.BThack.impl.Modules.PLAYER;


import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.ILivingEntity;

public class NoJumpDelay extends Module {
    public NoJumpDelay() {
        super("NoJumpDelay",
                "lang.module.NoJumpDelay",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );

    }

    @EventSubscriber
    public void onUpdate(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        ((ILivingEntity) mc.player).setJumpingCooldown(0);
    }
}