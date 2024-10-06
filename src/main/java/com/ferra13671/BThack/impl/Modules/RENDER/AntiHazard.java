package com.ferra13671.BThack.impl.Modules.RENDER;


import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.entity.effect.StatusEffects;

public class AntiHazard extends Module {
    public AntiHazard() {
        super("AntiHazard",
                "lang.module.AntiHazard",
                KeyboardUtils.RELEASE,
                Category.RENDER,
                false
        );
    }

    @EventSubscriber
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;

        if (mc.player.hasStatusEffect(StatusEffects.BLINDNESS)) {
            mc.player.removeStatusEffect(StatusEffects.BLINDNESS);
        }
        if (mc.player.hasStatusEffect(StatusEffects.NAUSEA)) {
            mc.player.removeStatusEffect(StatusEffects.NAUSEA);
        }
    }
}
