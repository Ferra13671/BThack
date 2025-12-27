package com.ferra13671.bthack.render.drawers;

import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometVertexFormats;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.gltextureutils.GlTex;
import lombok.Getter;

public class BasicTextureRectDrawer extends Drawer {
    @Getter
    private GlTex texture;

    public BasicTextureRectDrawer() {
        this(null);
    }

    public BasicTextureRectDrawer(Runnable preDrawRunnable) {
        super(preDrawRunnable, Mesh.builder(DrawMode.QUADS, CometVertexFormats.POSITION_TEXTURE));
    }

    public BasicTextureRectDrawer rectSized(float x, float y, float width, float height) {
        return rectPositioned(x, y, x + width, y + height, 0, 0, 1, 1);
    }

    public BasicTextureRectDrawer rectSized(float x, float y, float width, float height, float u1, float v1, float u2, float v2) {
        return rectPositioned(x, y, x + width, y + height, u1, v1, u2, v2);
    }

    public BasicTextureRectDrawer rectPositioned(float x1, float y1, float x2, float y2) {
        return rectPositioned(x1, y1, x2, y2, 0, 0, 1, 1);
    }

    public BasicTextureRectDrawer rectPositioned(float x1, float y1, float x2, float y2, float u1, float v1, float u2, float v2) {
        this.meshBuilder.vertex(x1, y1, 0).element("Texture", VertexElementType.FLOAT, u1, v1);
        this.meshBuilder.vertex(x1, y2, 0).element("Texture", VertexElementType.FLOAT, u1, v2);
        this.meshBuilder.vertex(x2, y2, 0).element("Texture", VertexElementType.FLOAT, u2, v2);
        this.meshBuilder.vertex(x2, y1, 0).element("Texture", VertexElementType.FLOAT, u2, v1);

        return this;
    }

    public BasicTextureRectDrawer setTexture(GlTex texture) {
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
