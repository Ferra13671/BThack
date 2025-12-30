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

    public final GlslFileEntry TEXTURE_COLOR_VERTEX = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("texture-color.vertex", new ResourcePath("bthack-client", "shaders/impl/texture-color.vert"));
    public final GlslFileEntry TEXTURE_COLOR_FRAGMENT = BThackRenderSystem.COMET_LOADER.createGlslFileEntry("texture-color.fragment", new ResourcePath("bthack-client", "shaders/impl/texture-color.frag"));
}
