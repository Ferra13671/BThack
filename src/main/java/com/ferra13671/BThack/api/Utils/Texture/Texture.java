package com.ferra13671.BThack.api.Utils.Texture;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Utils.PathMode;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import org.apache.commons.io.IOUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.Checks.CHECKS;
import static org.lwjgl.system.Checks.check;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memByteBufferSafe;

/**
 * @author Ferra13671
 *
 * @note Taken and modified from Ferra2DEngine
 */
// TODO: Uhhhhhhh... more tests?
public class Texture {


    public ByteBuffer data;

    private int texId;

    private int width;
    private int height;

    public void genImage(InputStream stream, TextureColorMode colorMode) throws IOException {
        ByteBuffer buffer = null;
        try {
            buffer = TextureUtil.readResource(stream);
            buffer.rewind();

            MemoryStack memoryStack = MemoryStack.stackPush();

            try {

                IntBuffer xBuffer = memoryStack.mallocInt(1);
                IntBuffer yBuffer = memoryStack.mallocInt(1);
                IntBuffer channelsBuffer = memoryStack.mallocInt(1);
                ByteBuffer byteBuffer = STBImage.stbi_load_from_memory(buffer, xBuffer, yBuffer, channelsBuffer, colorMode == TextureColorMode.RGBA ? 4 : 3);

                if (byteBuffer != null) {

                    this.width = xBuffer.get(0);
                    this.height = yBuffer.get(0);

                    GlStateManager._bindTexture(texId);
                    int maxLevel = 0;
                    GlStateManager._texParameter(3553, 33085, maxLevel);
                    GlStateManager._texParameter(3553, 33082, 0);
                    GlStateManager._texParameter(3553, 33083, maxLevel);
                    GlStateManager._texParameter(3553, 34049, 0.0F);

                    for (int i = 0; i <= maxLevel; ++i) {
                        GlStateManager._texImage2D(3553, i, colorMode == TextureColorMode.RGBA ? GL_RGBA : GL_RGB, width >> i, height >> i, 0, colorMode == TextureColorMode.RGBA ? GL_RGBA : GL_RGB, 5121, null);
                    }
                    GlStateManager._pixelStore(3314, 0);
                    GlStateManager._pixelStore(3316, 0);
                    GlStateManager._pixelStore(3315, 0);
                    GlStateManager._pixelStore(3317, colorMode == TextureColorMode.RGBA ? 4 : 3);
                    GlStateManager._texSubImage2D(3553, 0, 0, 0, width, height, colorMode == TextureColorMode.RGBA ? GL_RGBA : GL_RGB, 5121, MemoryUtil.memAddress(byteBuffer));



                    /*
                GlStateManager._bindTexture(texId);
                GlStateManager._texParameter(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                GlStateManager._texParameter(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
                GlStateManager._texParameter(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                GlStateManager._texParameter(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                glTexImage2D(GL_TEXTURE_2D, 0, colorMode == TextureColorMode.RGBA ? GL_RGBA : GL_RGB, this.width, this.height, 0, colorMode == TextureColorMode.RGBA ? GL_RGBA : GL_RGB, GL_UNSIGNED_BYTE, buffer);

                 */

                    GlStateManager._bindTexture(0);

                    nstbi_image_free(MemoryUtil.memAddress(byteBuffer));
                }
            } catch (Throwable var9) {
                if (memoryStack != null) {
                    try {
                        memoryStack.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }

                throw var9;
            }

            if (memoryStack != null) {
                memoryStack.close();
            }
        } finally {
            MemoryUtil.memFree(buffer);
            IOUtils.closeQuietly(stream);
        }
    }

    /**
     * Loads an image into the texture along the specified path and in the specified mode.
     *
     * @param path   Paths to the image file.
     * @param pathMode   Select how the image path will be treated: inside the jar file or outside.
     */
    public Texture(String path, PathMode pathMode, TextureColorMode colorMode) {
        RenderSystem.recordRenderCall(() -> {
            texId = TextureUtil.generateTextureId();

            try {
                InputStream stream;
                if (pathMode == PathMode.INSIDEJAR)
                    stream = Texture.class.getClassLoader().getResourceAsStream(path);
                else
                    stream = Files.newInputStream(Paths.get(path));

                BThack.log(path);

                genImage(stream, colorMode);  //I don't know if it will work consistently forever, but, it's working so far :/


                //generateImage(bufferedImage, colorMode);
            } catch (IOException | IllegalArgumentException e) {

                //BufferedImage img = genNotFoundImage();
                //this.width = img.getWidth();                              Nah, lol.
                //this.height = img.getHeight();

                //generateImage(img, colorMode);
            }

            /*
            if (pathMode ==TexturePathMode.OUTSIDEJAR) {
                MemoryStack memoryStack = MemoryStack.stackPush();

                ByteBuffer image = null;

                try {

                    IntBuffer width = memoryStack.mallocInt(1);
                    IntBuffer height = memoryStack.mallocInt(1);
                    IntBuffer channels = memoryStack.mallocInt(1);
                    image = load(path, width, height, channels, 0);

                    if (image != null) {
                        image.rewind();

                        glBindTexture(GL_TEXTURE_2D, texId);


                        //Texture parameters
                        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
                        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

                        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);


                        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

                        this.width = width.get(0);
                        this.height = height.get(0);
                        this.data = image;

                        MemoryUtil.memFree(image);

                        glBindTexture(GL_TEXTURE_2D, 0);
                    } else {
                        BufferedImage img = genNotFoundImage();
                        this.width = img.getWidth();
                        this.height = img.getHeight();

                        generateImage(img, colorMode);
                    }
                } catch (Throwable var9) {
                    if (memoryStack != null) {
                        try {
                            if (image != null)
                                MemoryUtil.memFree(image);
                            memoryStack.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                    }
                }

                if (memoryStack != null) {
                    if (image != null)
                        MemoryUtil.memFree(image);
                    memoryStack.close();
                }
            } else {
                try {
                    BufferedImage bufferedImage = ImageIO.read(Texture.class.getClassLoader().getResourceAsStream(path));

                    this.width = bufferedImage.getWidth();
                    this.height = bufferedImage.getHeight();

                    generateImage(bufferedImage, colorMode);
                } catch (IOException | IllegalArgumentException e) {
                    BufferedImage img = genNotFoundImage();
                    this.width = img.getWidth();
                    this.height = img.getHeight();

                    generateImage(img, colorMode);
                }
            }

             */
        });
    }

    /**
     * Removes the texture from video memory and returns null.
     * Use this for times when the texture is no longer needed to free up some video memory.
     *
     * @return   null.
     */
    public Texture deleteTexture() {
        GlStateManager._deleteTexture(texId);
        return null;
    }

    public int getHeight() {
        return height;
    }

    public int getTexId() {
        return this.texId;
    }

    public int getWidth() {
        return width;
    }



    public void generateImage(BufferedImage bufferedImage, TextureColorMode colorMode) {
        int[] pixels = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
        pixels = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), pixels, 0, bufferedImage.getWidth());

