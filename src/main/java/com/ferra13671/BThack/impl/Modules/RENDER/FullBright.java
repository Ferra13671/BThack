package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.LightmapGammaColorEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.awt.*;
import java.util.Arrays;

public class FullBright extends Module {

    public static ModeSetting mode;

    public static BooleanSetting customColor;
    public static NumberSetting red;
    public static NumberSetting green;
    public static NumberSetting blue;

    public FullBright() {
        super("FullBright",
                "lang.module.FullBright",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("Gamma", "Potion"));

        customColor = new BooleanSetting("Custom Color", this, false, () -> mode.getValue().equals("Gamma"));
        red = new NumberSetting("Red", this, 255, 0, 255, true, () -> mode.getValue().equals("Gamma") && customColor.getValue());
        green = new NumberSetting("Green", this, 255, 0, 255, true, () -> mode.getValue().equals("Gamma") && customColor.getValue());
        blue = new NumberSetting("Blue", this, 255, 0, 255, true, () -> mode.getValue().equals("Gamma") && customColor.getValue());

        initSettings(
                mode,

                customColor,
                red,
                green,
                blue
        );
    }

    private boolean hasAppliedNightVision = false;

    @Override
    public void onChangeSetting(Setting setting) {
        arrayListInfo = mode.getValue();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        hasAppliedNightVision = false;

        arrayListInfo = mode.getValue();
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    public void onGammaColor(LightmapGammaColorEvent e) {
        if (!mode.getValue().equals("Gamma")) {
            return;
        } else {
            if (hasAppliedNightVision) {
                if (mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION))
                    mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
                else
                    hasAppliedNightVision = false;
            }
        }
        e.setCancelled(true);
        if (customColor.getValue())
            e.gammaColor = new Color((int) blue.getValue(), (int) green.getValue(), (int) red.getValue()).hashCode();
        else
            e.gammaColor = -1;
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;
        if (mode.getValue().equals("Gamma")) return;
        if (!mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            hasAppliedNightVision = true;
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1000000000, 0));
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (hasAppliedNightVision && mc.player != null) {
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
            hasAppliedNightVision = false;
        }
    }
}
