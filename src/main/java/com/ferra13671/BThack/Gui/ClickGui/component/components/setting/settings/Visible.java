package com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.ColourUtils;
import com.ferra13671.BThack.Core.Render.RainbowUtils;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.Button;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;

public class Visible extends Checkbox {

    public Visible(Button parent, int offset, Module module) {
        super(null, parent, offset, module);

        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
    }

    @Override
    public void renderComponent() {
        int rainbowType = (int) ClickGui.rainbowSpeed.getValue();
        float speed = RainbowUtils.getRainbowRectSpeed(rainbowType)[0];
        int delay = (int) RainbowUtils.getRainbowRectSpeed(rainbowType)[1];

        BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 15, this.hovered ? ColourUtils.integrateAlpha(new Color(Client.colourTheme.getBackgroundFontHoveredColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() / 1.17)) : ColourUtils.integrateAlpha(new Color(Client.colourTheme.getBackgroundFontColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() / 1.17)));

        if (this.module.visible) {
            BThackRender.drawHorizontalGradientRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth() / 2), parent.parent.getY() + offset + 15, ClickGui.rainbow.getValue() ? ColourUtils.rainbow(delay, speed) : ClickGui.customColor.getValue() ? new Color((int) ClickGui.redColor.getValue(), (int) ClickGui.greenColor.getValue(), (int) ClickGui.blueColor.getValue()).getRGB() : new Color(Client.colourTheme.getFontColour()).hashCode(), new Color(0, 0, 0, 0).hashCode());
        }

        BThackRender.drawString("Visible", parent.parent.getX() + 14, parent.parent.getY() + offset + 4, Client.colourTheme.getModuleDisabledColour());
    }

    @Override
    public void updateDependencies(int offset) {
        //There should be no action here
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = isMouseOnButton(mouseX, mouseY);
        this.y = parent.parent.getY() + offset;
        this.x = parent.parent.getX();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.module.visible = !this.module.visible;
        }
    }
}
