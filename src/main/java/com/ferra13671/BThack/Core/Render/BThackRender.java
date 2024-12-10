package com.ferra13671.BThack.Core.Render;


import com.ferra13671.BThack.Core.Render.Box.BThackBoxRender;
import com.ferra13671.BThack.Core.Render.Line.BThackLineRender;
import com.ferra13671.BThack.Core.Render.Utils.BThackWorldRenderContext;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.Core.Render.Utils.RainbowUtils;
import com.ferra13671.BThack.api.Shader.ShaderProgram;
import com.ferra13671.BThack.api.Utils.RegionPos;
import com.ferra13671.BThack.api.Utils.RotateUtils;
import com.ferra13671.TextureUtils.GLTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;
import org.joml.Matrix4f;
import com.ferra13671.BThack.api.Interfaces.Mc;

import static com.ferra13671.BThack.Core.Render.Utils.BThackRenderUtils.*;

public final class BThackRender implements Mc {

    public static final VertexConsumerProvider.Immediate bufferSource = mc.getBufferBuilders().getEntityVertexConsumers();
    public static final DrawContext guiGraphics = new DrawContext(mc, bufferSource);
    public static final BThackWorldRenderContext worldRenderContext = new BThackWorldRenderContext();
    public static final BThackBoxRender boxRender = new BThackBoxRender();
    public static final BThackLineRender lineRender = new BThackLineRender();

    private static boolean inited = false;

    public static void init() {
        if (!inited) {
            boxRender.init();
        }
        inited = true;
    }

    public static void trace(Vec3d vec3d, Matrix4f matrix, float partialTicks, float red, float green, float blue, float alpha, BufferBuilder bufferBuilder, Vec3d regionVec) {
        RenderSystem.setShaderColor(red, green, blue, alpha);

        Vec3d start = RotateUtils.getClientLookVec(partialTicks).add(getCameraPos()).subtract(regionVec);
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

        Vec3d end = vec3d.subtract(new Vec3d(regionVec.x, regionVec.y, regionVec.z));
        bufferBuilder.vertex(matrix, (float)start.x, (float)start.y, (float)start.z).next();
        bufferBuilder.vertex(matrix, (float)end.x, (float)end.y, (float)end.z).next();
        draw();
    }

    public static void drawRect(float x1, float y1, float x2, float y2, int color) {
        drawRect(x1, y1, x2, y2, color, guiGraphics.getMatrices().peek().getPositionMatrix());
    }

    public static void drawRect(float x1, float y1, float x2, float y2, int color, Matrix4f matrix4f) {
        if (x1 < x2) {
            float i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            float j = y1;
            y1 = y2;
            y2 = j;
        }

        float[] c = hashCodeToRGBA(color);

        BufferBuilder buffer = prepareToDraw(GameRenderer::getPositionColorProgram);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix4f, x1, y1, 0).color(c[0], c[1], c[2], c[3]).next();
        buffer.vertex(matrix4f, x1, y2, 0).color(c[0], c[1], c[2], c[3]).next();
        buffer.vertex(matrix4f, x2, y2, 0).color(c[0], c[1], c[2], c[3]).next();
        buffer.vertex(matrix4f, x2, y1, 0).color(c[0], c[1], c[2], c[3]).next();

