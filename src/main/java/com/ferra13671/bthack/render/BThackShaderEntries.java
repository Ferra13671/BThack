package com.ferra13671.bthack.render;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.compiler.GlslFileEntry;

public class BThackShaderEntries {

    public final GlslFileEntry POSITION_VERTEX = CometLoaders.IN_JAR.createGlslFileEntry("position.vertex", "assets/bthack-client/shaders/impl/position.vert");
    public final GlslFileEntry POSITION_FRAGMENT = CometLoaders.IN_JAR.createGlslFileEntry("position.fragment", "assets/bthack-client/shaders/impl/position.frag");

    public final GlslFileEntry POSITION_COLOR_VERTEX = CometLoaders.IN_JAR.createGlslFileEntry("position-color.vertex", "assets/bthack-client/shaders/impl/position-color.vert");
    public final GlslFileEntry POSITION_COLOR_FRAGMENT = CometLoaders.IN_JAR.createGlslFileEntry("position-color.fragment", "assets/bthack-client/shaders/impl/position-color.frag");

    public final GlslFileEntry TEXTURE_VERTEX = CometLoaders.IN_JAR.createGlslFileEntry("texture.vertex", "assets/bthack-client/shaders/impl/texture.vert");
    public final GlslFileEntry TEXTURE_FRAGMENT = CometLoaders.IN_JAR.createGlslFileEntry("texture.fragment", "assets/bthack-client/shaders/impl/texture.frag");

    public final GlslFileEntry TEXTURE_COLOR_VERTEX = CometLoaders.IN_JAR.createGlslFileEntry("texture-color.vertex", "assets/bthack-client/shaders/impl/texture-color.vert");
    public final GlslFileEntry TEXTURE_COLOR_FRAGMENT = CometLoaders.IN_JAR.createGlslFileEntry("texture-color.fragment", "assets/bthack-client/shaders/impl/texture-color.frag");
}
