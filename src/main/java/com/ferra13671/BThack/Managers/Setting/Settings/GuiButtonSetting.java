package com.ferra13671.BThack.api.Managers.Setting.Settings;

import com.ferra13671.BThack.api.Module.Module;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Supplier;

public class GuiButtonSetting extends Setting {

    private Screen screenValue;

    public GuiButtonSetting(String name, Module module, Screen screen, Supplier<Boolean> dependence) {
        super(name, module, dependence);

        this.screenValue = screen;
    }

    public GuiButtonSetting(String name, Module module, Screen screen) {
        this(name, module, screen, null);
    }

    public Screen getValue() {
        return screenValue;
    }

    public void setValue(Screen value) {
        screenValue = value;
    }


    @Override
    public void toDefault() {
        //nothing
    }
}
