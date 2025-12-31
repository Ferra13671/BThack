package com.ferra13671.bthack.render.drawer;

public class DrawerPool implements IStaticDrawer {
    private final StaticDrawer<?>[] drawers;

    public DrawerPool(StaticDrawer<?>... staticDrawers) {
        this.drawers = staticDrawers;
    }

    @Override
    public void rebuild() {
        for (StaticDrawer<?> drawer : this.drawers)
            drawer.rebuild();
    }

    @Override
    public void draw() {
        for (StaticDrawer<?> drawer : this.drawers)
            drawer.draw();
    }

    @Override
    public void close() {
        for (StaticDrawer<?> drawer : this.drawers)
            drawer.close();
    }
}
