package com.ferra13671.BThack.api.Gui.ClickGui.component;

import com.ferra13671.BThack.api.Effect.Color.ColorEffectBase;

public class Component {
	public ColorEffectBase effect;

	public void tick() {
		effect.tick();
	}


	public void renderComponent() {
	}
	
	public boolean updateComponent(int mouseX, int mouseY) {
		return true;
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		return false;
	}
	
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	}
	
	public void keyTyped(int key) {
		
	}
	
	public void setOff(int newOff) {
		
	}
	
	public int getHeight() {
		return 0;
	}




	public void updateDependencies(int offset) {}
}
