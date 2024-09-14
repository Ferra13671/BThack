package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Events.Events.Render.RenderOthrerEvent;
import com.ferra13671.BThack.Events.MegaEvents.Event;
import com.ferra13671.BThack.Events.MegaEvents.EventModifiers.EventPhase;
import com.ferra13671.BThack.impl.Modules.RENDER.NoOverlay;
import com.ferra13671.BThack.impl.Modules.RENDER.NoRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Shadow protected abstract void renderNausea(DrawContext context, float distortionStrength);

    @Shadow @Final MinecraftClient client;

    //Hurt Render
    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    public void modifyTiltViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (Client.getModuleByName("NoOverlay").isEnabled() && NoOverlay.hurtCam.getValue())
            ci.cancel();
    }

    //Nausea Render
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;renderNausea(Lnet/minecraft/client/gui/DrawContext;F)V", ordinal = 0))
    public void modifyRenderNausea(GameRenderer instance, DrawContext context, float distortionStrength) {
        Event event = new RenderOthrerEvent(RenderOthrerEvent.OtherElement.NAUSEA);
        BThack.EVENT_BUS.activate(event);
        if (!event.isCancelled())
            this.renderNausea(context, distortionStrength);
    }

    //Overlay Render
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Overlay;render(Lnet/minecraft/client/gui/DrawContext;IIF)V", ordinal = 0))
    public void modifyRenderOverlay(Overlay instance, DrawContext drawContext, int i, int j, float v) {
        Event event = new RenderOthrerEvent(RenderOthrerEvent.OtherElement.OVERLAY, EventPhase.PRE);
        BThack.EVENT_BUS.activate(event);
        if (!event.isCancelled()) {
            this.client.getOverlay().render(drawContext, i, j, this.client.getLastFrameDuration());
            event = new RenderOthrerEvent(RenderOthrerEvent.OtherElement.OVERLAY, EventPhase.POST);
            BThack.EVENT_BUS.activate(event);
        }
    }

    //Screen Render
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/gui/DrawContext;IIF)V", ordinal = 0))
    public void modifyRenderScreen(Screen instance, DrawContext context, int mouseX, int mouseY, float delta) {
        Event event = new RenderOthrerEvent(RenderOthrerEvent.OtherElement.SCREEN, EventPhase.PRE);
        BThack.EVENT_BUS.activate(event);
        if (!event.isCancelled()) {
            this.client.currentScreen.renderWithTooltip(context, mouseX, mouseY, this.client.getLastFrameDuration());
            event = new RenderOthrerEvent(RenderOthrerEvent.OtherElement.SCREEN, EventPhase.POST);
            BThack.EVENT_BUS.activate(event);
        }
    }

    //Overlays Render
    @Redirect(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameOverlayRenderer;renderOverlays(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;)V", ordinal = 0))
    public void modifyRenderOverlays(MinecraftClient client, MatrixStack matrices) {
        if (Client.getModuleByName("NoRender").isEnabled() && NoRender.overlay.getValue()) {
        } else
            InGameOverlayRenderer.renderOverlays(this.client, matrices);
    }
}
