package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.item.BlockItem;

public class FastPlace extends Module {

    public FastPlace() {
        super("FastPlace",
                "lang.module.FastPlace",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );
    }

    @EventSubscriber
    public void onUpdate(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.getMainHandStack().getItem() instanceof BlockItem) {
            mc.itemUseCooldown = 0;
        }
    }
}
