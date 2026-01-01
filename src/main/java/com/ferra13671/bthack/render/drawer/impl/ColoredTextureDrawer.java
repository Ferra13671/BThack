package com.ferra13671.bthack.render.drawer.impl;

import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.RectColors;
import com.ferra13671.bthack.render.drawer.SimpleDrawer;
import com.ferra13671.bthack.render.vertex.BThackVertexElementTypes;
import com.ferra13671.bthack.render.vertex.BThackVertexFormats;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.gltextureutils.GlTex;
import com.ferra13671.gltextureutils.atlas.TextureBorder;
import lombok.Getter;

public class ColoredTextureDrawer extends SimpleDrawer {
    @Getter
    private GlTex texture;

    public ColoredTextureDrawer() {
        this(null);
    }

    public ColoredTextureDrawer(Runnable preDrawRunnable) {
        super(preDrawRunnable, Mesh.builder(DrawMode.QUADS, BThackVertexFormats.POSITION_TEXTURE_COLOR));
    }

    public ColoredTextureDrawer rectSized(float x, float y, float width, float height, RectColors rectColors, TextureBorder textureBorder) {
        return rectPositioned(x, y, x + width, y + height, rectColors, textureBorder);
    }

    public ColoredTextureDrawer rectPositioned(float x1, float y1, float x2, float y2, RectColors rectColors, TextureBorder textureBorder) {
        this.meshBuilder.vertex(x1, y1, 0)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV1())
                .element("Color", BThackVertexElementTypes.RENDER_COLOR, rectColors.x1y1Color());
        this.meshBuilder.vertex(x1, y2, 0)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU1(), textureBorder.getV2())
                .element("Color", BThackVertexElementTypes.RENDER_COLOR, rectColors.x1y2Color());
        this.meshBuilder.vertex(x2, y2, 0)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV2())
                .element("Color", BThackVertexElementTypes.RENDER_COLOR, rectColors.x2y2Color());
        this.meshBuilder.vertex(x2, y1, 0)
                .element("Texture", VertexElementType.FLOAT, textureBorder.getU2(), textureBorder.getV1())
                .element("Color", BThackVertexElementTypes.RENDER_COLOR, rectColors.x2y1Color());

        return this;
    }

    public ColoredTextureDrawer setTexture(GlTex texture) {
        this.texture = texture;
        return this;
    }

    @Override
    protected void draw() {
        CometRenderer.setGlobalProgram(BThackRenderSystem.PROGRAMS.COLORED_TEXTURE);

        CometRenderer.initShaderColor();
        MinecraftPlugin.initMatrix();

        if (this.texture != null)
            CometRenderer.getGlobalProgram().getSampler(0).set(this.texture);

        CometRenderer.draw(this.mesh, false);
    }
}
