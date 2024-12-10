package com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ColorSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;

@Deprecated
public class ColorPicker extends AbstractSetting {

    private final ColorSetting set;

    public ColorPicker(ColorSetting op, ModuleButton button , int offset, Module module) {
        super(offset, button, module, op);

        set = op;
    }

    @Override
    public int getHeight() {
        return 70;
    }

    @Override
    public void renderComponent() {
        if (!getVisible()) return;

        BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + getHeight(), ColorUtils.integrateAlpha(new Color(Client.clientInfo.getColorTheme().getBackgroundFontColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() * Math.min(1, ClickGui.opacity.getValue() + 0.13))));

        Color rgbColor = set.getValue();
        float[] hsbColor = Color.RGBtoHSB(rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue(), null);

        BThackRender.draw4ColorRect(parent.parent.getX() + 5, parent.parent.getY() + offset + 5, parent.parent.getX() + 55, parent.parent.getY() + offset + 55, new Color(Color.HSBtoRGB(hsbColor[0], 0, 1)).getRGB(), rgbColor.getRGB(), new Color(Color.HSBtoRGB(hsbColor[0], 1, 0)).getRGB(), new Color(Color.HSBtoRGB(hsbColor[0], 0, 0)).getRGB());
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        if (!getVisible()) return true;
        return false;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (!this.getVisible()) return false;

        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }
}
