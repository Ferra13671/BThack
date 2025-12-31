package com.ferra13671.bthack.render.drawer;

import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.render.BThackRenderSystem;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class StaticDrawer<T extends Drawer> implements IStaticDrawer {
    private final AtomicBoolean buildState = new AtomicBoolean(false);
    private final Supplier<T> prepareDrawerSupplier;
    private T drawer;
    private T prevDrawer;
    
    public StaticDrawer(Supplier<T> prepareDrawerSupplier) {
        this.prepareDrawerSupplier = prepareDrawerSupplier;
        rebuild();
    }

    @Override
    public void rebuild() {
        BThackClient.getInstance().getExecutor().execute(() -> {
            this.buildState.set(false);
            this.prevDrawer = this.drawer;
            this.drawer = null;

            this.drawer = this.prepareDrawerSupplier.get();

            AtomicBoolean finishIndicator = new AtomicBoolean(false);
            BThackRenderSystem.registerRenderCall(() -> {
                this.drawer.end();
                this.drawer.makeStandalone();
                if (this.prevDrawer != null)
                    this.prevDrawer.close();
                this.prevDrawer = null;
                this.buildState.set(true);
                finishIndicator.set(true);
            });

            while (!finishIndicator.get())
                Thread.yield();
        });
    }

    @Override
    public void draw() {
        if (this.buildState.get())
            this.drawer.tryDraw();
        else if (this.prevDrawer != null)
            this.prevDrawer.tryDraw();
    }

    @Override
    public void close() {
        if (this.drawer != null) {
            this.drawer.close();
            this.buildState.set(false);
        }
    }
}
