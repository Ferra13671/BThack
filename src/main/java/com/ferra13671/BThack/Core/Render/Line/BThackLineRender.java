package com.ferra13671.BThack.Core.Render.Line;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.RegionPos;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.List;

public final class BThackLineRender implements Mc {

    public void prepareLineRenderer() {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        BThackRender.worldRenderContext.matrixStack().push();

        RegionPos region = BThackRender.getCameraRegion();
        BThackRender.applyRegionalRenderOffset(BThackRender.worldRenderContext.matrixStack(), region);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    public void renderLines(List<RenderLine> lines) {
        Matrix4f matrix = BThackRender.worldRenderContext.matrixStack().peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Vec3d regionVec = BThackRender.getCameraRegion().toVec3d();

        for (RenderLine line : lines) {
            BThackRender.trace(line.vec3d, matrix, mc.getTickDelta(), line.red, line.green, line.blue, line.alpha, tessellator, bufferBuilder, regionVec);
        }
    }

    public void stopLineRenderer() {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderSystem.setShaderColor(1,1,1,1);

        BThackRender.worldRenderContext.matrixStack().pop();
    }
}
