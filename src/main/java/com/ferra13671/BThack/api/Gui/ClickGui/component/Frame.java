package com.ferra13671.BThack.api.Gui.ClickGui.component;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Category.Category;
import com.ferra13671.BThack.api.Effect.Color.ColorEffects.SmoothEmergenceEffect;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Frame implements Mc {

	public final ArrayList<ModuleButton> buttons;
	public final String frameName;
	private boolean open;
	private final int width;
	private int y;
	private int x;
	private final int barHeight = 12;
	private boolean isDragging;
	public int dragX;
	public int dragY;

	public Frame(String name, List<Module> modules) {
		buttons = new ArrayList<>();
		frameName = name;
		width = 100;
		x = 0;
		y = 60;
		dragX = 0;
		open = true;
		isDragging = false;
		int tY = barHeight;

		for(Module mod : modules) {
			ModuleButton button = new ModuleButton(mod, this, tY);
			button.effect = new SmoothEmergenceEffect(new Color(0,0,0,255), buttons.size() * 2, 10);
			buttons.add(button);
			tY += 12;
		}
	}
	
	public Frame(Category cat) {
		this(cat.name(), Client.getModulesInCategory(cat));
	}
	
	public ArrayList<ModuleButton> getButtons() {
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

	public String getFrameName() {
		return this.frameName;
	}
	
	public void renderFrame() {
		BThackRender.guiGraphics.getMatrices().translate(0,0, 1000);

		if(open) {
			if(!buttons.isEmpty()) {
				for(Component component : buttons) {
					component.renderComponent();
				}
			}
		}
		if (ClickGui.rainbow.getValue()) {
			int type = (int) ClickGui.rainbowSpeed.getValue();
			BThackRender.drawHorizontalRainbowRect(x, y, x + width, y + 12, type);
		} else {
			BThackRender.drawRect(x, y, x + width, y + 12, ClickGui.customColor.getValue() ? new Color((int) ClickGui.redColor.getValue(), (int) ClickGui.greenColor.getValue(), (int) ClickGui.blueColor.getValue()).getRGB() : new Color(Client.clientInfo.getColorTheme().getFontColour()).hashCode());
		}
		if (ClickGui.frameOutline.getValue())
			BThackRender.drawOutlineRect(x, y, x + width, y + 12, 1, new Color(0, 0, 0, 100).hashCode());
		BThackRender.drawString(frameName, x + (width / 2f) - (mc.textRenderer.getWidth(frameName) / 2f), y + (barHeight / 2f) - (mc.textRenderer.fontHeight / 2f), Client.clientInfo.getColorTheme().getModuleDisabledColour());
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
				x -= 5;
				break;
			case KeyboardUtils.KEY_RIGHT:
				x += 5;
				break;
			case KeyboardUtils.KEY_UP:
				y -= 5;
				break;
			case KeyboardUtils.KEY_DOWN:
				y += 5;
		}
	}

	public void moveFrame(double deltaX, double deltaY) {
		x += (int) (deltaX * 7);
		y += (int) (deltaY * 7);
	}
	
	public boolean isWithinHeader(int mouseX, int mouseY) {
        return mouseX >= ClickGui.applyGuiScale(x) && mouseX <= ClickGui.applyGuiScale(x + width) &&
				mouseY >= ClickGui.applyGuiScale(y) && mouseY <= ClickGui.applyGuiScale(y + barHeight);
    }

	public void refreshButtonEffects() {
		int size = 0;
		for (ModuleButton button : buttons) {
			button.effect = new SmoothEmergenceEffect(new Color(0,0,0,255), (int) (size * ClickGui.effectDelayS.getValue()), (int) ClickGui.effectDelay.getValue());
			size++;
		}
	}
	
}
