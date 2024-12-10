package com.ferra13671.BThack.api.Gui.ClickGui.component.components;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Gui.ClickGui.component.Component;
import com.ferra13671.BThack.api.Gui.ClickGui.component.Frame;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings.*;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings.Checkbox;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;
import java.util.ArrayList;

public class ModuleButton extends Component implements Mc {

	public Module module;
	public Frame parent;
	public int offset;
	public boolean open;
	public int height;

	private boolean isHovered;
	private final ArrayList<AbstractSetting> settings;
	private float alphaDelta = 1;
	private boolean alphaDeltaInverse = true;
	private int outlineHeight = 0;

	public ModuleButton(Module module, Frame parent, int offset) {
		this.module = module;
		this.parent = parent;
		this.offset = offset;
		height = 12;
		settings = new ArrayList<>();
		open = false;
		int opY = offset + 12;
		AbstractSetting setting;
		if(Managers.SETTINGS_MANAGER.getSettingsByMod(module) != null) {
			for(Setting s : Managers.SETTINGS_MANAGER.getSettingsByMod(module)){
				setting = s instanceof ModeSetting set ? new ModeButton(set, this, opY, set.getIndex(), module) :
					    	s instanceof NumberSetting set ? new Slider(set, this, opY, module) :
								  s instanceof BooleanSetting set ? new Checkbox(set, this, opY, module) :
										s instanceof KeyCodeSetting set ? new KeyCode(this, opY, set, module) :
												s instanceof GuiButtonSetting set ? new OpenGuiButton(set, this, opY, module) :
														s instanceof ColorSetting set ? new ColorPicker(set, this, opY, module) :
																null;
				settings.add(setting);
				opY += setting.getHeight();
			}
		}

		setting = new Visible(this, opY, module);
		settings.add(setting);
		if (module.allowRemapKeyCode) {
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
		outlineHeight = 0;
		for (AbstractSetting comp : settings) {
			comp.updateDependencies(0);
			if (comp.getVisible())
				outlineHeight += comp.getHeight();
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
		if (effect.getColor().getAlpha() != 0) {
			BThackRender.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + 12 + offset, isHovered ? ColorUtils.integrateAlpha(new Color(Client.clientInfo.getColorTheme().getBackgroundFontHoveredColour()).hashCode(), (int) (effect.getColor().getAlpha() * ClickGui.opacity.getValue())) : ColorUtils.integrateAlpha(new Color(Client.clientInfo.getColorTheme().getBackgroundFontColour()).hashCode(), (int) (effect.getColor().getAlpha() * ClickGui.opacity.getValue())));
			if (ClickGui.moduleOutline.getValue())
				BThackRender.drawOutlineRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + 12 + offset, 1, isHovered ? ColorUtils.integrateAlpha(new Color(Client.clientInfo.getColorTheme().getBackgroundFontHoveredColour()).hashCode(), (int) (effect.getColor().getAlpha() * ClickGui.opacity.getValue())) : ColorUtils.integrateAlpha(new Color(Client.clientInfo.getColorTheme().getBackgroundFontColour()).hashCode(), (int) (effect.getColor().getAlpha() * ClickGui.opacity.getValue())));
			BThackRender.drawString(module.getName(), (parent.getX() + 5), (parent.getY() + offset + 2), module.isEnabled() ? ColorUtils.integrateAlpha(ClickGui.getClickGuiColor(true), effect.getColor().getAlpha()) : ColorUtils.integrateAlpha(new Color(Client.clientInfo.getColorTheme().getModuleDisabledColour()).hashCode(), effect.getColor().getAlpha())); //0x999999

			if (settings.size() >= 2)
				BThackRender.drawString(open ? "-" : "+", (parent.getX() + parent.getWidth() - 10), (parent.getY() + offset + 2), ColorUtils.integrateAlpha(new Color(Client.clientInfo.getColorTheme().getModuleDisabledColour()).hashCode(), effect.getColor().getAlpha()));
		}
		if(open) {
			if(!settings.isEmpty()) {
				for(Component comp : settings) {
					comp.renderComponent();
				}
				if (ClickGui.settingsOutline.getValue()) {
					BThackRender.guiGraphics.getMatrices().translate(0, 0, 4);
					BThackRender.drawOutlineRect(parent.getX(), parent.getY() + offset, parent.getX() + parent.getWidth(), parent.getY() + offset + outlineHeight + 12, 1, new Color(255, 255, 255, Math.min(255, Math.max(1, (int) (alphaDelta * 255)))).hashCode());
					BThackRender.guiGraphics.getMatrices().translate(0, 0, -4);
				}
			}
		}
		if (isHovered)
			BThack.instance.clickGui.descriptionModule = module;
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
	public boolean updateComponent(int mouseX, int mouseY) {
		isHovered = isMouseOnButton(mouseX, mouseY);
		if(!settings.isEmpty()) {
			for(Component comp : settings) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
		return !isHovered;
	}

	@Override
	public void tick() {
		super.tick();
		if (open) {
			if (alphaDelta > 1) alphaDeltaInverse = true;
			if (alphaDelta <= 0.3) alphaDeltaInverse = false;
			if (alphaDeltaInverse) alphaDelta -= 0.01f;
			else alphaDelta += 0.01f;
		}
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0)
			module.toggle();
		if(isMouseOnButton(mouseX, mouseY) && button == 1) {
			open = !open;
			parent.refresh();
		}
		for(Component comp : settings) {
			comp.mouseClicked(mouseX, mouseY, button);
		}
		if (open)
			parent.updateDependencies();

		return isMouseOnButton(mouseX, mouseY);
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
        return x > ClickGui.applyGuiScale(parent.getX()) && x < ClickGui.applyGuiScale(parent.getX() + parent.getWidth()) &&
				y > ClickGui.applyGuiScale(parent.getY() + offset) && y < ClickGui.applyGuiScale(parent.getY() + 12 + offset);
    }

	public void resetHovered() {
		isHovered = false;
	}
}
