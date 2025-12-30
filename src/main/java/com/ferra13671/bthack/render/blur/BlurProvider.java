package com.ferra13671.bthack.render.blur;

import com.ferra13671.bthack.loader.api.ClientLoader;
import com.ferra13671.bthack.loader.api.ResourcePath;
import com.ferra13671.bthack.managers.config.BThackElementTypes;
import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.utils.FramebufferImplExtension;
import com.ferra13671.bthack.utils.Mc;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometVertexFormats;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.buffer.framebuffer.FramebufferImpl;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftFramebuffer;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.configprovider.element.ElementType;
import com.ferra13671.configprovider.parameter.ParameterImpl;
import com.ferra13671.configprovider.provider.ConfigProvider;
import com.ferra13671.configprovider.provider.impl.ArrayConfigProvider;
import com.google.gson.JsonArray;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import org.joml.Vector2f;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@ExtensionMethod({FramebufferImpl.class, FramebufferImplExtension.class})
public class BlurProvider implements Mc {
    public static final int DEFAULT_BLUR_RADIUS = 3;

    @Getter
    private final FramebufferImpl blurFrameBuffer = new FramebufferImpl("Blur framebuffer", false, 1, 1, Color.WHITE, 1);
    private final FramebufferImpl blurFrameBufferInternal1 = new FramebufferImpl("Blur framebuffer internal 1", false, 1, 1, Color.WHITE, 1);
    private final FramebufferImpl blurFrameBufferInternal2 = new FramebufferImpl("Blur framebuffer internal 2", false, 1, 1, Color.WHITE, 1);

    private final List<BlurPass> passes = new ArrayList<>();

    private final ConfigProvider configProvider = new ArrayConfigProvider(
            new ParameterImpl<>(ElementType.JSON_ARRAY) {
                @Override
                protected JsonArray toElement() {
                    JsonArray jsonArray = new JsonArray();

                    passes.forEach(blurPass -> jsonArray.add(BThackElementTypes.BLUR_PASS.toJsonFunction().apply(blurPass)));

                    return jsonArray;
                }

                @Override
                protected void fromElement(JsonArray element) {
                    element.forEach(jsonElement -> {
                        if (BThackElementTypes.BLUR_PASS.equalsFunction().apply(jsonElement))
                            passes.add(BThackElementTypes.BLUR_PASS.toElementFunction().apply(jsonElement));
                    });
                }
            }
    );
    {
        loadDefault();
    }

    @SneakyThrows
    private void loadDefault() {
        InputStream inputStream = ClientLoader.getResource(new ResourcePath("bthack-client", "blur-config.json"));
        loadConfig(inputStream);
        inputStream.close();
    }

    public void loadConfig(InputStream inputStream) {
        this.configProvider.load(inputStream);
    }

    public void drawBlur() {
        this.blurFrameBuffer.resizeToScreen();
        this.blurFrameBufferInternal1.resizeToScreen();
        this.blurFrameBufferInternal2.resizeToScreen();

        Framebuffer input = new MinecraftFramebuffer(mc.getMainRenderTarget(), Color.WHITE, 1);
        Framebuffer output = this.blurFrameBufferInternal1;

        float width = mc.getWindow().getWidth();
        float height = mc.getWindow().getHeight();

        Mesh mesh = Mesh.builder(DrawMode.QUADS, CometVertexFormats.POSITION)
                .vertex(0, 0, 0)
                .vertex(0, height, 0)
                .vertex(width, height, 0)
                .vertex(width, 0, 0)
                .buildNullable();

        CometRenderer.setGlobalProgram(BThackRenderSystem.PROGRAMS.BLUR_FRAME);
        CometRenderer.initShaderColor();
        MinecraftPlugin.initMatrix();
        CometRenderer.getGlobalProgram().getUniform("texelSize", UniformType.VEC2).set(new Vector2f(1f / mc.getWindow().getWidth(), 1f / mc.getWindow().getHeight()));

        for (int i = 0; i < this.passes.size(); i++) {
            if (i == this.passes.size() - 1)
                output = this.blurFrameBuffer;

            this.passes.get(i).draw(input, output, mesh);

            input = output;
            output = i % 2 == 0 ? this.blurFrameBufferInternal2 : this.blurFrameBufferInternal1;
        }

        MinecraftPlugin.bindMainFramebuffer(false);
        mesh.close();
    }
}
