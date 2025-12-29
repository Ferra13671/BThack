package com.ferra13671.bthack.render;

import com.ferra13671.bthack.init.InitStageImpl;
import com.ferra13671.bthack.mixins.IGlBuffer;
import com.ferra13671.bthack.utils.Mc;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.betterexceptions.BetterExceptionsPlugin;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.plugins.shaderlibraries.ShaderLibrariesPlugin;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BThackRenderSystem implements Mc {
    private static final List<Runnable> queueCalls = new CopyOnWriteArrayList<>();
    private static CachedOrthoProjectionMatrixBuffer matrix;
    public static BThackShaderEntries SHADER_ENTRIES;
    public static BThackPrograms PROGRAMS;
    public static BThackTextures TEXTURES;

    public static InitStageImpl createInitStage() {
        InitStageImpl initStage = InitStageImpl.of("Init Renderer", () -> matrix = new CachedOrthoProjectionMatrixBuffer("bthack-client-projection-matrix", -1000, 1000, true));

        initStage.registerLast(InitStageImpl.of("Init CometRenderer", () -> {
            CometRenderer.init();
            MinecraftPlugin.init(glGpuBuffer -> ((IGlBuffer) glGpuBuffer)._getHandle(), () -> mc.getWindow().getGuiScale());
            BetterExceptionsPlugin.init();
        }));
        initStage.registerLast(InitStageImpl.of("Load shader libraries", BThackRenderSystem::loadShaderLibraries));
        initStage.registerLast(InitStageImpl.of("Load shader entries", () -> SHADER_ENTRIES = new BThackShaderEntries()));
        initStage.registerLast(InitStageImpl.of("Compile shaders", () -> PROGRAMS = new BThackPrograms()));
        initStage.registerLast(InitStageImpl.of("Load textures", () -> TEXTURES = new BThackTextures()));

        return initStage;
    }

    public static void prepareProjection() {
        float width = mc.getWindow().getWidth();
        float height = mc.getWindow().getHeight();

        RenderSystem.setProjectionMatrix(matrix.getBuffer(width, height), ProjectionType.ORTHOGRAPHIC);
    }

    public static void loadShaderLibraries() {

        ShaderLibrariesPlugin.registerShaderLibraries(
                BThackShaderLibraries.MATRICES,
                BThackShaderLibraries.SHADER_COLOR
        );
    }

    public static void registerRenderCall(Runnable runnable) {
        queueCalls.add(runnable);
    }

    public static void invokeRenderCalls() {
        synchronized (queueCalls) {
            if (queueCalls.isEmpty()) return;

            List<Runnable> renderCalls = List.copyOf(queueCalls);
            queueCalls.clear();

            for (Runnable runnable : renderCalls)
                runnable.run();
        }
    }
}
