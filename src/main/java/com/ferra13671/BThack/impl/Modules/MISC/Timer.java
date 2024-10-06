package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;

import java.util.ArrayList;
import java.util.Objects;

public class Timer extends Module {

    public static ModeSetting mode;
    public static NumberSetting tickSpeed;
    public static NumberSetting microTicks;

    public Timer() {
        super("Timer",
                "lang.module.Timer",
                KeyboardUtils.RELEASE,
                Category.MISC,
                false
        );

        ArrayList<String> options = new ArrayList<>();

        options.add("Fast");
        options.add("Slow");
        options.add("Micro Tick");

        mode = new ModeSetting("Mode", this, options);
        tickSpeed = new NumberSetting("Tick speed", this, 2,1,10,false);
        microTicks = new NumberSetting("Micro Ticks", this, 0, -10, 10, false, () -> mode.getValue().equals("Micro Tick"));

        initSettings(
                mode,
                tickSpeed,
                microTicks
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        ThreadManager.startNewThread(thread -> {
            while (this.isEnabled()) {
                String Mode = mode.getValue();
                float speed = (float) tickSpeed.getValue();
                if (Objects.equals(Mode, "Fast")) {
                    mc.renderTickCounter.tickTime = 50f / speed;
                } else if (Objects.equals(Mode, "Slow")) {
                    mc.renderTickCounter.tickTime = 50f * speed;
                } else {
                    byte microSpeed = (byte) microTicks.getValue();
                    mc.renderTickCounter.tickTime = 50f * microSpeed;

                }

                try {
                    thread.sleep(50);
                } catch (InterruptedException ignored) {}
            }

            mc.renderTickCounter.tickTime = 50f;
        });
    }
}
