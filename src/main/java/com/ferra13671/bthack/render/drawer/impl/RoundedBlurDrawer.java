package com.ferra13671.bthack.render.drawer.impl;

import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.drawer.SimpleDrawer;
import com.ferra13671.bthack.render.vertex.BThackVertexFormats;
import com.ferra13671.bthack.utils.Mc;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;

public class RoundedBlurDrawer extends SimpleDrawer implements Mc {

    public RoundedBlurDrawer() {
        this(null);
    }

    public RoundedBlurDrawer(Runnable preDrawRunnable) {
        super(preDrawRunnable, Mesh.builder(DrawMode.QUADS, BThackVertexFormats.ROUNDED_BLUR));
    }

    public RoundedBlurDrawer rectSized(float x, float y, float width, float height, float radius) {
        return rectPositioned(x, y, x + width, y + height, radius);
    }

    public RoundedBlurDrawer rectPositioned(float x1, float y1, float x2, float y2, float radius) {
        float[] halfSize = {(x2 - x1) / 2, (y2 - y1) / 2};
        float[] pos = {x1 + halfSize[0], y1 + halfSize[1]};

        x1 -= 2;
        x2 += 2;
        y1 -= 2;
        y2 += 2;

        this.meshBuilder.vertex(x1, y1, 0)
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        this.meshBuilder.vertex(x1, y2, 0)
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        this.meshBuilder.vertex(x2, y2, 0)
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);
        this.meshBuilder.vertex(x2, y1, 0)
                .element("Rect Position", VertexElementType.FLOAT, pos[0], pos[1])
                .element("Half Size", VertexElementType.FLOAT, halfSize[0], halfSize[1])
                .element("Radius", VertexElementType.FLOAT, radius);

        return this;
    }

    @Override
    protected void draw() {
        CometRenderer.setGlobalProgram(BThackRenderSystem.PROGRAMS.ROUNDED_BLUR);

        CometRenderer.initShaderColor();
        MinecraftPlugin.initMatrix();

        CometRenderer.getGlobalProgram().getUniform("height", UniformType.FLOAT).set((float) mc.getWindow().getHeight());
        CometRenderer.getGlobalProgram().getSampler(0).set(BThackRenderSystem.BLUR_PROVIDER.getBlurFrameBuffer().getColorTexture());

        CometRenderer.draw(this.mesh, false);
    }
}
