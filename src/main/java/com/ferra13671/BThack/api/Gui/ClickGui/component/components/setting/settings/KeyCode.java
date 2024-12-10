package com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.KeyCodeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;

public class KeyCode extends AbstractSetting {

    private final KeyCodeSetting set;

    private boolean binding;

    public KeyCode(ModuleButton button, int offset, KeyCodeSetting option, Module module) {
        super(offset, button, module, option);
        set = option;
        x = button.parent.getX() + button.parent.getWidth();
        y = button.parent.getY() + button.offset;
    }

    @Override
    public void renderComponent() {
        if (!getVisible()) return;

        BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, this.hovered ? ColorUtils.integrateAlpha(new Color(Client.clientInfo.getColorTheme().getBackgroundFontHoveredColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() * Math.min(1, ClickGui.opacity.getValue() + 0.13))) : ColorUtils.integrateAlpha(new Color(Client.clientInfo.getColorTheme().getBackgroundFontColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() * Math.min(1, ClickGui.opacity.getValue() + 0.13))));

        BThackRender.drawString(binding ? "< PRESS KEY >" : (op.getName() + ": " + KeyboardUtils.getKeyName(set.getValue())), parent.parent.getX() + 2, parent.parent.getY() + offset + 4, Client.clientInfo.getColorTheme().getModuleDisabledColour());
    }

    @Override
    public boolean updateComponent(int mouseX, int mouseY) {
        if (!getVisible() || !parent.open) return true;

        hovered = isMouseOnButton(mouseX, mouseY);
        y = parent.parent.getY() + offset;
        x = parent.parent.getX();

        return true;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (!getVisible() || !parent.open) return false;

        if(isMouseOnButton(mouseX, mouseY) && button == 0 && parent.open) {
            binding = !binding;
        }

        return isMouseOnButton(mouseX, mouseY);
    }

    @Override
    public void keyTyped(int key) {
        if (!getVisible()) return;

        if (binding) {
            if (key == KeyboardUtils.KEY_DELETE) {
                set.setValue(0);
                set.module.onChangeSetting(set);
                binding = false;
            } else if (key != KeyboardUtils.KEY_ESCAPE) {
                set.setValue(key);
                set.module.onChangeSetting(set);
                binding = false;
            }
        }
    }
}
