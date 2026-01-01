package com.ferra13671.bthack.screen.api;

import com.ferra13671.MegaEvents.eventbus.IEventBus;
import com.ferra13671.bthack.utils.MouseUtils;
import com.ferra13671.discordipc.OnChangeHolder;
import lombok.Getter;

public abstract class ScreenObjectImpl extends AbstractScreenObject {
    @Getter protected float x;
    @Getter protected float y;
    @Getter protected float width;
    @Getter protected float height;
    @Getter protected OnChangeHolder<Boolean> hoveredState = new OnChangeHolder<>(false, state -> {
        if (state)
            mouseEnter();
        else
            mouseLeave();
    });

    public ScreenObjectImpl(IEventBus instanceEventBus, float x, float y, float width, float height) {
        super(instanceEventBus);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void mouseEnter() {}

    protected void mouseLeave() {}

    @Override
    public void update() {
        this.hoveredState.setValue(isMouseOnObject(MouseUtils.getMouseX(), MouseUtils.getMouseY()));
    }

    @Override
    public boolean isMouseOnObject(int mouseX, int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width &&
                mouseY >= this.y && mouseY <= this.y + this.height;
    }
}
