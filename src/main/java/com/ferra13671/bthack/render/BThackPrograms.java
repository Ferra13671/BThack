package com.ferra13671.bthack.render;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import com.ferra13671.cometrenderer.program.uniform.UniformType;

public class BThackPrograms {

    public final GlProgram POSITION = CometLoaders.IN_JAR.createProgramBuilder()
            .name("position")
            .shader(BThackRenderSystem.SHADER_ENTRIES.POSITION_VERTEX, ShaderType.Vertex)
            .shader(BThackRenderSystem.SHADER_ENTRIES.POSITION_FRAGMENT, ShaderType.Fragment)
            .build();

    public final GlProgram POSITION_COLOR = CometLoaders.IN_JAR.createProgramBuilder()
            .name("position-color")
            .shader(BThackRenderSystem.SHADER_ENTRIES.POSITION_COLOR_VERTEX, ShaderType.Vertex)
            .shader(BThackRenderSystem.SHADER_ENTRIES.POSITION_COLOR_FRAGMENT, ShaderType.Fragment)
            .build();

    public final GlProgram ROUNDED = CometLoaders.IN_JAR.createProgramBuilder()
            .name("rounded")
            .shader(BThackRenderSystem.SHADER_ENTRIES.ROUNDED_VERTEX, ShaderType.Vertex)
            .shader(BThackRenderSystem.SHADER_ENTRIES.ROUNDED_FRAGMENT, ShaderType.Fragment)
            .uniform("height", UniformType.FLOAT)
            .build();

    public final GlProgram ROUNDED_OUTLINED = CometLoaders.IN_JAR.createProgramBuilder()
            .name("rounded-outlined")
            .shader(BThackRenderSystem.SHADER_ENTRIES.ROUNDED_OUTLINED_VERTEX, ShaderType.Vertex)
            .shader(BThackRenderSystem.SHADER_ENTRIES.ROUNDED_OUTLINED_FRAGMENT, ShaderType.Fragment)
            .uniform("height", UniformType.FLOAT)
            .build();

    public final GlProgram ROUNDED_SHADOW = CometLoaders.IN_JAR.createProgramBuilder()
            .name("rounded-outlined")
            .shader(BThackRenderSystem.SHADER_ENTRIES.ROUNDED_VERTEX, ShaderType.Vertex)
            .shader(BThackRenderSystem.SHADER_ENTRIES.ROUNDED_SHADOW_FRAGMENT, ShaderType.Fragment)
            .uniform("height", UniformType.FLOAT)
            .build();

    public final GlProgram TEXTURE = CometLoaders.IN_JAR.createProgramBuilder()
            .name("texture")
            .shader(BThackRenderSystem.SHADER_ENTRIES.TEXTURE_VERTEX, ShaderType.Vertex)
            .shader(BThackRenderSystem.SHADER_ENTRIES.TEXTURE_FRAGMENT, ShaderType.Fragment)
            .sampler("u_Texture")
            .build();

    public final GlProgram COLORED_TEXTURE = CometLoaders.IN_JAR.createProgramBuilder()
            .name("colored-texture")
            .shader(BThackRenderSystem.SHADER_ENTRIES.COLORED_TEXTURE_VERTEX, ShaderType.Vertex)
            .shader(BThackRenderSystem.SHADER_ENTRIES.COLORED_TEXTURE_FRAGMENT, ShaderType.Fragment)
            .sampler("u_Texture")
            .build();

    public final GlProgram ROUNDED_TEXTURE = CometLoaders.IN_JAR.createProgramBuilder()
            .name("rounded-texture")
            .shader(BThackRenderSystem.SHADER_ENTRIES.ROUNDED_TEXTURE_VERTEX, ShaderType.Vertex)
            .shader(BThackRenderSystem.SHADER_ENTRIES.ROUNDED_TEXTURE_FRAGMENT, ShaderType.Fragment)
            .sampler("u_Texture")
            .uniform("height", UniformType.FLOAT)
            .build();

    public final GlProgram ROUNDED_BLUR = CometLoaders.IN_JAR.createProgramBuilder()
            .name("rounded-blur")
            .shader(BThackRenderSystem.SHADER_ENTRIES.ROUNDED_BLUR_VERTEX, ShaderType.Vertex)
            .shader(BThackRenderSystem.SHADER_ENTRIES.ROUNDED_BLUR_FRAGMENT, ShaderType.Fragment)
            .sampler("u_Texture")
            .uniform("height", UniformType.FLOAT)
            .build();

    public final GlProgram BLUR_FRAME = CometLoaders.IN_JAR.createProgramBuilder()
            .name("blur-frame")
            .shader(BThackRenderSystem.SHADER_ENTRIES.POSITION_VERTEX, ShaderType.Vertex)
            .shader(BThackRenderSystem.SHADER_ENTRIES.BLUR_FRAME_FRAGMENT, ShaderType.Fragment)
            .sampler("u_Texture")
            .uniform("weights", UniformType.FLOAT_ARRAY)
            .uniform("offsets", UniformType.VEC2)
            .uniform("radius", UniformType.INT)
            .uniform("texelSize", UniformType.VEC2)
            .build();

    public final GlProgram BLIT = CometLoaders.IN_JAR.createProgramBuilder()
            .name("blit")
            .shader(BThackRenderSystem.SHADER_ENTRIES.POSITION_VERTEX, ShaderType.Vertex)
            .shader(BThackRenderSystem.SHADER_ENTRIES.BLIT_FRAGMENT, ShaderType.Fragment)
            .sampler("u_Texture")
            .build();
}
