package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.IEntity;

public class ElytraFastClose extends Module {

    public ElytraFastClose() {
        super("ElytraFastClose",
                "lang.module.ElytraFastClose",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );
    }

    @EventSubscriber
    public void onTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.verticalCollision) {
            if (Client.getModuleByName("ElytraFlight").isEnabled()) return;
            IEntity entity = (IEntity) mc.player;
            if (entity.invokeGetFlag(7))
                entity.invokeSetFlag(7, false);
        }
    }
}
