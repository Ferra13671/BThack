package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.MemorySystem.MemoryManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;

public class CleanMemory extends Module {
    public CleanMemory() {
        super("CleanMemory",
                "lang.module.CleanMemory",
                KeyboardUtils.RELEASE,
                Category.MISC,
                false
        );
    }

    @Override
    public void onEnable() {
        MemoryManager.cleanMemory();
    }
}
