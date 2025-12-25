package com.ferra13671.bthack.render;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.plugins.shaderlibraries.GlShaderLibraryBuilder;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BThackShaderLibraries {

    GlslFileEntry MATRICES = new GlShaderLibraryBuilder<>(CometLoaders.IN_JAR)
            .name("matrices")
            .library("assets/bthack-client/shaders/libraries/matrices.glsl")
            .uniform("Projection", UniformType.BUFFER)
            .uniform("modelViewMat", UniformType.MATRIX4)
            .build();

    GlslFileEntry SHADER_COLOR = new GlShaderLibraryBuilder<>(CometLoaders.IN_JAR)
            .name("shaderColor")
            .library("assets/bthack-client/shaders/libraries/shaderColor.glsl")
            .uniform("shaderColor", UniformType.VEC4)
            .build();
}
