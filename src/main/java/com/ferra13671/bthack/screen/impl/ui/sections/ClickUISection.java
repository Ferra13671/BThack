package com.ferra13671.bthack.screen.impl.ui.sections;

import com.ferra13671.MegaEvents.eventbus.IEventBus;
import com.ferra13671.bthack.screen.api.ScreenObjectImpl;
import com.ferra13671.bthack.screen.impl.ui.ClickUI;
import com.ferra13671.bthack.screen.impl.ui.Rebuildable;
import lombok.Getter;

public abstract class ClickUISection extends ScreenObjectImpl implements Rebuildable {
    @Getter
    protected ClickUI clickUI;

    public ClickUISection(ClickUI clickUI, IEventBus instanceEventBus, int width, int height) {
        super(instanceEventBus, 0, 0, 0, 0);

        this.clickUI = clickUI;
        this.width = width;
        this.height = height;
    }
}
