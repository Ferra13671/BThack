package com.ferra13671.bthack.render.drawer.impl;

import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.drawer.SimpleDrawer;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometVertexFormats;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;

public class BasicRectDrawer extends SimpleDrawer {

    public BasicRectDrawer() {
        this(null);
    }

    public BasicRectDrawer(Runnable preDrawRunnable) {
        super(preDrawRunnable, Mesh.builder(DrawMode.QUADS, CometVertexFormats.POSITION));
    }

    public BasicRectDrawer rectSized(float x, float y, float width, float height) {
        return rectPositioned(x, y, x + width, y + height);
    }

    public BasicRectDrawer rectPositioned(float x1, float y1, float x2, float y2) {
        this.meshBuilder.vertex(x1, y1, 0);
        this.meshBuilder.vertex(x1, y2, 0);
        this.meshBuilder.vertex(x2, y2, 0);
        this.meshBuilder.vertex(x2, y1, 0);

        return this;
    }

    @Override
    protected void draw() {
        CometRenderer.setGlobalProgram(BThackRenderSystem.PROGRAMS.POSITION);

        CometRenderer.initShaderColor();
        MinecraftPlugin.initMatrix();

        CometRenderer.draw(this.mesh, false);
    }
}
