package com.ferra13671.bthack.screen;

import com.ferra13671.MegaEvents.eventbus.IEventBus;
import com.ferra13671.MegaEvents.eventbus.impl.EventBus;
import com.ferra13671.bthack.events.screen.CloseScreenEvent;
import com.ferra13671.bthack.events.screen.DisplayScreenEvent;
import com.ferra13671.bthack.events.screen.RepositionElementsEvent;
import com.ferra13671.bthack.events.screen.ResizeScreenEvent;
import com.ferra13671.bthack.utils.Mc;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class BThackScreen extends Screen implements Mc {
    protected final IEventBus eventBus = new EventBus();

    protected BThackScreen() {
        super(Component.empty());
        this.eventBus.register(this);
    }

    @Override
    public final void added() {
        this.eventBus.activate(new DisplayScreenEvent());
        this.eventBus.activate(new RepositionElementsEvent());
    }

    @Override
    public final void removed() {
        this.eventBus.activate(new CloseScreenEvent());
    }

    @Override
    public final void resize(int width, int height) {
        super.resize(width, height);
        this.eventBus.activate(new ResizeScreenEvent(width, height));
        this.eventBus.activate(new RepositionElementsEvent());
    }

    public abstract void render(int mouesX, int mouseY);
}
