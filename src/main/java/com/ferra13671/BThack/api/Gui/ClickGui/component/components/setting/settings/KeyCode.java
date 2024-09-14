package com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.ColourUtils;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.Button;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.KeyCodeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;

import java.awt.*;

public class KeyCode extends AbstractSetting {

    private final KeyCodeSetting set;

    private boolean binding;

    public KeyCode(Button button, int offset, KeyCodeSetting option, Module module) {
        super(offset, button, module, option);
        set = option;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
    }

    @Override
    public void renderComponent() {
        if (!this.getVisible()) return;

        BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, this.hovered ? ColourUtils.integrateAlpha(new Color(Client.colourTheme.getBackgroundFontHoveredColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() / 1.17)) : ColourUtils.integrateAlpha(new Color(Client.colourTheme.getBackgroundFontColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() / 1.17)));

        BThackRender.drawString(binding ? "< PRESS KEY >" : (op.getName() + ": " + KeyboardUtils.getKeyName(set.getValue())), parent.parent.getX() + 2, parent.parent.getY() + offset + 4, Client.colourTheme.getModuleDisabledColour());
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        if (!this.getVisible() || !parent.open) return;

        this.hovered = isMouseOnButton(mouseX, mouseY);
        this.y = parent.parent.getY() + offset;
        this.x = parent.parent.getX();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (!this.getVisible() || !parent.open) return;

        if(isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.binding = !this.binding;
        }
    }

    @Override
    public void keyTyped(int key) {
        if (!this.getVisible()) return;

        if (this.binding) {
            if (key == KeyboardUtils.KEY_DELETE) {
                set.setValue(0);
                this.binding = false;
            } else if (key != KeyboardUtils.KEY_ESCAPE) {
                set.setValue(key);
                this.binding = false;
            }
        }
    }
}
