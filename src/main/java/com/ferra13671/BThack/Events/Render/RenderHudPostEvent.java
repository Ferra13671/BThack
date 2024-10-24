package com.ferra13671.BThack.Events.Render;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.client.gui.DrawContext;

public class RenderHudPostEvent extends Event {
    private final DrawContext drawContext;
    private final float partialTicks;

    public RenderHudPostEvent(DrawContext drawContext, float partialTicks) {
        this.drawContext = drawContext;
        this.partialTicks = partialTicks;
    }

    public DrawContext getDrawContext() {
        return this.drawContext;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}
