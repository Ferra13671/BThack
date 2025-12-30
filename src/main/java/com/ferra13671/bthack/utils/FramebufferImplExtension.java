package com.ferra13671.bthack.utils;

import com.ferra13671.cometrenderer.buffer.framebuffer.FramebufferImpl;

public class FramebufferImplExtension implements Mc {

    public static void resizeToScreen(FramebufferImpl framebuffer) {
        int width = mc.getWindow().getWidth();
        int height = mc.getWindow().getHeight();

        if (framebuffer.getWidth() != width || framebuffer.getHeight() != height) {
            framebuffer.resize(width, height);
            framebuffer.clearAll();
        }
    }
}
