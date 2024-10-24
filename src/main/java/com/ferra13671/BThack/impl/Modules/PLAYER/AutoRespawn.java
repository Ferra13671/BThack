package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;

/**
 * @see com.ferra13671.BThack.mixins.MixinDeathScreen
 */

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn",
                "lang.module.AutoRespawn",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );
    }
}