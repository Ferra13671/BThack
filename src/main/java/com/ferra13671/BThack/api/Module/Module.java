package com.ferra13671.BThack.api.Module;

import com.ferra13671.BTbot.api.Utils.Controller.ClientPlayerController;
import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.ChatNotifications;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

public class Module {
    public final String name;
    private final String description;
    public boolean toggled;
    private final boolean autoEnabled;
    private int keyCode;
    private final Category category;
    public final MinecraftClient mc = MinecraftClient.getInstance();
    public final ClientPlayerController pc = BThack.instance.playerController;

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

    public void sendNotification(String text) {
        if (Client.chatNotifications.isEnabled() && ChatNotifications.moduleMessages.getValue()) {
            ChatUtils.sendMessage(getChatName() + " " + text);
        }
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
            sendToggleMessage();
            onEnable();
        } else {
            sendToggleMessage();
            onDisable();
        }
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        if (this.toggled) {
            sendToggleMessage();
            onEnable();
        } else {
            sendToggleMessage();
            onDisable();
        }
    }

    public void initSettings(Setting... settings) {
        for (Setting setting : settings)
            BThack.instance.settingsManager.addModuleSetting(setting);
    }

    public void sendToggleMessage() {
        if (Client.chatNotifications.isEnabled() && ChatNotifications.moduleToggle.getValue()) {
            if (toggled) {
                ChatUtils.sendMessage(this.getName() + ": " + Formatting.GREEN + "Enabled");
            } else {
                ChatUtils.sendMessage(this.getName() + ": " + Formatting.RED + "Disabled");
            }
        }
    }

}
