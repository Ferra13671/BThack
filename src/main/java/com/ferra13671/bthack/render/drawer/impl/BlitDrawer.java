package com.ferra13671.bthack.render.drawer.impl;

import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.drawer.SimpleDrawer;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometVertexFormats;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;

public class BlitDrawer extends SimpleDrawer {
    private final Framebuffer input;

    public BlitDrawer(int textureWidth, int textureHeight, Framebuffer input) {
        super(null, Mesh.builder(DrawMode.QUADS, CometVertexFormats.POSITION));
        this.input = input;

        rect(textureWidth, textureHeight);
        end();
    }

    private void rect(int width, int height) {
        this.meshBuilder
                .vertex(0, 0, 0)
                .vertex(0, height, 0)
                .vertex(width, height, 0)
                .vertex(width, 0, 0);
    }

    @Override
    protected void draw() {
        CometRenderer.setGlobalProgram(BThackRenderSystem.PROGRAMS.BLIT);
        MinecraftPlugin.initMatrix();
        CometRenderer.getGlobalProgram().getSampler(0).set(this.input.getColorTextureId());

        CometRenderer.draw(this.mesh, false);
    }
}
