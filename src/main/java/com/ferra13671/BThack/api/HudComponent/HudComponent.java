package com.ferra13671.BThack.api.HudComponent;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.ColourUtils;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.impl.Modules.CLIENT.HUD;
import net.minecraft.client.MinecraftClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HudComponent {
    public final String name;
    public final MinecraftClient mc = MinecraftClient.getInstance();
    public boolean toggled;

    private float x; //Left edge
    private float y; //Upper edge

    private int scaledWidth;
    private int scaledHeight;

    public float width;  //Right
    public float height; //Down

    public final String pattern = "#0.00";

    public final DecimalFormat decimal = new DecimalFormat("0.00");

    public final BooleanSetting parentSetting;

    public HudComponent(String name, float x, float y, boolean autoToggled) {

        this.name = name;

        this.setX(x, mc.getWindow().getScaledWidth());
        this.setY(y, mc.getWindow().getScaledHeight());

        parentSetting = new BooleanSetting(name, Client.hudModule, autoToggled);

        BThack.instance.settingsManager.addModuleSetting(parentSetting);

        initSettings().forEach(setting -> BThack.instance.settingsManager.addModuleSetting(setting));
    }

    public List<Setting> initSettings() {
        return new ArrayList<>();
    }

    public void setX(float value, int scaledWidth) {
        this.x = value;
        this.scaledWidth = scaledWidth;
    }

    public void setY(float value, int scaledHeight) {
        this.y = value;
        this.scaledHeight = scaledHeight;
    }

    public float getX() {
        float factor = (this.x / scaledWidth) * 100;
        return (mc.getWindow().getScaledWidth() / 100f) * factor;
    }

    public float getY() {
        //float factor = (this.y / scaledHeight) * 100;
        //return (mc.getWindow().getScaledHeight() / 100f) * factor;
        return this.y;
    }

    public int getScaledWidth() {
        return this.scaledWidth;
    }

    public int getScaledHeight() {
        return this.scaledHeight;
    }

    public float getNoScaledX() {
        return this.x;
    }

    public float getNoScaledY() {
        return this.y;
    }

    public boolean isEnabled() {
        return toggled;
    }

    public boolean nullCheck() {
        return mc.player == null || mc.world == null;
    }

    public void render(RenderType type) {}

    public String getName() {
        return this.name;
    }

    public void toggle() {
        toggled = !toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public void drawText(String text, int x, int y, int color) {
        BThackRender.drawString(text, x, y, color);
    }

    public void drawText(String text, int x, int y) {
        drawText(text, x, y, ColourUtils.rainbowType((int) HUD.rainbowType.getValue()));
    }

    public ModeSetting addMode(String name, ArrayList<String> options) {
        ModeSetting setting = new ModeSetting(name, Client.hudModule, options, parentSetting::getValue);
        BThack.instance.settingsManager.addModuleSetting(setting);
        return setting;
    }

    public NumberSetting addSlider(String name, double dVal, double min, double max, boolean onlyInt) {
        NumberSetting setting = new NumberSetting(name, Client.hudModule, dVal, min, max, onlyInt, parentSetting::getValue);
        BThack.instance.settingsManager.addModuleSetting(setting);
        return setting;
    }

    public BooleanSetting addCheckbox(String name, boolean bVal) {
        BooleanSetting setting = new BooleanSetting(name, Client.hudModule, bVal, parentSetting::getValue);
        BThack.instance.settingsManager.addModuleSetting(setting);
        return setting;
    }


    public enum RenderType {
        IMAGE,
        TEXT
    }
}
