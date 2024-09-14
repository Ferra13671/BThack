package com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.ColourUtils;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.Button;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;

import java.awt.*;

public class ModeButton extends AbstractSetting {

	private final ModeSetting set;

	public ModeButton(ModeSetting option, Button button, int offset, int modeIndex, Module module) {
		super(offset, button, module, option);
		set = option;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;

		set.setValue(option.getOptions().get(modeIndex));
	}
	
	@Override
	public void renderComponent() {
		if (!this.getVisible()) return;

		BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, this.hovered ? ColourUtils.integrateAlpha(new Color(Client.colourTheme.getBackgroundFontHoveredColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() / 1.17)) : ColourUtils.integrateAlpha(new Color(Client.colourTheme.getBackgroundFontColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() / 1.17)));

		BThackRender.drawString(this.op.getName() + ": " + set.getOptions().get(set.getIndex()), parent.parent.getX() + 2, parent.parent.getY() + offset + 4, Client.colourTheme.getModuleDisabledColour());
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

		if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			int maxIndex = set.getOptions().size();

			if (set.getIndex() + 1 >= maxIndex)
				set.setIndex(0);
			else {
				int currentIndex = set.getIndex();
				set.setIndex(currentIndex + 1);
			}

			set.setValue(set.getOptions().get(set.getIndex()));
		}
	}
}
