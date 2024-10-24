package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot;


import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.GuiButtonSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.Gui.ActionBot.ActionBotConfigGui;

public class ActionBot extends Module {
    ActionBotRunTimeThread thread;

    public static BooleanSetting repeat;
    public static GuiButtonSetting openConfig;

    public ActionBot() {
        super("ActionBot",
                "Can execute various given commands in alternating order.",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );

        repeat = new BooleanSetting("Repeat", this, false);


        openConfig = new GuiButtonSetting("Open Config", this, new ActionBotConfigGui());

        initSettings(
                repeat,
                openConfig
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        super.onEnable();

        thread = new ActionBotRunTimeThread();
        thread.start();
    }

    @Override
    public void onDisable() {
        if (nullCheck() || thread == null) {
            super.onDisable();
        }

        try {
            thread.stop();
        } catch (Exception ignored) {}
    }
}
