package com.ferra13671.bthack.render.drawers;

import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.RectColors;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometVertexFormats;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;

public class ColoredRectDrawer extends Drawer {

    public ColoredRectDrawer() {
        this(null);
    }

    public ColoredRectDrawer(Runnable preDrawRunnable) {
        super(preDrawRunnable, Mesh.builder(DrawMode.QUADS, CometVertexFormats.POSITION_COLOR));
    }

    public ColoredRectDrawer rectSized(float x, float y, float width, float height, RectColors rectColors) {
        return rectPositioned(x, y, x + width, y + height, rectColors);
    }

    public ColoredRectDrawer rectPositioned(float x1, float y1, float x2, float y2, RectColors rectColors) {
        this.meshBuilder.vertex(x1, y1, 0).element("Color", VertexElementType.FLOAT, rectColors.x1y1Color()[0], rectColors.x1y1Color()[1], rectColors.x1y1Color()[2], rectColors.x1y1Color()[3]);
        this.meshBuilder.vertex(x1, y2, 0).element("Color", VertexElementType.FLOAT, rectColors.x1y2Color()[0], rectColors.x1y2Color()[1], rectColors.x1y2Color()[2], rectColors.x1y2Color()[3]);
        this.meshBuilder.vertex(x2, y2, 0).element("Color", VertexElementType.FLOAT, rectColors.x2y2Color()[0], rectColors.x2y2Color()[1], rectColors.x2y2Color()[2], rectColors.x2y2Color()[3]);
        this.meshBuilder.vertex(x2, y1, 0).element("Color", VertexElementType.FLOAT, rectColors.x2y1Color()[0], rectColors.x2y1Color()[1], rectColors.x2y1Color()[2], rectColors.x2y1Color()[3]);

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
