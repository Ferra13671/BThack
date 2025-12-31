package com.ferra13671.bthack.screen.api;

import com.ferra13671.MegaEvents.eventbus.IEventBus;

public abstract class ScreenObject {
    protected final IEventBus instanceEventBus;

    public ScreenObject(IEventBus instanceEventBus) {
        this.instanceEventBus = instanceEventBus;
        this.instanceEventBus.register(this);
    }

    public abstract void render(int mouseX, int mouseY);

    public void mouseClicked(MouseClick click) {}

    public void mouseReleased(MouseClick click) {}

    public void mouseScrolled(MouseScroll scroll) {}

    public void keyTyped(int keyCode) {}

    public void charTyped(int _char) {}

    public boolean isMouseOnObject(int mouseX, int mouseY) {
        return false;
    }
}
