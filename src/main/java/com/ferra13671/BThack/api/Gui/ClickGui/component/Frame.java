package com.ferra13671.BThack.api.Gui.ClickGui.component;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Effect.Color.ColorEffects.SmoothEmergenceEffect;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.Button;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;
import java.util.ArrayList;

public class Frame implements Mc {

	public ArrayList<Button> buttons;
	public Module.Category category;
	private boolean open;
	private final int width;
	private int y;
	private int x;
	private final int barHeight;
	private boolean isDragging;
	public int dragX;
	public int dragY;
	public static int color;
	
	public Frame(Module.Category cat) {
		buttons = new ArrayList<>();
		category = cat;
		width = 100;
		x = 0;
		y = 60;
		dragX = 0;
		barHeight = 12;
		open = true;
		isDragging = false;
		int tY = barHeight;

		for(Module mod : Client.getModulesInCategory(category)) {
			Button button = new Button(mod, this, tY);
			button.effect = new SmoothEmergenceEffect(new Color(0,0,0,255), buttons.size() * 2, 10);
			buttons.add(button);
			tY += 12;
		}
	}
	
	public ArrayList<Button> getButtons() {
		return buttons;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public void setDrag(boolean drag) {
		isDragging = drag;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}

	public Module.Category getCategory() {
		return this.category;
	}
	
	public void renderFrame() {
		if (ClickGui.rainbow.getValue()) {
			int type = (int) ClickGui.rainbowSpeed.getValue();
			BThackRender.drawHorizontalRainbowRect(x, y - 1, x + width, y, type);
			BThackRender.drawHorizontalRainbowRect(x, y, x + width, y + 12, type);
		} else {
			BThackRender.drawRect(x, y - 1, x + width, y, ClickGui.customColor.getValue() ? new Color((int) ClickGui.redColor.getValue(), (int) ClickGui.greenColor.getValue(), (int) ClickGui.blueColor.getValue()).getRGB() : new Color(Client.colourTheme.getFontColour()).hashCode());
			BThackRender.drawRect(x, y, x + width, y + 12, ClickGui.customColor.getValue() ? new Color((int) ClickGui.redColor.getValue(), (int) ClickGui.greenColor.getValue(), (int) ClickGui.blueColor.getValue()).getRGB() : new Color(Client.colourTheme.getFontColour()).hashCode());
		}
		BThackRender.drawString(category.name(), x + (width / 2) - (mc.textRenderer.getWidth(category.name()) / 2), y + (barHeight / 2) - (mc.textRenderer.fontHeight / 2), Client.colourTheme.getModuleDisabledColour());

		if(open) {
			if(!buttons.isEmpty()) {
				for(Component component : buttons) {
					component.renderComponent();
				}
			}
		}
	}
	
	public void refresh() {
		int off = this.barHeight;
		for(Component comp : buttons) {
			comp.setOff(off);
			off += comp.getHeight();
		}
	}

	public void updateDependencies() {
		int off = this.barHeight;
		for(Component comp : buttons) {
			comp.updateDependencies(off);
			off += comp.getHeight();
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void updatePosition(int mouseX, int mouseY) {
		if(isDragging) {
			setX(mouseX - dragX);
			setY(mouseY - dragY);
		}
	}

	public void tick() {
		for (Component component : buttons) {
			component.tick();
		}
	}

	public void moveFrame(int keyCode) {
		switch (keyCode) {
			case KeyboardUtils.KEY_LEFT:
				x -= 10;
				break;
			case KeyboardUtils.KEY_RIGHT:
				x += 10;
				break;
			case KeyboardUtils.KEY_UP:
				y -= 10;
				break;
			case KeyboardUtils.KEY_DOWN:
				y += 10;
		}
	}
	
	public boolean isWithinHeader(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + barHeight;
    }

	public void refreshButtonEffects() {
		int size = 0;
		for (Button button : buttons) {
			button.effect = new SmoothEmergenceEffect(new Color(0,0,0,255), (int) (size * ClickGui.effectDelayS.getValue()), (int) ClickGui.effectDelay.getValue());
			size++;
		}
	}
	
}
