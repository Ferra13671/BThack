package com.ferra13671.bthack.render;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.compiler.GlslFileEntry;

public class BThackShaderEntries {

    public final GlslFileEntry POSITION_VERTEX = CometLoaders.IN_JAR.createGlslFileEntry("position.vertex", "assets/bthack-client/shaders/impl/position.vert");
    public final GlslFileEntry POSITION_FRAGMENT = CometLoaders.IN_JAR.createGlslFileEntry("position.fragment", "assets/bthack-client/shaders/impl/position.frag");
}
