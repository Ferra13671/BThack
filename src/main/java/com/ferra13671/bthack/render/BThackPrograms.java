package com.ferra13671.bthack.render;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.shader.ShaderType;

public class BThackPrograms {

    public final GlProgram position = CometLoaders.IN_JAR.createProgramBuilder(
    )
            .name("position")
            .shader(BThackRenderSystem.SHADER_ENTRIES.positionVertex, ShaderType.Vertex)
            .shader(BThackRenderSystem.SHADER_ENTRIES.positionFragment, ShaderType.Fragment)
            .build();
}
