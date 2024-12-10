package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.MOVEMENT.ElytraFlight;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class Timer extends Module {
    public static NumberSetting tickSpeed;

    public Timer() {
        super("Timer",
                "lang.module.Timer",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        tickSpeed = new NumberSetting("Tick speed", this, 1,0.1,3,false);

        initSettings(
                tickSpeed
        );
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        float speed = (float) tickSpeed.getValue();
        Managers.TICK_MANAGER.applyTickModifier((50f / speed) / 50);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (ModuleList.elytraFlight.isEnabled())
            if (ElytraFlight.mode.equals("Timer")) return;
        Managers.TICK_MANAGER.applyTickModifier(1);
    }
}
