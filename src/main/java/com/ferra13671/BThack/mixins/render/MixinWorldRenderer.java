package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Events.Events.Render.RenderWorldEvent;
import com.ferra13671.BThack.Events.MegaEvents.Event;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    @Final
    @Shadow
    private BufferBuilderStorage bufferBuilders;
    @Shadow private ClientWorld world;
    @Shadow private PostEffectProcessor transparencyPostProcessor;
    @Final
    @Shadow
    private MinecraftClient client;
    @Unique private boolean didRenderParticles;

    @Inject(method = "render", at = @At("HEAD"))
    private void beforeRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
        BThackRender.worldRenderContext.prepare((WorldRenderer) (Object) this, matrices, tickDelta, limitTime, renderBlockOutline, camera, gameRenderer, lightmapTextureManager, matrix4f, bufferBuilders.getEntityVertexConsumers(), world.getProfiler(), transparencyPostProcessor != null, world);
        BThack.EVENT_BUS.activate(new RenderWorldEvent.Start(BThackRender.worldRenderContext));
        didRenderParticles = false;
    }

    @Inject(method = "setupTerrain", at = @At("RETURN"))
    private void afterTerrainSetup(Camera camera, Frustum frustum, boolean hasForcedFrustum, boolean spectator, CallbackInfo ci) {
        BThackRender.worldRenderContext.setFrustum(frustum);
        BThack.EVENT_BUS.activate(new RenderWorldEvent.AfterSetup(BThackRender.worldRenderContext));
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/util/math/MatrixStack;DDDLorg/joml/Matrix4f;)V", ordinal = 2, shift = At.Shift.AFTER))
    private void afterTerrainSolid(CallbackInfo ci) {
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        BThack.EVENT_BUS.activate(new RenderWorldEvent.BeforeEntities(BThackRender.worldRenderContext));
    }

    @Inject(method = "render", at = @At(value = "CONSTANT", args = "stringValue=blockentities", ordinal = 0))
    private void afterEntities(CallbackInfo ci) {
        BThack.EVENT_BUS.activate(new RenderWorldEvent.AfterEntities(BThackRender.worldRenderContext));
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/MinecraftClient;crosshairTarget:Lnet/minecraft/util/hit/HitResult;",
                    shift = At.Shift.AFTER,
                    ordinal = 1
            )
    )
    private void beforeRenderOutline(CallbackInfo ci) {
        Event event = new RenderWorldEvent.BeforeBlockOutline(BThackRender.worldRenderContext, client.crosshairTarget);
        BThack.EVENT_BUS.activate(event);

        BThackRender.worldRenderContext.renderBlockOutline = !event.isCancelled();
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
    private void onDrawBlockOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if (!BThackRender.worldRenderContext.renderBlockOutline) {
            // Was cancelled before we got here, so do not
            // fire the BLOCK_OUTLINE event per contract of the API.
            ci.cancel();
        } else {
            BThackRender.worldRenderContext.prepareBlockOutline(entity, cameraX, cameraY, cameraZ, blockPos, blockState);

            Event event = new RenderWorldEvent.BlockOutline(BThackRender.worldRenderContext, BThackRender.worldRenderContext);
            BThack.EVENT_BUS.activate(event);

            if (event.isCancelled()) {
                ci.cancel();
            }

            // The immediate mode VertexConsumers use a shared buffer, so we have to make sure that the immediate mode VCP
            // can accept block outline lines rendered to the existing vertexConsumer by the vanilla block overlay.
            BThackRender.worldRenderContext.consumers().getBuffer(RenderLayer.getLines());
        }
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/debug/DebugRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;DDD)V",
                    ordinal = 0
            )
    )
    private void beforeDebugRender(CallbackInfo ci) {
        BThack.EVENT_BUS.activate(new RenderWorldEvent.BeforeDebugRender(BThackRender.worldRenderContext));
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/particle/ParticleManager;renderParticles(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/client/render/Camera;F)V"
            )
    )
    private void onRenderParticles(CallbackInfo ci) {
        // set a flag so we know the next pushMatrix call is after particles
        didRenderParticles = true;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"))
    private void beforeClouds(CallbackInfo ci) {
        if (didRenderParticles) {
            didRenderParticles = false;
            BThack.EVENT_BUS.activate(new RenderWorldEvent.AfterTranslucent(BThackRender.worldRenderContext));
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderChunkDebugInfo(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/Camera;)V"))
    private void onChunkDebugRender(CallbackInfo ci) {
        BThack.EVENT_BUS.activate(new RenderWorldEvent.Last(BThackRender.worldRenderContext));
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void afterRender(CallbackInfo ci) {
        MinecraftClient.getInstance().getProfiler().push("oyvey-render-3d");
        RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
        BThack.EVENT_BUS.activate(new RenderWorldEvent.End(BThackRender.worldRenderContext));
        MinecraftClient.getInstance().getProfiler().pop();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    public void modifyRenderWeather(LightmapTextureManager manager, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (Client.getModuleByName("NoWeather").isEnabled())
            ci.cancel();
    }

    @Inject(method = "tickRainSplashing", at = @At("HEAD"), cancellable = true)
    public void modifyTickRainSplashing(Camera camera, CallbackInfo ci) {
        if (Client.getModuleByName("NoWeather").isEnabled())
            ci.cancel();
    }
}
