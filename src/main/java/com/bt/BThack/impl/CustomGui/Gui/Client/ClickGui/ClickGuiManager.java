package com.bt.BThack.impl.CustomGui.Gui.Client.ClickGui;

import com.bt.BThack.BThack;
import com.bt.BThack.System.ConfigInit.ConfigInit;
import com.bt.BThack.impl.CustomGui.Gui.Client.GuiUtils.component.Component;
import com.bt.BThack.impl.CustomGui.Gui.Client.GuiUtils.component.Frame;
import com.bt.BThack.api.Module.Module;
import com.bt.BThack.impl.CustomGui.Gui.Client.GuiUtils.component.components.Button;
import com.bt.BThack.impl.Module.PLAYER.ActionBot.Config.Gui.ActionBotConfigGui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;


public class ClickGuiManager extends GuiScreen {
	public static ArrayList<Frame> frames;

	public static int color;

	public static int descriptionY;

	public ClickGuiManager() {
		frames = new ArrayList<>();
		int frameX = 0;
		int frameY = 0;
		for(Module.Category category : Module.Category.values()) {
			Frame frame = new Frame(category);
			frame.setY(frameY);
			frame.setX(frameX);
			frames.add(frame);
			frameX += 100;
		}
	}
	
	@Override
	public void initGui() {
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for(Frame frame : frames) {
			frame.renderFrame();
			frame.updatePosition(mouseX, mouseY);
			for(Component comp : frame.getButtons()) {
				comp.updateComponent(mouseX, mouseY);

			}
			
		}
		descriptionY = this.height - (this.height / 40);
	}

	private boolean guiButtonsInited = false;

	public void tick() {
		for (Frame frame : frames) {
			frame.tick();
		}
	}

	@Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
		if (!guiButtonsInited) {
			mc.gameSettings.guiScale = 2;

			BThack.instance.settingsManager.getModuleSettingByName("ActionBot", "Open Config").setScreenVal(new ActionBotConfigGui());

			guiButtonsInited = true;
		}

		for(Frame frame : frames) {
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDrag(true);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
				frame.setOpen(!frame.isOpen());
			}
			if(frame.isOpen()) {
				if(!frame.getButtons().isEmpty()) {
					for(Component component : frame.getButtons()) {
						component.mouseClicked(mouseX, mouseY, mouseButton);
					}
				}
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		for(Frame frame : frames) {
			if(frame.isOpen() && keyCode != 1) {
				if(!frame.getButtons().isEmpty()) {
					for(Component component : frame.getButtons()) {
						component.keyTyped(typedChar, keyCode);
					}
				}
			}
		}

		switch (keyCode) {
			case 1:
				ConfigInit.saveConfig();
				this.mc.displayGuiScreen(null);
				break;
			case Keyboard.KEY_LEFT:
			case Keyboard.KEY_RIGHT:
			case Keyboard.KEY_UP:
			case Keyboard.KEY_DOWN:
				for(Frame frame : frames) {
					frame.moveFrame(keyCode);
				}
		}
	}
	
	@Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
		for(Frame frame : frames) {
			frame.setDrag(false);
		}
		for(Frame frame : frames) {
			if(frame.isOpen()) {
				if(!frame.getButtons().isEmpty()) {
					for(Component component : frame.getButtons()) {
						component.mouseReleased(mouseX, mouseY, state);
					}
				}
			}
		}
	}

	@Override
	public void onGuiClosed() {
		for (Frame frame : frames) {
			frame.refreshButtonEffects();
			for (Button component : frame.buttons) {
				component.open = false;
				component.parent.refresh();
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
