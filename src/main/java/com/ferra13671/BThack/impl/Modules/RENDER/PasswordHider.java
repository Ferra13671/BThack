package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class PasswordHider extends Module {

    public PasswordHider() {
        super("PasswordHider",
                "lang.module.PasswordHider",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );
    }
}
