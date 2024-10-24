package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;

public class NoSlow extends Module {

    public static BooleanSetting useItems;
    public static BooleanSetting soulSand;
    public static BooleanSetting slime;
    public static BooleanSetting honey;

    public NoSlow() {
        super("NoSlow",
                "lang.module.NoSlow",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );


        useItems = new BooleanSetting("Use Items", this, true);
        soulSand = new BooleanSetting("SoulSand", this, false);
        slime = new BooleanSetting("Slime", this, false);
        honey = new BooleanSetting("Honey", this, false);

        initSettings(
                useItems,
                soulSand,
                slime,
                honey
        );
    }
}
