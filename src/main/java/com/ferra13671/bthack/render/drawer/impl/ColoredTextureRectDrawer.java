package com.ferra13671.bthack.render.drawer.impl;

import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.RectColors;
import com.ferra13671.bthack.render.drawer.SimpleDrawer;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometVertexFormats;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.gltextureutils.GlTex;
import lombok.Getter;

public class ColoredTextureRectDrawer extends SimpleDrawer {
    @Getter
    private GlTex texture;

    public ColoredTextureRectDrawer() {
        this(null);
    }

    public ColoredTextureRectDrawer(Runnable preDrawRunnable) {
        super(preDrawRunnable, Mesh.builder(DrawMode.QUADS, CometVertexFormats.POSITION_TEXTURE_COLOR));
    }

    public ColoredTextureRectDrawer rectSized(float x, float y, float width, float height, RectColors rectColors) {
        return rectPositioned(x, y, x + width, y + height, 0, 0, 1, 1, rectColors);
    }

    public ColoredTextureRectDrawer rectSized(float x, float y, float width, float height, float u1, float v1, float u2, float v2, RectColors rectColors) {
        return rectPositioned(x, y, x + width, y + height, u1, v1, u2, v2, rectColors);
    }

    public ColoredTextureRectDrawer rectPositioned(float x1, float y1, float x2, float y2, RectColors rectColors) {
        return rectPositioned(x1, y1, x2, y2, 0, 0, 1, 1, rectColors);
    }

    public ColoredTextureRectDrawer rectPositioned(float x1, float y1, float x2, float y2, float u1, float v1, float u2, float v2, RectColors rectColors) {
        this.meshBuilder.vertex(x1, y1, 0)
                .element("Texture", VertexElementType.FLOAT, u1, v1)
                .element("Color", VertexElementType.FLOAT, rectColors.x1y1Color()[0], rectColors.x1y1Color()[1], rectColors.x1y1Color()[2], rectColors.x1y1Color()[3]);
        this.meshBuilder.vertex(x1, y2, 0)
                .element("Texture", VertexElementType.FLOAT, u1, v2)
                .element("Color", VertexElementType.FLOAT, rectColors.x1y2Color()[0], rectColors.x1y2Color()[1], rectColors.x1y2Color()[2], rectColors.x1y2Color()[3]);
        this.meshBuilder.vertex(x2, y2, 0)
                .element("Texture", VertexElementType.FLOAT, u2, v2)
                .element("Color", VertexElementType.FLOAT, rectColors.x2y2Color()[0], rectColors.x2y2Color()[1], rectColors.x2y2Color()[2], rectColors.x2y2Color()[3]);
        this.meshBuilder.vertex(x2, y1, 0)
                .element("Texture", VertexElementType.FLOAT, u2, v1)
                .element("Color", VertexElementType.FLOAT, rectColors.x2y1Color()[0], rectColors.x2y1Color()[1], rectColors.x2y1Color()[2], rectColors.x2y1Color()[3]);

        return this;
    }

    public ColoredTextureRectDrawer setTexture(GlTex texture) {
        this.texture = texture;
        return this;
    }

    @Override
    protected void draw() {
        CometRenderer.setGlobalProgram(BThackRenderSystem.PROGRAMS.TEXTURE_COLOR);

        CometRenderer.initShaderColor();
        MinecraftPlugin.initMatrix();

        if (this.texture != null)
            CometRenderer.getGlobalProgram().getSampler(0).set(this.texture);

        CometRenderer.draw(this.mesh, false);
    }
}
