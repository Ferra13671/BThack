package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.client.option.KeyBinding;

public class GuiMove extends Module {

    public GuiMove() {
        super("GuiMove",
                "lang.module.GuiMove",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );
    }

    @EventSubscriber
    public void onTick(TickEvent.ClientTickEvent e) {
        if (mc.currentScreen == null || Client.getModuleByName("ElytraFlight").isEnabled()) return;
        KeyBinding[] keys = {mc.options.forwardKey, mc.options.backKey, mc.options.leftKey, mc.options.rightKey, mc.options.sprintKey, mc.options.sneakKey, mc.options.jumpKey};

        for (KeyBinding keyBinding : keys) {
            keyBinding.setPressed(KeyboardUtils.isKeyDown(keyBinding.getDefaultKey().getCode()));
        }
    }
}