        draw();
    }

    public static void drawLine(float x1, float y1, float x2, float y2, float wight, int color) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

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

        float[] c = hashCodeToRGBA(color);

        BufferBuilder buffer = prepareToDraw(GameRenderer::getPositionColorProgram);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix4f, x2 + wight, y2 + wight, 0).color(c[0], c[1], c[2], c[3]).next();
        buffer.vertex(matrix4f, x2 + wight, y2 - wight, 0).color(c[0], c[1], c[2], c[3]).next();
        buffer.vertex(matrix4f, x1 - wight, y1 - wight, 0).color(c[0], c[1], c[2], c[3]).next();
        buffer.vertex(matrix4f, x1 - wight, y1 + wight, 0).color(c[0], c[1], c[2], c[3]).next();

        draw();
    }

    public static void drawVerticalGradientRect(float x1, float y1, float x2, float y2, int startColor, int endColor) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        float[] startC = hashCodeToRGBA(startColor);
        float[] endC = hashCodeToRGBA(endColor);

        BufferBuilder buffer = prepareToDraw(GameRenderer::getPositionColorProgram);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix4f, x1, y1, 0).color(startC[0], startC[1], startC[2], startC[3]).next();
        buffer.vertex(matrix4f, x1, y2, 0).color(endC[0], endC[1], endC[2], endC[3]).next();
        buffer.vertex(matrix4f, x2, y2, 0).color(endC[0], endC[1], endC[2], endC[3]).next();
        buffer.vertex(matrix4f, x2, y1, 0).color(startC[0], startC[1], startC[2], startC[3]).next();

        draw();
    }

    public static void drawHorizontalGradientRect(float x1, float y1, float x2, float y2, int startColor, int endColor) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        float[] startC = hashCodeToRGBA(startColor);
        float[] endC = hashCodeToRGBA(endColor);

        BufferBuilder buffer = prepareToDraw(GameRenderer::getPositionColorProgram);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix4f, x1, y1, 0).color(startC[0], startC[1], startC[2], startC[3]).next();
        buffer.vertex(matrix4f, x1, y2, 0).color(startC[0], startC[1], startC[2], startC[3]).next();
        buffer.vertex(matrix4f, x2, y2, 0).color(endC[0], endC[1], endC[2], endC[3]).next();
        buffer.vertex(matrix4f,  x2, y1, 0).color(endC[0], endC[1], endC[2], endC[3]).next();

        draw();
    }

    public static void draw4ColorRect(float x1, float y1, float x2, float y2, int x1y1Color, int x2y1Color, int x1y2Color, int x2y2Color) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        float[] x1y1C = hashCodeToRGBA(x1y1Color);
        float[] x2y1C = hashCodeToRGBA(x2y1Color);
        float[] x1y2C = hashCodeToRGBA(x1y2Color);
        float[] x2y2C = hashCodeToRGBA(x2y2Color);

        BufferBuilder buffer = prepareToDraw(GameRenderer::getPositionColorProgram);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix4f, x1, y1, 0).color(x1y1C[0], x1y1C[1], x1y1C[2], x1y1C[3]).next();
        buffer.vertex(matrix4f, x1, y2, 0).color(x1y2C[0], x1y2C[1], x1y2C[2], x1y2C[3]).next();
        buffer.vertex(matrix4f, x2, y2, 0).color(x2y2C[0], x2y2C[1], x2y2C[2], x2y2C[3]).next();
        buffer.vertex(matrix4f, x2, y1, 0).color(x2y1C[0], x2y1C[1], x2y1C[2], x2y1C[3]).next();

        draw();
    }

    public static void drawHorizontalRainbowRect(float x1, float y1, float x2, float y2, int rainbowType) {
        float counter = 1;
        float dX;
        float tX = x1;
        int delay = (int) RainbowUtils.getRainbowRectSpeed(rainbowType)[1];
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

        Tessellator tessellator = Tessellator.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        applyBlend();
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();
        BufferBuilder buffer = tessellator.getBuffer();

        while (tX != x2) {
            if (x1 < x2) {
                if (tX + dX > x2) {
                    dX = x2 - tX;
                }
            } else {
                if (tX + dX < x2) {
                    dX = tX - x2;
                }
            }

            int color = ColorUtils.rainbow((int)(counter * delay), speed);
            float[] c = hashCodeToRGBA(color);

            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

            buffer.vertex(matrix4f, tX, y1, 0).color(c[0], c[1], c[2], c[3]).next();
            buffer.vertex(matrix4f, tX, y2, 0).color(c[0], c[1], c[2], c[3]).next();
            buffer.vertex(matrix4f, tX + dX, y2, 0).color(c[0], c[1], c[2], c[3]).next();
            buffer.vertex(matrix4f, tX + dX, y1, 0).color(c[0], c[1], c[2], c[3]).next();

            draw();

            tX += dX;
            counter++;
        }
    }

    public static void drawOutlineRect(float x1, float y1, float x2, float y2, float depth, int color) {
        float outlineX = x1 > x2 ? -depth : depth;
        float outlineY = y1 > y2 ? depth : -depth;

        drawRect(x1,y1, x1 + outlineX, y2, color);
        drawRect(x1 + outlineX, y2, x2, y2 + outlineY, color);
        drawRect(x2, y2 + outlineY, x2 - outlineX, y1, color);
        drawRect(x2 - outlineX, y1, x1 + outlineX, y1 - outlineY, color);
    }

    public static void drawSquare(float x1, float y1, float size, int color) {
        drawRect(x1 - size, y1 - size, x1 + size, y1 + size, color);
    }


    public static void drawTriangle(float x, float y, float size, float theta, int color) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        double radians = Math.toRadians(theta);

        float xA = -size;
        double newXA = xA * Math.cos(radians) + size * Math.sin(radians);
        double newYA = size * Math.cos(radians) - xA * Math.sin(radians);

        float xB = 0;
        float yB = -(size * 2);
        double newXB = xB * Math.cos(radians) + yB * Math.sin(radians);
        double newYB = yB * Math.cos(radians) - xB * Math.sin(radians);

        double newXC = size * Math.cos(radians) + size * Math.sin(radians);
        double newYC = size * Math.cos(radians) - size * Math.sin(radians);

        float[] c = hashCodeToRGBA(color);

        BufferBuilder buffer = prepareToDraw(GameRenderer::getPositionColorProgram);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix4f, (float)(x + newXB), (float)(y + newYB), 0).color(c[0], c[1], c[2], c[3]).next();
        buffer.vertex(matrix4f, (float)(x + newXA), (float)(y + newYA), 0).color(c[0], c[1], c[2], c[3]).next();
        buffer.vertex(matrix4f, (float)(x + newXC), (float)(y + newYC), 0).color(c[0], c[1], c[2], c[3]).next();
        buffer.vertex(matrix4f, (float)(x + newXB), (float)(y + newYB), 0).color(c[0], c[1], c[2], c[3]).next();

        draw();
    }

    public static void drawString(String text, float x1, float y1, int color, boolean shadow, float size) {

        if (text == null || text.isEmpty()) return;

        guiGraphics.getMatrices().push();
        guiGraphics.getMatrices().scale(size, size, size);
        mc.textRenderer.draw(text, x1 * (1 / size), y1 * (1 / size), color, shadow, guiGraphics.getMatrices().peek().getPositionMatrix(), guiGraphics.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880, mc.textRenderer.isRightToLeft());
        guiGraphics.tryDraw();
        guiGraphics.getMatrices().pop();
        resetShader();
    }

    public static void drawString(String text, float x1, float y1, int color, boolean shadow) {
        drawString(text, x1, y1, color, shadow, 1);
    }

    public static void drawString(String text, float x1, float y1, int color) {
        drawString(text, x1, y1, color, true);
    }

    public static void drawCenteredString(String text, float x1, float y1, int color) {
        drawCenteredString(text, x1, y1, color, 1);
    }

    public static void drawCenteredString(String text, float x1, float y1, int color, float size) {
        drawString(text, (x1 - (mc.textRenderer.getWidth(text) / 2f)), y1, color, true, size);
    }

    /**
     * This is shit, don't use it please, use another renderer on my texture system.
     */
    @Deprecated
    public static void drawTextureRect(Identifier texture, float x1, float y1, float x2, float y2) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, x1, y2, 0.0f).texture(0, 1).next();
        bufferBuilder.vertex(matrix4f, x2, y2, 0.0f).texture(1, 1).next();
        bufferBuilder.vertex(matrix4f, x2, y1, 0.0f).texture(1, 0).next();
        bufferBuilder.vertex(matrix4f, x1, y1, 0.0f).texture(0, 0).next();
        draw();
    }

    public static void drawTextureRect(GLTexture texture, float x1, float y1, float x2, float y2) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        RenderSystem.setShaderTexture(0, texture.getTexId());
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, x1, y2, 0.0f).texture(0, 1).next();
        bufferBuilder.vertex(matrix4f, x2, y2, 0.0f).texture(1, 1).next();
        bufferBuilder.vertex(matrix4f, x2, y1, 0.0f).texture(1, 0).next();
        bufferBuilder.vertex(matrix4f, x1, y1, 0.0f).texture(0, 0).next();
        draw();
    }


    /**
     * THE SHADER MUST HAVE VERTEXFORMAT = VERTEXFORMATS.POSITION!!!!
     */
    public static void drawShader(ShaderProgram shaderProgram, float x1, float y1, float x2, float y2) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        guiGraphics.getMatrices().push();
        shaderProgram.use();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        bufferBuilder.vertex(matrix4f, x1, y2, 0.0f).next();
        bufferBuilder.vertex(matrix4f, x2, y2, 0.0f).next();
        bufferBuilder.vertex(matrix4f, x2, y1, 0.0f).next();
        bufferBuilder.vertex(matrix4f, x1, y1, 0.0f).next();
        draw();
        shaderProgram.release();
        guiGraphics.getMatrices().pop();
        //RenderSystem.setShader(GameRenderer::getPositionProgram);
    }

    public static void drawItem(DrawContext context, ItemStack stack, int x, int y, String amountText, boolean onSlot) {
        drawItem(context, stack, x, y, amountText, onSlot, 1);
    }

    public static void drawItem(DrawContext context, ItemStack stack, int x, int y, String amountText, boolean onSlot, float size) {
        context.getMatrices().push();
        //context.getMatrices().translate(0.0f, 0.0f, 232.0f);
        context.getMatrices().scale(size, size, 1);
        context.drawItem(stack, x, y);
        if (onSlot)
            context.drawItemInSlot(mc.textRenderer, stack, x, y, amountText);
        context.getMatrices().pop();
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
