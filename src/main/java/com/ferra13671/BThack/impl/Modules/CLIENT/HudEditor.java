package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Gui.HudMover.HudMoverScreen;
import com.ferra13671.BThack.api.Module.OneActionModule;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;

public class HudEditor extends OneActionModule {

    public HudEditor() {
        super("HudEditor",
                "lang.module.HudEditor",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                false
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        mc.setScreen(new HudMoverScreen());
    }
}
