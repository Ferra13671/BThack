package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Events.Events.LightmapGammaColorEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;

import java.awt.*;

public class GammaColor extends Module {

    public static NumberSetting red;
    public static NumberSetting green;
    public static NumberSetting blue;

    public GammaColor() {
        super("GammaColor",
                "lang.module.GammaColor",
                KeyboardUtils.RELEASE,
                Category.RENDER,
                false
        );

        red = new NumberSetting("Red", this, 255, 0, 255, true);
        green = new NumberSetting("Green", this, 255, 0, 255, true);
        blue = new NumberSetting("Blue", this, 255, 0, 255, true);

        initSettings(
                red,
                green,
                blue
        );
    }

    @EventSubscriber
    public void onGammaColor(LightmapGammaColorEvent e) {
        e.setCancelled(true);
        e.gammaColor = new Color((int) blue.getValue(), (int) green.getValue(), (int) red.getValue()).hashCode();
    }
}
