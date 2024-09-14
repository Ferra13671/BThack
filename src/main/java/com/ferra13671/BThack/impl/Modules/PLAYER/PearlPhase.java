package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;

public class PearlPhase extends Module {

    public static ModeSetting activeMode;
    public static NumberSetting pitch;

    public static BooleanSetting withFire;
    public static BooleanSetting withShift;

    public PearlPhase() {
        super("PearlPhase",
                "",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );
    }
}
