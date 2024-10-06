package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;

public class ShiftSpam extends Module {

    public static NumberSetting activeDelay;
    public static NumberSetting deActiveDelay;

    public ShiftSpam() {
        super("ShiftSpam",
                "lang.module.ShiftSpam",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );

        activeDelay = new NumberSetting("Delay Active", this, 0.1,0.01,1,false);
        deActiveDelay = new NumberSetting("Delay deActive", this, 0.1,0.01,1,false);

        initSettings(
                activeDelay,
                deActiveDelay
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        ThreadManager.startNewThread(thread -> {
            while (Client.getModuleByName("ShiftSpam").isEnabled()) {
                long delayActive = (long) activeDelay.getValue() * 1000;
                long delayDeActive = (long) deActiveDelay.getValue() * 1000;

                mc.options.sneakKey.setPressed(true);
                try {
                    thread.sleep(delayActive);
                } catch (InterruptedException ignored) {}
                mc.options.sneakKey.setPressed(false);
                try {
                    thread.sleep(delayDeActive);
                } catch (InterruptedException ignored) {}
            }
        });
    }
}
