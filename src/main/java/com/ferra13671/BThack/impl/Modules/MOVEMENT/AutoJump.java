package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;

public class AutoJump extends Module {
    public AutoJump() {
        super("AutoJump",
                "lang.module.AutoJump",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        ThreadManager.startNewThread(thread -> {
            while (Client.getModuleByName("AutoJump").isEnabled()) {
                if (!mc.options.jumpKey.isPressed()) {
                    mc.options.jumpKey.setPressed(true);
                }

                try {
                    thread.sleep(50);
                } catch (InterruptedException ignored) {}
            }

            mc.options.jumpKey.setPressed(false);
        });
    }
}
