package com.ferra13671.bthack.render;

import com.ferra13671.bthack.loader.api.ResourcePath;
import com.ferra13671.cometrenderer.compiler.GlslFileEntry;


public class BThackShaderEntries {

    public final GlslFileEntry POSITION_VERTEX = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("position.vertex", new ResourcePath("bthack-client", "shaders/impl/position.vert"));
    public final GlslFileEntry POSITION_FRAGMENT = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("position.fragment", new ResourcePath("bthack-client", "shaders/impl/position.frag"));

    public final GlslFileEntry POSITION_COLOR_VERTEX = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("position-color.vertex", new ResourcePath("bthack-client", "shaders/impl/position-color.vert"));
    public final GlslFileEntry POSITION_COLOR_FRAGMENT = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("position-color.fragment", new ResourcePath("bthack-client", "shaders/impl/position-color.frag"));

    public final GlslFileEntry ROUNDED_VERTEX = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("rounded.vertex", new ResourcePath("bthack-client", "shaders/impl/rounded.vert"));
    public final GlslFileEntry ROUNDED_FRAGMENT = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("rounded.fragment", new ResourcePath("bthack-client", "shaders/impl/rounded.frag"));

    public final GlslFileEntry ROUNDED_OUTLINED_VERTEX = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("rounded-outlined.vertex", new ResourcePath("bthack-client", "shaders/impl/rounded-outlined.vert"));
    public final GlslFileEntry ROUNDED_OUTLINED_FRAGMENT = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("rounded-outlined.fragment", new ResourcePath("bthack-client", "shaders/impl/rounded-outlined.frag"));

    public final GlslFileEntry TEXTURE_VERTEX = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("texture.vertex", new ResourcePath("bthack-client", "shaders/impl/texture.vert"));
    public final GlslFileEntry TEXTURE_FRAGMENT = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("texture.fragment", new ResourcePath("bthack-client", "shaders/impl/texture.frag"));

    public final GlslFileEntry COLORED_TEXTURE_VERTEX = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("colored-texture.vertex", new ResourcePath("bthack-client", "shaders/impl/colored-texture.vert"));
    public final GlslFileEntry COLORED_TEXTURE_FRAGMENT = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("colored-texture.fragment", new ResourcePath("bthack-client", "shaders/impl/colored-texture.frag"));

    public final GlslFileEntry ROUNDED_TEXTURE_VERTEX = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("rounded-texture.vertex", new ResourcePath("bthack-client", "shaders/impl/rounded-texture.vert"));
    public final GlslFileEntry ROUNDED_TEXTURE_FRAGMENT = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("rounded-texture.fragment", new ResourcePath("bthack-client", "shaders/impl/rounded-texture.frag"));

    public final GlslFileEntry BLUR_FRAME_FRAGMENT = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("blur-frame.fragment", new ResourcePath("bthack-client", "shaders/impl/blur-frame.frag"));

    public final GlslFileEntry BLIT_FRAGMENT = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("blit.fragment", new ResourcePath("bthack-client", "shaders/impl/blit.frag"));
}
