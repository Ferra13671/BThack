package com.ferra13671.bthack.render;

import com.ferra13671.bthack.init.InitStageImpl;
import com.ferra13671.bthack.loader.api.ClientLoader;
import com.ferra13671.bthack.loader.api.ResourcePath;
import com.ferra13671.bthack.mixins.IGlBuffer;
import com.ferra13671.bthack.render.blur.BlurProvider;
import com.ferra13671.bthack.utils.Mc;
import com.ferra13671.cometrenderer.CometLoader;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.betterexceptions.BetterExceptionsPlugin;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.plugins.shaderlibraries.ShaderLibrariesPlugin;
import com.ferra13671.gltextureutils.GLTexture;
import com.ferra13671.gltextureutils.loader.TextureLoaders;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@UtilityClass
public class BThackRenderSystem implements Mc {
    private final List<Runnable> queueCalls = new CopyOnWriteArrayList<>();
    public final CometLoader<ResourcePath> COMET_LOADER = new CometLoader<>() {
        @Override
        public String load(ResourcePath path) throws Exception {
            InputStream inputStream = ClientLoader.getResource(path);
            String content = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            inputStream.close();
            return content;
        }
    };
    private CachedOrthoProjectionMatrixBuffer matrix;
    public BThackShaderEntries SHADER_ENTRIES;
    public BThackPrograms PROGRAMS;
    public BThackTextures TEXTURES;
    public BlurProvider BLUR_PROVIDER;
    public GLTexture TEST_TEXTURE;

    public static InitStageImpl createInitStage() {
        InitStageImpl initStage = InitStageImpl.of("Init Renderer", () -> {
            matrix = new CachedOrthoProjectionMatrixBuffer("bthack-client-projection-matrix", -1000, 1000, true);
            TEST_TEXTURE = TextureLoaders.INPUT_STREAM.createTextureBuilder()
                    .name("test-texture")
                    .info(ClientLoader.getResource(new ResourcePath("bthack-client", "textures/test.jpg")))
                    .build();
        });

        initStage.registerLast(InitStageImpl.of("Init CometRenderer", () -> {
            CometRenderer.init();
            MinecraftPlugin.init(glGpuBuffer -> ((IGlBuffer) glGpuBuffer)._getHandle(), () -> mc.getWindow().getGuiScale());
            BetterExceptionsPlugin.init();
        }));
        initStage.registerLast(InitStageImpl.of("Load shader libraries", BThackRenderSystem::loadShaderLibraries));
        initStage.registerLast(InitStageImpl.of("Load shader entries", () -> SHADER_ENTRIES = new BThackShaderEntries()));
        initStage.registerLast(InitStageImpl.of("Compile shaders", () -> PROGRAMS = new BThackPrograms()));
        initStage.registerLast(InitStageImpl.of("Load textures", () -> TEXTURES = new BThackTextures()));
        initStage.registerLast(InitStageImpl.of("Load blur provider", () -> BLUR_PROVIDER = new BlurProvider()));

        return initStage;
    }

    public void prepareProjection() {
        float width = mc.getWindow().getWidth();
        float height = mc.getWindow().getHeight();

        RenderSystem.setProjectionMatrix(matrix.getBuffer(width, height), ProjectionType.ORTHOGRAPHIC);
    }

    public void loadShaderLibraries() {

        ShaderLibrariesPlugin.registerShaderLibraries(
                BThackShaderLibraries.MATRICES,
                BThackShaderLibraries.ROUNDED,
                BThackShaderLibraries.SHADER_COLOR
        );
    }

    public void registerRenderCall(Runnable runnable) {
        queueCalls.add(runnable);
    }

    public void invokeRenderCalls() {
        synchronized (queueCalls) {
            if (queueCalls.isEmpty()) return;

            List<Runnable> renderCalls = List.copyOf(queueCalls);
            queueCalls.clear();

            for (Runnable runnable : renderCalls)
                runnable.run();
        }
    }
}
