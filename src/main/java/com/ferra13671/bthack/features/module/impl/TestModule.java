package com.ferra13671.bthack.features.module.impl;

import com.ferra13671.MegaEvents.eventbus.EventSubscriber;
import com.ferra13671.bthack.events.Render2DEvent;
import com.ferra13671.bthack.features.module.BThackModule;
import com.ferra13671.bthack.features.module.ModuleInfo;
import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.RectColors;
import com.ferra13671.bthack.render.RenderColor;
import com.ferra13671.bthack.render.drawers.ColoredTextureRectDrawer;
import com.ferra13671.bthack.render.drawers.Drawer;

import java.awt.*;

@ModuleInfo(name = "Test Module", category = "misc")
public class TestModule extends BThackModule {

    private final Drawer rectDrawer = new ColoredTextureRectDrawer()
            .setTexture(BThackRenderSystem.TEXTURES.PLAYER_CATEGORY_ICON)
            .rectSized(250, 250, 100, 100, RectColors.horizontalGradient(RenderColor.of(Color.RED).getColor(), RenderColor.of(Color.YELLOW).getColor()))
            .rectSized(250, 350, 100, 100, new RectColors(
                    RenderColor.of(Color.GREEN).getColor(),
                    RenderColor.of(Color.CYAN).getColor(),
                    RenderColor.of(Color.YELLOW).getColor(),
                    RenderColor.of(Color.MAGENTA).getColor()
            ))
            .end();


    @EventSubscriber(event = Render2DEvent.class)
    public void onRender2D() {
        rectDrawer.tryDraw();
    }
}