        MemoryStack.stackPush();

        ByteBuffer data = MemoryUtil.memAlloc((bufferedImage.getWidth() * bufferedImage.getHeight() * 4));

        for (int y = 0; y < bufferedImage.getHeight(); y++)
        {
            for (int x = 0; x < bufferedImage.getWidth(); x++)
            {
                int pixel = pixels[y * bufferedImage.getWidth() + x];
                data.put((byte) ((pixel >> 16) & 0xFF));
                data.put((byte) ((pixel >> 8) & 0xFF));
                data.put((byte) (pixel & 0xFF));
                data.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        data.flip();



        /*
        glBindTexture(GL_TEXTURE_2D, texId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, colorMode == TextureColorMode.RGBA ? GL_RGBA : GL_RGB, bufferedImage.getWidth(), bufferedImage.getHeight(), 0, colorMode == TextureColorMode.RGBA ? GL_RGBA : GL_RGB, GL_UNSIGNED_BYTE, data);

        this.data = data;

        glBindTexture(GL_TEXTURE_2D, 0);

         */

        GlStateManager._bindTexture(texId);
        GlStateManager._texParameter(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        GlStateManager._texParameter(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        GlStateManager._texParameter(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        GlStateManager._texParameter(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, colorMode == TextureColorMode.RGBA ? GL_RGBA : GL_RGB, bufferedImage.getWidth(), bufferedImage.getHeight(), 0, colorMode == TextureColorMode.RGBA ? GL_RGBA : GL_RGB, GL_UNSIGNED_BYTE, data);
        this.data = data;
        GlStateManager._bindTexture(0);

        MemoryUtil.memFree(data);
        MemoryStack.stackPop();
    }


    public static BufferedImage genNotFoundImage() {
        BufferedImage nf = new BufferedImage(64, 64, BufferedImage.TYPE_4BYTE_ABGR);

        Graphics2D graphics = (Graphics2D) nf.getGraphics();

        graphics.setBackground(Color.DARK_GRAY);
        graphics.clearRect(0, 0, 64, 64);

        graphics.setColor(Color.MAGENTA);
        graphics.fillRect(0, 0, 32, 32);
        graphics.fillRect(32, 32, 32, 32);

        BufferedImage nf2 = new BufferedImage(128, 128, BufferedImage.TYPE_4BYTE_ABGR);

        graphics = (Graphics2D) nf2.getGraphics();

        graphics.drawImage(nf, 0, 0, null);
        graphics.drawImage(nf, 64, 0, null);
        graphics.drawImage(nf, 64, 64, null);
        graphics.drawImage(nf, 0, 64, null);

        nf.flush();

        return nf2;
    }


    public static ByteBuffer load(CharSequence filename, IntBuffer x, IntBuffer y, IntBuffer channels_in_file, int desired_channels) {
        if (CHECKS) {
            check(x, 1);
            check(y, 1);
            check(channels_in_file, 1);
        }
        MemoryStack stack = MemoryStack.stackPush();
        int stackPointer = stack.getPointer();
        try {
            stack.nUTF8(filename, true);
            long filenameEncoded = stack.getPointerAddress();
            long __result = nstbi_load(filenameEncoded, memAddress(x), memAddress(y), memAddress(channels_in_file), desired_channels);
            return memByteBufferSafe(__result, x.get(x.position()) * y.get(y.position()) * (desired_channels != 0 ? desired_channels : channels_in_file.get(channels_in_file.position())));
        } finally {
            stack.setPointer(stackPointer);
            stack.pop();
            stack.close();
        }
    }
}
