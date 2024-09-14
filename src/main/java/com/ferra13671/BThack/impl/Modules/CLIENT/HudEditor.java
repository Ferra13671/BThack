package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.api.Gui.HudMover.HudMoverScreen;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.text.Text;

public class HudEditor extends Module {

    public HudEditor() {
        super("HudEditor",
                "lang.module.HudEditor",
                KeyboardUtils.RELEASE,
                Category.CLIENT,
                false
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        mc.setScreen(new HudMoverScreen(Text.of("Hud_Mover")));
        toggle();
    }
}
