package com.ferra13671.bthack.screen.impl.ui.sections;

import com.ferra13671.MegaEvents.eventbus.EventSubscriber;
import com.ferra13671.MegaEvents.eventbus.IEventBus;
import com.ferra13671.bthack.events.screen.RepositionElementsEvent;
import com.ferra13671.bthack.render.drawer.DrawerPool;
import com.ferra13671.bthack.screen.impl.ui.ClickUI;

public class CategoryBarSection extends ClickUISection {
    //TODO Continue after create texture atlas gen in CometRenderer
    private final DrawerPool drawerPool = new DrawerPool(
    );

    public CategoryBarSection(ClickUI clickUI, IEventBus instanceEventBus) {
        super(clickUI, instanceEventBus, 208, 458);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.drawerPool.draw();
    }

    @EventSubscriber(event = RepositionElementsEvent.class, priority = 9)
    public void onRepositionElements() {
        this.x = this.clickUI.getX() + 1;
        this.y = this.clickUI.getWatermarkSection().getY() + this.clickUI.getWatermarkSection().getHeight();

        this.drawerPool.rebuild();
    }
}
