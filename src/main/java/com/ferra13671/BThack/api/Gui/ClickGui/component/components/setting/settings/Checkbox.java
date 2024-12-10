package com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;

public class Checkbox extends AbstractSetting {

	private final BooleanSetting set;
	
	public Checkbox(BooleanSetting option, ModuleButton button, int offset, Module module) {
		super(offset, button, module, option);
		set = option;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
	}

	@Override
	public void renderComponent() {
		if (!getVisible()) return;

		BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 15, this.hovered ? ColorUtils.integrateAlpha(new Color(Client.clientInfo.getColorTheme().getBackgroundFontHoveredColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() * Math.min(1, ClickGui.opacity.getValue() + 0.13))) : ColorUtils.integrateAlpha(new Color(Client.clientInfo.getColorTheme().getBackgroundFontColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() * Math.min(1, ClickGui.opacity.getValue() + 0.13))));

		if (set.getValue()) {
			BThackRender.drawHorizontalGradientRect(parent.parent.getX() + 1, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth() / 2f), parent.parent.getY() + offset + 15, ClickGui.getClickGuiColor(true), new Color(0, 0, 0, 0).hashCode());
		}

		BThackRender.drawString(op.getName(), parent.parent.getX() + 7, parent.parent.getY() + offset + 4, Client.clientInfo.getColorTheme().getModuleDisabledColour());
	}
	
	@Override
	public boolean updateComponent(int mouseX, int mouseY) {

		if (!getVisible()) return true;

		hovered = isMouseOnButton(mouseX, mouseY);
		y = parent.parent.getY() + offset;
		x = parent.parent.getX();
		return true;
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if (!getVisible()) return false;

		if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			set.setValue(!set.getValue());
			set.module.onChangeSetting(set);
		}

		return isMouseOnButton(mouseX, mouseY);
	}
}
