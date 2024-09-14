package com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.ColourUtils;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.Button;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;

import java.awt.*;

public class Keybind extends AbstractSetting {

	private boolean binding;
	
	public Keybind(Button button, int offset) {
		super(offset, button, null, null);


		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
	}
	
	@Override
	public void renderComponent() {
		BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, this.hovered ? ColourUtils.integrateAlpha(new Color(Client.colourTheme.getBackgroundFontHoveredColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() / 1.17)) : ColourUtils.integrateAlpha(new Color(Client.colourTheme.getBackgroundFontColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() / 1.17)));

		BThackRender.drawString(binding ? "< PRESS KEY >" : ("Key: " + KeyboardUtils.getKeyName(this.parent.mod.getKey())), parent.parent.getX() + 2, parent.parent.getY() + offset + 4, Client.colourTheme.getModuleDisabledColour());
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
		if(isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			this.binding = !this.binding;
		}
	}
	
	@Override
	public void keyTyped(int key) {
		if (this.binding) {
			if (key == KeyboardUtils.KEY_DELETE) {
				this.parent.mod.setKey(0);
				this.binding = false;
			} else if (key != KeyboardUtils.KEY_ESCAPE) {
				this.parent.mod.setKey(key);
				this.binding = false;
			}
		}
	}
}
