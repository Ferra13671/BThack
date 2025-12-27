package com.ferra13671.bthack.render.drawers;

import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.cometrenderer.vertex.mesh.MeshBuilder;

import java.io.Closeable;

public abstract class Drawer implements Closeable {
    protected final MeshBuilder meshBuilder;
    protected final Runnable preDrawRunnable;
    protected Mesh mesh = null;
    private boolean finished = false;
    private boolean closed = false;

    public Drawer(Runnable preDrawRunnable, MeshBuilder meshBuilder) {
        this.preDrawRunnable = preDrawRunnable;
        this.meshBuilder = meshBuilder;
    }

    public Drawer end() {
        if (this.finished)
            throw new IllegalStateException("Drawer already finished");
        assertNotClosed();

        this.mesh = this.meshBuilder.buildNullable();
        this.finished = true;

        return this;
    }

    public Drawer makeStandalone() {
        assertFinished();
        assertNotClosed();

        if (this.mesh != null)
            this.mesh.makeStandalone();

        return this;
    }

    public final Drawer tryDraw() {
        assertFinished();
        assertNotClosed();

        if (this.mesh != null) {
            if (this.preDrawRunnable != null)
                this.preDrawRunnable.run();
            draw();
        }

        return this;
    }

    protected abstract void draw();

    @Override
    public void close() {
        if (!this.closed) {

            if (this.mesh != null)
                this.mesh.close();
            this.closed = true;
        }
    }

    private void assertNotClosed() {
        if (this.closed)
            throw new IllegalStateException("Drawer already closed");
    }

    private void assertFinished() {
        if (!this.finished)
            throw new IllegalStateException("Drawer was not finished yet");
    }
}
