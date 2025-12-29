package com.ferra13671.bthack.render.drawer.impl;

import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.RectColors;
import com.ferra13671.bthack.render.drawer.SimpleDrawer;
import com.ferra13671.bthack.render.vertex.BThackVertexElementTypes;
import com.ferra13671.bthack.render.vertex.BThackVertexFormats;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;

public class ColoredRectDrawer extends SimpleDrawer {

    public ColoredRectDrawer() {
        this(null);
    }

    public ColoredRectDrawer(Runnable preDrawRunnable) {
        super(preDrawRunnable, Mesh.builder(DrawMode.QUADS, BThackVertexFormats.POSITION_COLOR));
    }

    public ColoredRectDrawer rectSized(float x, float y, float width, float height, RectColors rectColors) {
        return rectPositioned(x, y, x + width, y + height, rectColors);
    }

    public ColoredRectDrawer rectPositioned(float x1, float y1, float x2, float y2, RectColors rectColors) {
        this.meshBuilder.vertex(x1, y1, 0).element("Color", BThackVertexElementTypes.RENDER_COLOR, rectColors.x1y1Color());
        this.meshBuilder.vertex(x1, y2, 0).element("Color", BThackVertexElementTypes.RENDER_COLOR, rectColors.x1y2Color());
        this.meshBuilder.vertex(x2, y2, 0).element("Color", BThackVertexElementTypes.RENDER_COLOR, rectColors.x2y2Color());
        this.meshBuilder.vertex(x2, y1, 0).element("Color", BThackVertexElementTypes.RENDER_COLOR, rectColors.x2y1Color());

        return this;
    }

    @Override
    protected void draw() {
        CometRenderer.setGlobalProgram(BThackRenderSystem.PROGRAMS.POSITION_COLOR);

        CometRenderer.initShaderColor();
        MinecraftPlugin.initMatrix();

        CometRenderer.draw(this.mesh, false);
    }
}
