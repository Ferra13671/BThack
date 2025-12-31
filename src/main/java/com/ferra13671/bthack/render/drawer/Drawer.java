package com.ferra13671.bthack.render.drawer;

import java.io.Closeable;

public interface Drawer extends Closeable {

    boolean isEnded();

    Drawer end();

    Drawer makeStandalone();

    Drawer tryDraw();

    @Override
    void close();
}
