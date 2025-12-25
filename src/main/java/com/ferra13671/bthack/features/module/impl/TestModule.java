package com.ferra13671.bthack.features.module.impl;

import com.ferra13671.MegaEvents.eventbus.EventSubscriber;
import com.ferra13671.bthack.events.Render2DEvent;
import com.ferra13671.bthack.features.module.BThackModule;
import com.ferra13671.bthack.features.module.ModuleInfo;
import com.ferra13671.bthack.render.RenderColor;
import com.ferra13671.bthack.render.drawers.BasicRectDrawer;
import com.ferra13671.bthack.render.drawers.Drawer;

@ModuleInfo(name = "Test Module", category = "misc")
public class TestModule extends BThackModule {

    private final Drawer rectDrawer = new BasicRectDrawer(RenderColor.of(255, 0, 0, 255))
            .rectSized(250, 250, 150, 50)
            .rectSized(300, 150, 50, 100)
            .end();


    @EventSubscriber(event = Render2DEvent.class)
    public void onRender2D() {
        rectDrawer.tryDraw();
    }
}
