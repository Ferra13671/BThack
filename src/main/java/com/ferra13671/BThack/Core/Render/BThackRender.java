package com.ferra13671.BThack.Core.Render;


import com.ferra13671.BThack.Core.Render.Box.BThackBoxRender;
import com.ferra13671.BThack.Core.Render.Line.BThackLineRender;
import com.ferra13671.BThack.api.Utils.EntityUtils;
import com.ferra13671.BThack.api.Utils.RegionPos;
import com.ferra13671.BThack.api.Utils.RotateUtils;
import com.ferra13671.BThack.api.Utils.Texture.Texture;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;
import org.joml.Matrix4f;
import com.ferra13671.BThack.api.Interfaces.Mc;

import java.awt.*;

public final class BThackRender implements Mc {

    public static final VertexConsumerProvider.Immediate bufferSource = mc.getBufferBuilders().getEntityVertexConsumers();
    public static MatrixStack pose;
    public static final DrawContext guiGraphics = new DrawContext(mc, bufferSource);
    public static final BThackWorldRenderContext worldRenderContext = new BThackWorldRenderContext();
    public static final BThackBoxRender boxRender = new BThackBoxRender();
    public static final BThackLineRender lineRender = new BThackLineRender();

    private static boolean inited = false;

    public static void init() {
        if (!inited) {
            pose = guiGraphics.getMatrices();
            boxRender.init();
        }
        inited = true;
    }

    public static void trace(Entity entity, MatrixStack matrixStack, float partialTicks, RegionPos region, Color color) {
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionProgram);

