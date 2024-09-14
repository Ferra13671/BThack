package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;

public class MemoryCleaner extends Module {

    public static BooleanSetting showMessages;

    public MemoryCleaner() {
        super("MemoryCleaner",
                "lang.module.MemoryCleaner",
                KeyboardUtils.RELEASE,
                Category.CLIENT,
                true
        );

        allowRemapKeyCode = false;

        showMessages = new BooleanSetting("Show Messages", this, true);

        initSettings(showMessages);
    }
}
