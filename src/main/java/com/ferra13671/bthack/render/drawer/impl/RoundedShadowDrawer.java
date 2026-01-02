package com.ferra13671.bthack.render.drawer.impl;

import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.RectColors;
import com.ferra13671.bthack.render.drawer.SimpleDrawer;
import com.ferra13671.bthack.render.vertex.BThackVertexElementTypes;
import com.ferra13671.bthack.render.vertex.BThackVertexFormats;
import com.ferra13671.bthack.utils.Mc;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;

public class RoundedShadowDrawer extends SimpleDrawer implements Mc {

    public RoundedShadowDrawer() {
        this(null);
    }

    public RoundedShadowDrawer(Runnable preDrawRunnable) {
        super(preDrawRunnable, Mesh.builder(DrawMode.QUADS, BThackVertexFormats.ROUNDED));
    }

    public RoundedShadowDrawer rectSized(float x, float y, float width, float height, float radius, RectColors rectColors) {
        return rectPositioned(x, y, x + width, y + height, radius, rectColors);
    }

    public RoundedShadowDrawer rectPositioned(float x1, float y1, float x2, float y2, float radius, RectColors rectColors) {
        float[] halfSize = {(x2 - x1) / 2, (y2 - y1) / 2};
        float[] pos = {x1 + halfSize[0], y1 + halfSize[1]};

        x1 -= 20;
        x2 += 20;
        y1 -= 20;
        y2 += 20;

        this.meshBuilder.vertex(x1, y1, 0)
                .element("Color", BThackVertexElementTypes.RENDER_COLOR, rectColors.x1y1Color())
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        this.meshBuilder.vertex(x1, y2, 0)
                .element("Color", BThackVertexElementTypes.RENDER_COLOR, rectColors.x1y2Color())
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        this.meshBuilder.vertex(x2, y2, 0)
                .element("Color", BThackVertexElementTypes.RENDER_COLOR, rectColors.x2y2Color())
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        this.meshBuilder.vertex(x2, y1, 0)
                .element("Color", BThackVertexElementTypes.RENDER_COLOR, rectColors.x2y1Color())
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);

        return this;
    }

    @Override
    protected void draw() {
        CometRenderer.setGlobalProgram(BThackRenderSystem.PROGRAMS.ROUNDED_SHADOW);

        CometRenderer.initShaderColor();
        MinecraftPlugin.initMatrix();

        CometRenderer.getGlobalProgram().getUniform("height", UniformType.FLOAT).set((float) mc.getWindow().getHeight());

        CometRenderer.draw(this.mesh, false);
    }
}
