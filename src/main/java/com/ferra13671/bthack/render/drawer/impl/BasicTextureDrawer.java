package com.ferra13671.bthack.render.drawer.impl;

import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.TextureBounds;
import com.ferra13671.bthack.render.drawer.SimpleDrawer;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometVertexFormats;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.gltextureutils.GlTex;
import lombok.Getter;

public class BasicTextureDrawer extends SimpleDrawer {
    @Getter
    private GlTex texture;

    public BasicTextureDrawer() {
        this(null);
    }

    public BasicTextureDrawer(Runnable preDrawRunnable) {
        super(preDrawRunnable, Mesh.builder(DrawMode.QUADS, CometVertexFormats.POSITION_TEXTURE));
    }

    public BasicTextureDrawer rectSized(float x, float y, float width, float height, TextureBounds textureBounds) {
        return rectPositioned(x, y, x + width, y + height, textureBounds);
    }

    public BasicTextureDrawer rectPositioned(float x1, float y1, float x2, float y2, TextureBounds textureBounds) {
        this.meshBuilder.vertex(x1, y1, 0).element("Texture", VertexElementType.FLOAT, textureBounds.u1(), textureBounds.v1());
        this.meshBuilder.vertex(x1, y2, 0).element("Texture", VertexElementType.FLOAT, textureBounds.u1(), textureBounds.v2());
        this.meshBuilder.vertex(x2, y2, 0).element("Texture", VertexElementType.FLOAT, textureBounds.u2(), textureBounds.v2());
        this.meshBuilder.vertex(x2, y1, 0).element("Texture", VertexElementType.FLOAT, textureBounds.u2(), textureBounds.v1());

        return this;
    }

    public BasicTextureDrawer setTexture(GlTex texture) {
        this.texture = texture;
        return this;
    }

    @Override
    protected void draw() {
        CometRenderer.setGlobalProgram(BThackRenderSystem.PROGRAMS.TEXTURE);

        CometRenderer.initShaderColor();
        MinecraftPlugin.initMatrix();

        if (this.texture != null)
            CometRenderer.getGlobalProgram().getSampler(0).set(this.texture);

        CometRenderer.draw(this.mesh, false);
    }
}
