package com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;

public class ModeButton extends AbstractSetting {

	private final ModeSetting set;

	public ModeButton(ModeSetting option, ModuleButton button, int offset, int modeIndex, Module module) {
		super(offset, button, module, option);
		set = option;
		x = button.parent.getX() + button.parent.getWidth();
		y = button.parent.getY() + button.offset;

		set.setValue(option.getOptions().get(modeIndex));
	}
	
	@Override
	public void renderComponent() {
		if (!getVisible()) return;

		BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, this.hovered ? ColorUtils.integrateAlpha(new Color(Client.clientInfo.getColorTheme().getBackgroundFontHoveredColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() * Math.min(1, ClickGui.opacity.getValue() + 0.13))) : ColorUtils.integrateAlpha(new Color(Client.clientInfo.getColorTheme().getBackgroundFontColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() * Math.min(1, ClickGui.opacity.getValue() + 0.13))));

		BThackRender.drawString(this.op.getName() + ": " + getModeString(), parent.parent.getX() + 2, parent.parent.getY() + offset + 4, Client.clientInfo.getColorTheme().getModuleDisabledColour());
	}

	private String getModeString() {
		return (!set.getOptions().contains(set.getValue()) ? "NULL" : (set.getOptions().size() < set.getIndex() ? set.getValue() : set.getOptions().get(set.getIndex())));
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

		if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			int maxIndex = set.getOptions().size();

			if (set.getIndex() + 1 >= maxIndex) {
				set.setIndex(0);
			} else {
				int currentIndex = set.getIndex();
				set.setIndex(currentIndex + 1);
			}

			set.setValue(set.getOptions().get(set.getIndex()));
			set.module.onChangeSetting(set);
		}

		return isMouseOnButton(mouseX, mouseY);
	}
}
