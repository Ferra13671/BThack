package com.ferra13671.bthack.render;

import com.ferra13671.bthack.loader.api.ResourcePath;
import com.ferra13671.cometrenderer.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.plugins.shaderlibraries.GlShaderLibraryBuilder;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BThackShaderLibraries {

    GlslFileEntry MATRICES = new GlShaderLibraryBuilder<>(BThackRenderSystem.COMET_LOADER)
            .name("matrices")
            .library(new ResourcePath("bthack-client", "shaders/libraries/matrices.glsl"))
            .uniform("Projection", UniformType.BUFFER)
            .uniform("modelViewMat", UniformType.MATRIX4)
            .build();

    GlslFileEntry ROUNDED = new GlShaderLibraryBuilder<>(BThackRenderSystem.COMET_LOADER)
            .name("rounded")
            .library(new ResourcePath("bthack-client", "shaders/libraries/rounded.glsl"))
            .build();

    GlslFileEntry SHADER_COLOR = new GlShaderLibraryBuilder<>(BThackRenderSystem.COMET_LOADER)
            .name("shaderColor")
            .library(new ResourcePath("bthack-client", "shaders/libraries/shaderColor.glsl"))
            .uniform("shaderColor", UniformType.VEC4)
            .build();
}
