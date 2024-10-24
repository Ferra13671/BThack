package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.ColourThemes.ColourTheme;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.OneActionModule;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;

import java.util.ArrayList;

public class ClickGui extends OneActionModule {

    public static int x = 0;

    public static ModeSetting activeTheme;
    public static NumberSetting redColor;
    public static NumberSetting greenColor;
    public static NumberSetting blueColor;
    public static BooleanSetting customColor;
    public static BooleanSetting rainbow;
    public static NumberSetting rainbowSpeed;

    public static NumberSetting effectDelayS;
    public static NumberSetting effectDelay;

    public ClickGui() {
        super("ClickGui",
                "lang.module.ClickGui",
                KeyboardUtils.KEY_RSHIFT,
                Category.CLIENT,
                false
        );

        ArrayList<String> options = new ArrayList<>();

        for (ColourTheme theme : BThack.instance.colourThemeManager.getColourThemes()) {
            options.add(theme.getName());
        }

        activeTheme = new ModeSetting("Active theme", this, options);

        redColor = new NumberSetting("Red", this, 25,0,255,true, () -> customColor.getValue());
        greenColor = new NumberSetting("Green", this, 28,0,255,true, () -> customColor.getValue());
        blueColor = new NumberSetting("Blue", this, 255,0,255,true, () -> customColor.getValue());
        customColor = new BooleanSetting("Custom Color", this, false, () -> !rainbow.getValue());
        rainbow = new BooleanSetting("Rainbow", this, false);
        rainbowSpeed = new NumberSetting("Rainbow speed", this, 2, 1, 4, true, () -> rainbow.getValue());

        effectDelayS = new NumberSetting("Effect DelayS", this, 2, 0.5, 4, false);
        effectDelay = new NumberSetting("Effect Delay", this, 10, 3, 20, true);

        initSettings(
                activeTheme,
                redColor,
                greenColor,
                blueColor,
                customColor,
                rainbow,
                rainbowSpeed,
                effectDelayS,
                effectDelay
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        if (mc.currentScreen == null) {
            BThack.instance.clickGui.firstIgnore = true;
            mc.setScreen(BThack.instance.clickGui);
        }

        toggle();
    }
}
