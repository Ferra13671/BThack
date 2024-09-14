package com.ferra13671.BThack.api.Gui.ClickGui.component.components;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.ColourUtils;
import com.ferra13671.BThack.Core.Render.RainbowUtils;
import com.ferra13671.BThack.api.Gui.ClickGui.ClickGuiScreen;
import com.ferra13671.BThack.api.Gui.ClickGui.component.Component;
import com.ferra13671.BThack.api.Gui.ClickGui.component.Frame;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings.*;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings.Checkbox;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.PluginModule;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;
import java.util.ArrayList;

public class Button extends Component implements Mc {

	public Module mod;
	public Frame parent;
	public int offset;
	private boolean isHovered;
	private final ArrayList<AbstractSetting> settings;
	public boolean open;
	public int height;

	public Button(Module mod, Frame parent, int offset) {
		this.mod = mod;
		this.parent = parent;
		this.offset = offset;
		height = 12;
		settings = new ArrayList<>();
		open = false;
		int opY = offset + 12;
		AbstractSetting setting;
		if(BThack.instance.settingsManager.getSettingsByMod(mod) != null) {
			for(Setting s : BThack.instance.settingsManager.getSettingsByMod(mod)){
				setting = s instanceof ModeSetting set ? new ModeButton(set, this, opY, set.getIndex(), mod) :
					    	s instanceof NumberSetting set ? new Slider(set, this, opY, mod) :
								  s instanceof BooleanSetting set ? new Checkbox(set, this, opY, mod) :
										s instanceof KeyCodeSetting set ? new KeyCode(this, opY, set, mod) :
												s instanceof GuiButtonSetting set ? new OpenGuiButton(set, this, opY, mod) : null;
				settings.add(setting);
				opY += setting.getHeight();
			}
		}

		setting = new Visible(this, opY, mod);
		settings.add(setting);
		if (mod.allowRemapKeyCode) {
			opY += setting.getHeight();
			settings.add(new Keybind(this, opY));
		}
	}

	@Override
	public void setOff(int newOff) {
		offset = newOff;
		int opY = offset + 12;
		for(AbstractSetting comp : settings) {
			comp.setOff(opY);
			if (comp.getVisible())
				opY += comp.getHeight();
		}
	}

	@Override
	public void updateDependencies(int offset) {
		for (Component comp : settings) {
			comp.updateDependencies(0);
		}

		this.offset = offset;
		int opY = offset + 12;
		for(AbstractSetting comp : settings) {
			comp.setOff(opY);
			if (comp.getVisible())
				opY += comp.getHeight();
		}
	}

	@Override
	public void renderComponent() {
		int rainbowType = (int) ClickGui.rainbowSpeed.getValue();
		float speed = RainbowUtils.getRainbowRectSpeed(rainbowType)[0];
		int delay = (int) RainbowUtils.getRainbowRectSpeed(rainbowType)[1];

		if (effect.getColor().getAlpha() != 0) {
			BThackRender.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + 12 + offset, isHovered ? ColourUtils.integrateAlpha(new Color(Client.colourTheme.getBackgroundFontHoveredColour()).hashCode(), (int) (effect.getColor().getAlpha() / 1.3)) : ColourUtils.integrateAlpha(new Color(Client.colourTheme.getBackgroundFontColour()).hashCode(), (int) (effect.getColor().getAlpha() / 1.3)));
			BThackRender.drawString(mod.getName(), (parent.getX() + 5), (parent.getY() + offset + 2), mod.isEnabled() ? ClickGui.rainbow.getValue() ? ColourUtils.integrateAlpha(ColourUtils.rainbow(delay, speed), effect.getColor().getAlpha()) : ClickGui.customColor.getValue() ? ColourUtils.integrateAlpha(new Color((int) ClickGui.redColor.getValue(), (int) ClickGui.greenColor.getValue(), (int) ClickGui.blueColor.getValue()).getRGB(), effect.getColor().getAlpha()) : ColourUtils.integrateAlpha(new Color(Client.colourTheme.getModuleEnabledColour()).hashCode(), effect.getColor().getAlpha()) : ColourUtils.integrateAlpha(new Color(Client.colourTheme.getModuleDisabledColour()).hashCode(), effect.getColor().getAlpha())); //0x999999
			if (settings.size() >= 2) {
				BThackRender.drawString(open ? "-" : "+", (parent.getX() + parent.getWidth() - 10), (parent.getY() + offset + 2), ColourUtils.integrateAlpha(new Color(Client.colourTheme.getModuleDisabledColour()).hashCode(), effect.getColor().getAlpha()));
			}
		}
		if(open) {
			if(!settings.isEmpty()) {
				for(Component comp : settings) {
					comp.renderComponent();
				}
			}
		}
		if (isHovered) {
			int length;
			if (mod instanceof PluginModule pluginMod) {
				int pluginNameLength = mc.textRenderer.getWidth("Plugin: " + pluginMod.plugin.pluginName) + 10;
				int descriptionLength = mc.textRenderer.getWidth(mod.getDescription()) + 10;
				length = Math.max(pluginNameLength, descriptionLength);
				BThackRender.drawRect(1, ClickGuiScreen.descriptionY - 17, length, (ClickGuiScreen.descriptionY + 12), new Color(Client.colourTheme.getBackgroundFontColour()).hashCode());
				BThackRender.drawOutlineRect(1, ClickGuiScreen.descriptionY - 17, length, (ClickGuiScreen.descriptionY + 12), 1, ColourUtils.rainbow(delay, speed));
				BThackRender.drawString(mod.getDescription(), 6, (ClickGuiScreen.descriptionY - 1), Client.colourTheme.getModuleDisabledColour());
				BThackRender.drawString("Plugin: " + pluginMod.plugin.pluginName, 6, (ClickGuiScreen.descriptionY - 12), Client.colourTheme.getModuleDisabledColour());
			} else {
				length = mc.textRenderer.getWidth(mod.getDescription()) + 10;
				BThackRender.drawRect(1, ClickGuiScreen.descriptionY - 5, length, (ClickGuiScreen.descriptionY + 12), new Color(Client.colourTheme.getBackgroundFontColour()).hashCode());
				BThackRender.drawOutlineRect(1, ClickGuiScreen.descriptionY - 5, length, (ClickGuiScreen.descriptionY + 12), 1, ColourUtils.rainbow(delay, speed));
				BThackRender.drawString(mod.getDescription(), 6, (ClickGuiScreen.descriptionY - 1), Client.colourTheme.getModuleDisabledColour());
			}
		}
	}

	@Override
	public int getHeight() {
		if(open) {
			int height = 12;
			for (AbstractSetting component : settings) {
				if (component.getVisible()) {
					height += component.getHeight();
				}
			}
			return height;
		}
		return 12;
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.isHovered = isMouseOnButton(mouseX, mouseY);
		if(!settings.isEmpty()) {
			for(Component comp : settings) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0) {
			mod.toggle();
		}
		if(isMouseOnButton(mouseX, mouseY) && button == 1) {
			open = !open;
			parent.refresh();
		}
		for(Component comp : settings) {
			comp.mouseClicked(mouseX, mouseY, button);
		}
		if (open)
			parent.updateDependencies();
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for(Component comp : settings) {
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		}
		if (open)
			parent.updateDependencies();
	}

	@Override
	public void keyTyped(int key) {
		for(Component comp : this.settings) {
			comp.keyTyped(key);
		}
	}

	public boolean isMouseOnButton(int x, int y) {
        return x > parent.getX() && x < parent.getX() + parent.getWidth() && y > parent.getY() + offset && y < parent.getY() + 12 + offset;
    }
}
