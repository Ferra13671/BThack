package com.ferra13671.bthack.render.drawer;

import java.io.Closeable;

public interface IStaticDrawer extends Closeable {

    void rebuild();

    void draw();

    @Override
    void close();
}