        Vec3d regionVec = region.toVec3d();
        Vec3d start = RotateUtils.getClientLookVec(partialTicks)
                .add(BThackRender.getCameraPos()).subtract(regionVec);

        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
                VertexFormats.POSITION);
        Vec3d end = EntityUtils.getLerpedBox(entity, partialTicks).getCenter()
                .subtract(new Vec3d(regionVec.x, regionVec.y, regionVec.z));

        bufferBuilder
                .vertex(matrix, (float)start.x, (float)start.y, (float)start.z)
                .next();
        bufferBuilder
                .vertex(matrix, (float)end.x, (float)end.y, (float)end.z)
                .next();
        tessellator.draw();
    }

    //Just a simplified line renderer, so as not to waste CPU time on constant data retrieval.
    public static void trace(Entity entity, Matrix4f matrix, float partialTicks, Color color, Tessellator tessellator, BufferBuilder bufferBuilder, Vec3d regionVec) {
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);

        Vec3d start = RotateUtils.getClientLookVec(partialTicks)
                .add(BThackRender.getCameraPos()).subtract(regionVec);
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
                VertexFormats.POSITION);
        Vec3d end = EntityUtils.getLerpedBox(entity, partialTicks).getCenter()
                .subtract(new Vec3d(regionVec.x, regionVec.y, regionVec.z));
        bufferBuilder
                .vertex(matrix, (float)start.x, (float)start.y, (float)start.z)
                .next();
        bufferBuilder
                .vertex(matrix, (float)end.x, (float)end.y, (float)end.z)
                .next();
        tessellator.draw();
    }

    public static void trace(BlockEntity entity, Matrix4f matrix, float partialTicks, Color color, Tessellator tessellator, BufferBuilder bufferBuilder, Vec3d regionVec) {
        Box box = EntityUtils.getLerpedBox(entity, partialTicks);

        if (box == null) return;

        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);

        Vec3d start = RotateUtils.getClientLookVec(partialTicks)
                .add(BThackRender.getCameraPos()).subtract(regionVec);
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
                VertexFormats.POSITION);

        Vec3d end = box.getCenter()
                .subtract(new Vec3d(regionVec.x, regionVec.y, regionVec.z));
        bufferBuilder
                .vertex(matrix, (float)start.x, (float)start.y, (float)start.z)
                .next();
        bufferBuilder
                .vertex(matrix, (float)end.x, (float)end.y, (float)end.z)
                .next();
        tessellator.draw();
    }

    public static void trace(Vec3d vec3d, Matrix4f matrix, float partialTicks, float red, float green, float blue, float alpha, Tessellator tessellator, BufferBuilder bufferBuilder, Vec3d regionVec) {
        RenderSystem.setShaderColor(red, green, blue, alpha);

        Vec3d start = RotateUtils.getClientLookVec(partialTicks)
                .add(BThackRender.getCameraPos()).subtract(regionVec);
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
                VertexFormats.POSITION);

        Vec3d end = vec3d
                .subtract(new Vec3d(regionVec.x, regionVec.y, regionVec.z));
        bufferBuilder
                .vertex(matrix, (float)start.x, (float)start.y, (float)start.z)
                .next();
        bufferBuilder
                .vertex(matrix, (float)end.x, (float)end.y, (float)end.z)
                .next();
        tessellator.draw();
    }

    /*

    public static void renderBoxes(Entity e, MatrixStack matrixStack, float partialTicks, Color color)
    {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        matrixStack.pushPose();

        RegionPos region = BThackRenderUtils.getCameraRegion();
        BThackRenderUtils.applyRegionalRenderOffset(matrixStack, region);

        matrixStack.pushPose();

        Vec3d lerpedPos = BThackRenderUtils.getLerpedPos(e, partialTicks)
                .subtract(region.toVec3d());
        matrixStack.translate(lerpedPos.x, lerpedPos.y, lerpedPos.z);

        matrixStack.pushPose();
        matrixStack.scale(e.getBbWidth(),
                e.getBbHeight(), e.getBbWidth());

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        RenderSystem.setShaderColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, (float)color.getAlpha() / 255);
        drawOutlinedBox(new AABB(-0.5, 0, -0.5, 0.5, 1, 0.5),
                matrixStack);

        matrixStack.popPose();
        matrixStack.popPose();
        matrixStack.popPose();

        RenderSystem.setShaderColor(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawOutlinedBox(AABB bb, MatrixStack matrixStack)
    {
        Matrix4f matrix = matrixStack.last().pose();
        Tesselator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionShader);

        bufferBuilder.begin(VertexFormat.Mode.DEBUG_LINES,
                VertexFormats.POSITION);
        bufferBuilder
                .vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
                .next();

        bufferBuilder
                .vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
                .next();
        bufferBuilder
                .vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
                .next();
        tessellator.end();
    }

     */

    public static void drawRect(int x1, int y1, int x2, int y2, int color) {

        Matrix4f matrix4f = pose.peek().getPositionMatrix();

        if (x1 < x2) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            int j = y1;
            y1 = y2;
            y2 = j;
        }

        float f = (float) ColorHelper.Argb.getAlpha(color) / 255.0F;
        float g = (float) ColorHelper.Argb.getRed(color) / 255.0F;
        float h = (float) ColorHelper.Argb.getGreen(color) / 255.0F;
        float j = (float) ColorHelper.Argb.getBlue(color) / 255.0F;
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderLayer.getGui());
        //вправо-вниз
        vertexconsumer.vertex(matrix4f, (float)x1, (float)y1, (float)0).color(g, h, j, f).next();
        //врапо-вверх
        vertexconsumer.vertex(matrix4f, (float)x1, (float)y2, (float)0).color(g, h, j, f).next();
        //влево-вверх
        vertexconsumer.vertex(matrix4f, (float)x2, (float)y2, (float)0).color(g, h, j, f).next();
        //влево-вниз
        vertexconsumer.vertex(matrix4f, (float)x2, (float)y1, (float)0).color(g, h, j, f).next();
        guiGraphics.draw();
    }

    public static void drawLine(float x1, float y1, float x2, float y2, float wight, int color) {

        Matrix4f matrix4f = pose.peek().getPositionMatrix();

        if (x1 > x2) {
            float i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 > y2) {
            float j = y1;
            y1 = y2;
            y2 = j;
        }

        wight = wight / 2;

        float f = (float) ColorHelper.Argb.getAlpha(color) / 255.0F;
        float g = (float) ColorHelper.Argb.getRed(color) / 255.0F;
        float h = (float) ColorHelper.Argb.getGreen(color) / 255.0F;
        float j = (float) ColorHelper.Argb.getBlue(color) / 255.0F;
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderLayer.getGui());
        //вправо-вниз
        vertexconsumer.vertex(matrix4f, x2 + wight, y2 + wight, (float)0).color(g, h, j, f).next();
        //врапо-вверх
        vertexconsumer.vertex(matrix4f, x2 + wight, y2 - wight, (float)0).color(g, h, j, f).next();
        //влево-вверх
        vertexconsumer.vertex(matrix4f, x1 - wight, y1 - wight, (float)0).color(g, h, j, f).next();
        //влево-вниз
        vertexconsumer.vertex(matrix4f, x1 - wight, y1 + wight, (float)0).color(g, h, j, f).next();
        guiGraphics.draw();
    }

    public static void drawVerticalGradientRect(int x1, int y1, int x2, int y2, int startColor, int endColor) {
        float f = (float) ColorHelper.Argb.getAlpha(startColor) / 255.0F;
        float f1 = (float) ColorHelper.Argb.getRed(startColor) / 255.0F;
        float f2 = (float)ColorHelper.Argb.getGreen(startColor) / 255.0F;
        float f3 = (float) ColorHelper.Argb.getBlue(startColor) / 255.0F;
        float f4 = (float) ColorHelper.Argb.getAlpha(endColor) / 255.0F;
        float f5 = (float) ColorHelper.Argb.getRed(endColor) / 255.0F;
        float f6 = (float) ColorHelper.Argb.getGreen(endColor) / 255.0F;
        float f7 = (float) ColorHelper.Argb.getBlue(endColor) / 255.0F;
        Matrix4f matrix4f = pose.peek().getPositionMatrix();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderLayer.getGui());
        vertexConsumer.vertex(matrix4f, (float) x1, (float) y1, (float)0).color(f1, f2, f3, f).next();
        vertexConsumer.vertex(matrix4f, (float) x1, (float) y2, (float)0).color(f5, f6, f7, f4).next();
        vertexConsumer.vertex(matrix4f, (float) x2, (float) y2, (float)0).color(f5, f6, f7, f4).next();
        vertexConsumer.vertex(matrix4f, (float) x2, (float) y1, (float)0).color(f1, f2, f3, f).next();
        guiGraphics.draw();
    }

    public static void drawHorizontalGradientRect(int x1, int y1, int x2, int y2, int startColor, int endColor) {
        float startAlpha = (float) ColorHelper.Argb.getAlpha(startColor) / 255.0F;
        float startRed = (float) ColorHelper.Argb.getRed(startColor) / 255.0F;
        float startGreen = (float) ColorHelper.Argb.getGreen(startColor) / 255.0F;
        float startBlue = (float) ColorHelper.Argb.getBlue(startColor) / 255.0F;

        float endAlpha = (float) ColorHelper.Argb.getAlpha(endColor) / 255.0F;
        float endRed = (float) ColorHelper.Argb.getRed(endColor) / 255.0F;
        float endGreen = (float) ColorHelper.Argb.getGreen(endColor) / 255.0F;
        float endBlue = (float) ColorHelper.Argb.getBlue(endColor) / 255.0F;
        Matrix4f matrix4f = pose.peek().getPositionMatrix();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderLayer.getGui());
        vertexConsumer.vertex(matrix4f, (float) x1, (float) y1, (float)0).color(startRed, startGreen, startBlue, startAlpha).next();
        vertexConsumer.vertex(matrix4f, (float) x1, (float) y2, (float)0).color(startRed, startGreen, startBlue, startAlpha).next();
        vertexConsumer.vertex(matrix4f, (float) x2, (float) y2, (float)0).color(endRed, endGreen, endBlue, endAlpha).next();
        vertexConsumer.vertex(matrix4f, (float) x2, (float) y1, (float)0).color(endRed, endGreen, endBlue, endAlpha).next();
        guiGraphics.draw();
    }

    public static void draw4ColorRect(int x1, int y1, int x2, int y2, int x1y1Color, int x2y1Color, int x1y2Color, int x2y2Color) {
        float x1y1Alpha = (float) ColorHelper.Argb.getAlpha(x1y1Color) / 255.0F;
        float x1y1Red = (float) ColorHelper.Argb.getRed(x1y1Color) / 255.0F;
        float x1y1Green = (float) ColorHelper.Argb.getGreen(x1y1Color) / 255.0F;
        float x1y1Blue = (float) ColorHelper.Argb.getBlue(x1y1Color) / 255.0F;

        float x2y1Alpha = (float) ColorHelper.Argb.getAlpha(x2y1Color) / 255.0F;
        float x2y1Red = (float) ColorHelper.Argb.getRed(x2y1Color) / 255.0F;
        float x2y1Green = (float) ColorHelper.Argb.getGreen(x2y1Color) / 255.0F;
        float x2y1Blue = (float) ColorHelper.Argb.getBlue(x2y1Color) / 255.0F;

        float x1y2Alpha = (float) ColorHelper.Argb.getAlpha(x1y2Color) / 255.0F;
        float x1y2Red = (float) ColorHelper.Argb.getRed(x1y2Color) / 255.0F;
        float x1y2Green = (float) ColorHelper.Argb.getGreen(x1y2Color) / 255.0F;
        float x1y2Blue = (float) ColorHelper.Argb.getBlue(x1y2Color) / 255.0F;

        float x2y2Alpha = (float) ColorHelper.Argb.getAlpha(x2y2Color) / 255.0F;
        float x2y2Red = (float) ColorHelper.Argb.getRed(x2y2Color) / 255.0F;
        float x2y2Green = (float) ColorHelper.Argb.getGreen(x2y2Color) / 255.0F;
        float x2y2Blue = (float) ColorHelper.Argb.getBlue(x2y2Color) / 255.0F;

        Matrix4f matrix4f = pose.peek().getPositionMatrix();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderLayer.getGui());
        vertexConsumer.vertex(matrix4f, (float) x1, (float) y1, (float)0).color(x1y1Red, x1y1Green, x1y1Blue, x1y1Alpha).next();
        vertexConsumer.vertex(matrix4f, (float) x1, (float) y2, (float)0).color(x1y2Red, x1y2Green, x1y2Blue, x1y2Alpha).next();
        vertexConsumer.vertex(matrix4f, (float) x2, (float) y2, (float)0).color(x2y2Red, x2y2Green, x2y2Blue, x2y2Alpha).next();
        vertexConsumer.vertex(matrix4f, (float) x2, (float) y1, (float)0).color(x2y1Red, x2y1Green, x2y1Blue, x2y1Alpha).next();
        guiGraphics.draw();
    }

    public static void drawHorizontalRainbowRect(int x1, int y1, int x2, int y2, int rainbowType) {
        final float[] counter = {1};
        int dX;
        int tX = x1;
        int delay = (int)RainbowUtils.getRainbowRectSpeed(rainbowType)[1];
        float speed = RainbowUtils.getRainbowRectSpeed(rainbowType)[0];

        float fX;

        if (x1 < x2) {
            fX = x2 - x1;
            fX /= 45;
        } else {
            fX = x1 - x2;
            fX = -(fX / 45);
        }
        dX = fX != 0 ? (int) Math.ceil(fX) : 0;

        while (tX != x2) {
            VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderLayer.getGui());
            Matrix4f matrix4f = pose.peek().getPositionMatrix();

            if (x1 < x2) {
                if (tX + dX > x2) {
                    dX = x2 - tX;
                }
            } else {
                if (tX + dX < x2) {
                    dX = tX - x2;
                }
            }

            int color = ColourUtils.rainbow((int)(counter[0] * delay), speed);

            float f3 = (float) ColorHelper.Argb.getAlpha(color) / 255.0F;
            float f = (float) ColorHelper.Argb.getRed(color) / 255.0F;
            float f1 = (float) ColorHelper.Argb.getGreen(color) / 255.0F;
            float f2 = (float) ColorHelper.Argb.getBlue(color) / 255.0F;
            vertexconsumer.vertex(matrix4f, (float)tX, (float)y1, (float)0).color(f, f1, f2, f3).next();
            vertexconsumer.vertex(matrix4f, (float)tX, (float)y2, (float)0).color(f, f1, f2, f3).next();
            vertexconsumer.vertex(matrix4f, (float)tX + dX, (float)y2, (float)0).color(f, f1, f2, f3).next();
            vertexconsumer.vertex(matrix4f, (float)tX + dX, (float)y1, (float)0).color(f, f1, f2, f3).next();
            guiGraphics.draw();

            tX += dX;
            counter[0]++;
        }
    }

    public static void drawOutlineRect(int x1, int y1, int x2, int y2, int depth, int colour) {
        int outlineX;
        int outlineY;
        outlineX = x1 > x2 ? -depth : depth;
        outlineY = y1 > y2 ? depth : -depth;

        drawRect(x1,y1, x1 + outlineX, y2, colour);
        drawRect(x1 + outlineX, y2, x2, y2 + outlineY, colour);
        drawRect(x2, y2 + outlineY, x2 - outlineX, y1, colour);
        drawRect(x2 - outlineX, y1, x1, y1 - outlineY, colour);
    }

    public static void drawSquare(int x1, int y1, int size, int color) {
        Matrix4f matrix4f = pose.peek().getPositionMatrix();

        float f3 = (float) ColorHelper.Argb.getAlpha(color) / 255.0F;
        float f = (float) ColorHelper.Argb.getRed(color) / 255.0F;
        float f1 = (float) ColorHelper.Argb.getGreen(color) / 255.0F;
        float f2 = (float) ColorHelper.Argb.getBlue(color) / 255.0F;
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderLayer.getGui());
        vertexconsumer.vertex(matrix4f, (float)x1 - size, (float)y1 - size, (float)0).color(f, f1, f2, f3).next();
        vertexconsumer.vertex(matrix4f, (float)x1 - size, (float)y1 + size, (float)0).color(f, f1, f2, f3).next();
        vertexconsumer.vertex(matrix4f, (float)x1 + size, (float)y1 + size, (float)0).color(f, f1, f2, f3).next();
        vertexconsumer.vertex(matrix4f, (float)x1 + size, (float)y1 - size, (float)0).color(f, f1, f2, f3).next();
        guiGraphics.draw();
    }


    public static void drawTriangle(float x, float y, float size, float theta, int color) {
        double radians = Math.toRadians(theta);

        float xA = -size;
        float yA = size;
        double newXA = xA * Math.cos(radians) + yA * Math.sin(radians);
        double newYA = yA * Math.cos(radians) - xA * Math.sin(radians);

        float xB = 0;
        float yB = -(size * 2);
        double newXB = xB * Math.cos(radians) + yB * Math.sin(radians);
        double newYB = yB * Math.cos(radians) - xB * Math.sin(radians);

        float xC = size;
        float yC = size;
        double newXC = xC * Math.cos(radians) + yC * Math.sin(radians);
        double newYC = yC * Math.cos(radians) - xC * Math.sin(radians);


        Matrix4f matrix4f = pose.peek().getPositionMatrix();

        float f3 = (float) ColorHelper.Argb.getAlpha(color) / 255.0F;
        float f = (float) ColorHelper.Argb.getRed(color) / 255.0F;
        float f1 = (float) ColorHelper.Argb.getGreen(color) / 255.0F;
        float f2 = (float) ColorHelper.Argb.getBlue(color) / 255.0F;
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderLayer.getGui());
        vertexconsumer.vertex(matrix4f, (float)(x + newXB), (float)(y + newYB), (float)0).color(f, f1, f2, f3).next();
        vertexconsumer.vertex(matrix4f, (float)(x + newXA), (float)(y + newYA), (float)0).color(f, f1, f2, f3).next();
        vertexconsumer.vertex(matrix4f, (float)(x + newXC), (float)(y + newYC), (float)0).color(f, f1, f2, f3).next();
        vertexconsumer.vertex(matrix4f, (float)(x + newXB), (float)(y + newYB), (float)0).color(f, f1, f2, f3).next();
        guiGraphics.draw();


    }

    public static int drawString(String text, int x1, int y1, int color) {
        Text component = Text.of(text);

        return guiGraphics.drawText(mc.textRenderer, component, x1, y1, color, true);
    }

    public static int drawString(String text, float x1, float y1, int color) {
        return drawString(text, (int) x1, (int) y1, color);
    }

    public static void drawCenteredString(String text, int x1, int y1, int color) {
        guiGraphics.drawText(mc.textRenderer, text, x1 - mc.textRenderer.getWidth(text) / 2, y1, color, true);
    }

    public static void drawTextureRect(Identifier texture, float x1, float y1, float x2, float y2) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        Matrix4f matrix4f = pose.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, x1, y2, 0.0f).texture(0, 1).next();
        bufferBuilder.vertex(matrix4f, x2, y2, 0.0f).texture(1, 1).next();
        bufferBuilder.vertex(matrix4f, x2, y1, 0.0f).texture(1, 0).next();
        bufferBuilder.vertex(matrix4f, x1, y1, 0.0f).texture(0, 0).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void drawTextureRect(Texture texture, float x1, float y1, float x2, float y2) {
        RenderSystem.setShaderTexture(0, texture.getTexId());
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        Matrix4f matrix4f = pose.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, x1, y2, 0.0f).texture(0, 1).next();
        bufferBuilder.vertex(matrix4f, x2, y2, 0.0f).texture(1, 1).next();
        bufferBuilder.vertex(matrix4f, x2, y1, 0.0f).texture(1, 0).next();
        bufferBuilder.vertex(matrix4f, x1, y1, 0.0f).texture(0, 0).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void drawItem(DrawContext context, ItemStack stack, int x, int y, String amountText, boolean onSlot) {
        context.getMatrices().push();
        context.getMatrices().translate(0.0f, 0.0f, 232.0f);
        context.drawItem(stack, x, y);
        if (onSlot)
            context.drawItemInSlot(mc.textRenderer, stack, x, y, amountText);
        context.getMatrices().pop();
    }

    public static BlockPos getCameraBlockPos() {
        Camera camera = mc.getBlockEntityRenderDispatcher().camera;
        if(camera == null)
            return BlockPos.ORIGIN;

        return camera.getBlockPos();
    }

    public static Vec3d getCameraPos() {
        Camera camera = mc.getBlockEntityRenderDispatcher().camera;
        if(camera == null)
            return Vec3d.ZERO;

        return camera.getPos();
    }

    public static RegionPos getCameraRegion() {
        return RegionPos.of(getCameraBlockPos());
    }

    public static void applyRegionalRenderOffset(MatrixStack matrixStack) {
        applyRegionalRenderOffset(matrixStack, getCameraRegion());
    }

    public static void applyRegionalRenderOffset(MatrixStack matrixStack, Chunk chunk) {
        applyRegionalRenderOffset(matrixStack, RegionPos.of(chunk.getPos()));
    }

    public static void applyRegionalRenderOffset(MatrixStack matrixStack, RegionPos region) {
        Vec3d offset = region.toVec3d().subtract(getCameraPos());
        matrixStack.translate(offset.x, offset.y, offset.z);
    }
}
