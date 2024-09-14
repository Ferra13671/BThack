package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Events.Events.LightmapGammaColorEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;

public class FullBright extends Module {
    public FullBright() {
        super("FullBright",
                "lang.module.FullBright",
                KeyboardUtils.RELEASE,
                Category.RENDER,
                false
        );
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    public void onGammaColor(LightmapGammaColorEvent e) {
        e.setCancelled(true);
        e.gammaColor = -1;
    }
}
