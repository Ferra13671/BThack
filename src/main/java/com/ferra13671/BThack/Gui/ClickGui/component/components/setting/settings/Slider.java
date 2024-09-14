package com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings;


import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.ColourUtils;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.Button;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.AbstractSetting;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;
import com.google.common.collect.Sets;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public class Slider extends AbstractSetting implements Mc {

	private final NumberSetting set;

	private boolean dragging = false;

	private boolean writing = false;
	private StringBuilder textBuilder = new StringBuilder();

	private final Set<Integer> keys = Sets.newHashSet(
			KeyboardUtils.KEY_0,
			KeyboardUtils.KEY_1,
			KeyboardUtils.KEY_2,
			KeyboardUtils.KEY_3,
			KeyboardUtils.KEY_4,
			KeyboardUtils.KEY_5,
			KeyboardUtils.KEY_6,
			KeyboardUtils.KEY_7,
			KeyboardUtils.KEY_8,
			KeyboardUtils.KEY_9
	);

	private double renderWidth;

	public Slider(NumberSetting option, Button button, int offset, Module module) {
		super(offset, button, module, option);
		set = option;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
	}

	@Override
	public void renderComponent() {
		if (!this.getVisible()) return;

		BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 15, ColourUtils.integrateAlpha(new Color(Client.colourTheme.getBackgroundFontColour()).hashCode(), (int) (parent.effect.getColor().getAlpha() / 1.17)));

		BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX(), parent.parent.getY() + offset + 15, new Color(Client.colourTheme.getBackgroundFontColour()).hashCode());
		BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset + 11, parent.parent.getX() + 100, parent.parent.getY() + offset + 15, Color.GRAY.darker().darker().darker().getRGB());

		if (ClickGui.rainbow.getValue()) {
			int type = (int) ClickGui.rainbowSpeed.getValue();
			BThackRender.drawHorizontalRainbowRect(parent.parent.getX(), parent.parent.getY() + offset + 11, parent.parent.getX() + (int) renderWidth, parent.parent.getY() + offset + 15, type);
		} else {
			BThackRender.drawRect(parent.parent.getX(), parent.parent.getY() + offset + 11, parent.parent.getX() + (int) renderWidth, parent.parent.getY() + offset + 15, ClickGui.customColor.getValue() ? new Color((int) ClickGui.redColor.getValue(), (int) ClickGui.greenColor.getValue(), (int) ClickGui.blueColor.getValue()).getRGB() : new Color(Client.colourTheme.getFontColour()).hashCode());
		}

		BThackRender.drawString(this.op.getName() + ": " + set.getValue(), parent.parent.getX() + 2, (parent.parent.getY() + offset + 1), Client.colourTheme.getModuleDisabledColour());


		if (writing) {
			int scaleWidth = mc.getWindow().getScaledWidth();

			BThackRender.drawString("New Value: " + textBuilder, (int) ((scaleWidth / 2f) - (mc.textRenderer.getWidth("New Value: " + textBuilder) / 2)), (mc.getWindow().getScaledHeight() - 20), Color.white.hashCode());
		}
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		if (!this.getVisible() || !parent.open) return;

		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();

		double diff = Math.min(100, Math.max(0, mouseX - this.x));

		double min = set.getMinValue();
		double max = set.getMaxValue();

		renderWidth = (100) * (set.getValue() - min) / (max - min);

		if (dragging) {
			if (diff == 0) {
				set.setValue(set.getMinValue());
			} else {
				double newValue = roundToPlace(((diff / 100) * (max - min) + min), 2);
				set.setValue(newValue);
			}
		}
	}

	private static double roundToPlace(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {

		if (!this.getVisible() || !parent.open) {
			this.writing = false;
			return;
		}

		if (button != 2) {
			if (isMouseOnButton(mouseX, mouseY)) {
				dragging = true;
				writing = false;
			}
		} else {
			if (isMouseOnButton(mouseX, mouseY)) {
				writing = !writing;
			}
		}
	}

	@Override
	public void keyTyped(int key) {
		if (!this.writing) return;

		if (key == KeyboardUtils.KEY_MINUS) {
			if (textBuilder.isEmpty())
				textBuilder.append((char) key);
		}

		if (keys.contains(key) || (char) key == '.') {
			textBuilder.append((char) key);
		}
		if (key == KeyboardUtils.KEY_BACKSPACE) {
			if (!textBuilder.isEmpty())
				textBuilder.deleteCharAt(textBuilder.length() - 1);
		}

		if (key == KeyboardUtils.KEY_ENTER) {
			double number = getNumber();
			textBuilder = new StringBuilder();

			writing = false;

			if (number < set.getMinValue())
				number = set.getMinValue();
			if (number > set.getMaxValue())
				number = set.getMaxValue();

			if (set.onlyInt) {
				set.setValue((int) number);
			} else
				set.setValue(number);
		}

		if (key == KeyboardUtils.KEY_ESCAPE || key == Client.getModuleByName("ClickGui").getKey()) {
			writing = false;
			textBuilder = new StringBuilder();
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		dragging = false;
	}

	@Override
	public boolean isMouseOnButton(int x, int y) {
		return isMouseOnButtonD(x, y) || isMouseOnButtonI(x, y);
	}

	private boolean isMouseOnButtonD(int x, int y) {
		return x > this.x && x < this.x + (parent.parent.getWidth() / 2 + 1) && y > this.y && y < this.y + 15;
	}

	private boolean isMouseOnButtonI(int x, int y) {
		return x > this.x + parent.parent.getWidth() / 2 && x < this.x + parent.parent.getWidth() && y > this.y && y < this.y + 15;
	}

	public double getNumber() {
		if (!textBuilder.isEmpty()) {
			if (textBuilder.charAt(textBuilder.length() - 1) == '.') {
				textBuilder.append("0");
			}
		}
		if (textBuilder.isEmpty()) {
			return 0;
		}

		return Double.parseDouble(textBuilder.toString());
	}
}
