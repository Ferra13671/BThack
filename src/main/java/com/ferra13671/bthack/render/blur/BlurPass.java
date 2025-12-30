package com.ferra13671.bthack.render.blur;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import lombok.Getter;
import org.joml.Vector2f;

public class BlurPass {
    @Getter
    private final Vector2f offset;
    @Getter
    private final int radius;
    private final float[] weights;

    public BlurPass(Vector2f offset, int radius) {
        this.offset = offset;
        this.radius = radius;
        this.weights = calculateGaussianWeights(radius, radius);
    }

    public void draw(Framebuffer input, Framebuffer output, Mesh mesh) {
        GlProgram program = CometRenderer.getGlobalProgram();

        program.getUniform("offsets", UniformType.VEC2).set(this.offset);
        program.getUniform("radius", UniformType.INT).set(this.radius);
        program.getUniform("weights", UniformType.FLOAT_ARRAY).set(this.weights);
        program.getSampler(0).set(input.getColorTextureId());

        output.bind(false);
        CometRenderer.draw(mesh, false);
    }

    private static float[] calculateGaussianWeights(int radius, float sigma) {
        int size = radius + 1;
        float[] w = new float[size];
        float sum = 0f;

        for (int i = 0; i < size; i++) {
            w[i] = (float) Math.exp(- (i * i) / (2 * sigma * sigma));
            sum += (i == 0 ? w[i] : w[i] * 2);
        }
        for (int i = 0; i < size; i++) {
            w[i] /= sum;
        }
        return w;
    }
}
