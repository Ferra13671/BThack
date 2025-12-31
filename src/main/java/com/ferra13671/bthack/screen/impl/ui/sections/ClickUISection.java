package com.ferra13671.bthack.screen.impl.ui.sections;

import com.ferra13671.MegaEvents.eventbus.IEventBus;
import com.ferra13671.bthack.screen.api.ScreenObject;
import com.ferra13671.bthack.screen.impl.ui.ClickUI;
import lombok.Getter;

public abstract class ClickUISection extends ScreenObject {
    @Getter
    protected ClickUI clickUI;
    @Getter
    protected int x;
    @Getter
    protected int y;
    @Getter
    protected final int width;
    @Getter
    protected final int height;

    public ClickUISection(ClickUI clickUI, IEventBus instanceEventBus, int width, int height) {
        super(instanceEventBus);

        this.clickUI = clickUI;
        this.width = width;
        this.height = height;
    }
}
