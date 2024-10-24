package com.ferra13671.BThack.api.Module;

import com.ferra13671.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

public class OneActionModule extends Module {

    public OneActionModule(String name, String description, int key, Category c, boolean autoEnabled) {
        super(name, description, key, c, autoEnabled);
    }

    @Override
    public void sendToggleMessage() {
        if (toggled) {
            ChatUtils.sendMessage(this.getName() + ": " + Formatting.YELLOW + "Toggled");
        }
    }

    @Override
    public void toggle() {
        toggled = !toggled;
        if (toggled) {
            sendToggleMessage();
            onEnable();
            onDisable();
            toggled = false;
        } else {
            onDisable();
        }
    }

    @Override
    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        if (this.toggled) {
            sendToggleMessage();
            onEnable();
            onDisable();
            this.toggled = false;
        } else {
            onDisable();
        }
    }
}
