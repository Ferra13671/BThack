package com.ferra13671.BThack.api.Module;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import net.minecraft.client.MinecraftClient;

public class Module {
    public final String name;
    private final String description;
    public boolean toggled;
    private final boolean autoEnabled;
    private int keyCode;
    private final Category category;
    public final MinecraftClient mc = MinecraftClient.getInstance();

    public String arrayListInfo = "";

    public boolean visible = true;
    public boolean allowRemapKeyCode = true;

    public Module(String name, String description, int key, Category c, boolean autoEnabled) {
        this.name = name;
        this.description = description.endsWith(".") ? description : !description.startsWith(".lang") ? description : description + ".";
        this.keyCode = key;
        this.category = c;
        this.autoEnabled = autoEnabled;
    }

    public boolean isEnabled() {
        return toggled;
    }

    public boolean isAutoEnabled() {
        return autoEnabled;
    }

    public int getKey() {
        return keyCode;
    }

    public static boolean nullCheck() {
        MinecraftClient mc = MinecraftClient.getInstance();
        return mc.player == null || mc.world == null;
    }

    public void onEnable() {
        BThack.EVENT_BUS.register(this);
    }

    public void onDisable() {
        BThack.EVENT_BUS.unregister(this);
    }


    public void setKey(int key) {
        this.keyCode = key;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        if (description.startsWith("lang.")) {
            return LanguageSystem.translate(description);
        } else {
            return this.description;
        }
    }

    public String getChatName() {
        return "[" + this.name + "]";
    }


    public enum Category {
        COMBAT,
        MISC,
        CLIENT,
        RENDER,
        MOVEMENT,
        PLAYER,
        WORLD
    }

    public void toggle() {
        toggled = !toggled;
        if (toggled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        if (this.toggled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public void initSettings(Setting... settings) {
        for (Setting setting : settings)
            BThack.instance.settingsManager.addModuleSetting(setting);
    }
}
