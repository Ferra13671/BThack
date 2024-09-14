package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Events.Events.Render.RenderHudElementEvent;
import com.ferra13671.BThack.Events.Events.Render.RenderHudPostEvent;
import com.ferra13671.BThack.Events.Events.Render.RenderPortalEvent;
import com.ferra13671.BThack.Events.Events.Render.RenderSpyglassEvent;
import com.ferra13671.BThack.Events.MegaEvents.Event;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.JumpingMount;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {
	@Shadow protected abstract void renderVignetteOverlay(DrawContext context, @Nullable Entity entity);

	@Shadow protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);

	@Shadow protected abstract void renderSpyglassOverlay(DrawContext context, float scale);

	@Shadow protected abstract void renderPortalOverlay(DrawContext context, float nauseaStrength);

	@Shadow protected abstract void renderHotbar(float tickDelta, DrawContext context);

	@Shadow protected abstract void renderScoreboardSidebar(DrawContext context, ScoreboardObjective objective);

	@Inject(method = "render", at = @At("RETURN"))
	public void render(DrawContext drawContext, float tickDelta, CallbackInfo callbackInfo) {
		RenderSystem.setShaderColor(1, 1, 1, 1);

		RenderSystem.disableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		RenderSystem.disableCull();
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		BThack.EVENT_BUS.activate(new RenderHudPostEvent(drawContext, tickDelta));

		RenderSystem.enableDepthTest();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	//Vignette Render
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderVignetteOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/Entity;)V", ordinal = 0))
	public void modifyRenderVignetteOverlay(InGameHud instance, DrawContext context, Entity entity) {
		Event event = new RenderHudElementEvent(instance, RenderHudElementEvent.ElementType.VIGNETTE);
		BThack.EVENT_BUS.activate(event);
		if (!event.isCancelled())
			this.renderVignetteOverlay(context, entity);
	}

	//Spyglass Render
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderSpyglassOverlay(Lnet/minecraft/client/gui/DrawContext;F)V", ordinal = 0))
	public void modifyRenderSpyglassOverlay(InGameHud instance, DrawContext context, float scale) {
		Event event = new RenderHudElementEvent(instance, RenderHudElementEvent.ElementType.SPYGLASS);
		BThack.EVENT_BUS.activate(event);
		if (!event.isCancelled()) {
			RenderSpyglassEvent e = new RenderSpyglassEvent(scale);
			BThack.EVENT_BUS.activate(e);
			if (!e.isCancelled())
				this.renderSpyglassOverlay(context, e.scale);
		}
	}

	//Pumpkin Render
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V", ordinal = 0))
	public void modifyRenderPumpkinBlurOverlay(InGameHud instance, DrawContext context, Identifier texture, float opacity) {
		Event event = new RenderHudElementEvent(instance, RenderHudElementEvent.ElementType.PUMPKIN);
		BThack.EVENT_BUS.activate(event);
		if (!event.isCancelled())
			this.renderOverlay(context, texture, opacity);
	}

	//Portal Render
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderPortalOverlay(Lnet/minecraft/client/gui/DrawContext;F)V", ordinal = 0))
	public void modifyRenderPortalOverlay(InGameHud instance, DrawContext context, float nauseaStrength) {
		Event event = new RenderHudElementEvent(instance, RenderHudElementEvent.ElementType.PORTAL);
		BThack.EVENT_BUS.activate(event);
		if (!event.isCancelled()) {
			RenderPortalEvent e = new RenderPortalEvent(nauseaStrength);
			if (!e.isCancelled())
				this.renderPortalOverlay(context, e.nauseaStrength);
		}
	}

	//Hotbar Render
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/gui/DrawContext;)V", ordinal = 0))
	public void modifyRenderHotbar(InGameHud instance, float tickDelta, DrawContext context) {
		Event event = new RenderHudElementEvent(instance, RenderHudElementEvent.ElementType.HOTBAR);
		BThack.EVENT_BUS.activate(event);
		if (!event.isCancelled())
			this.renderHotbar(tickDelta, context);
	}

	//Health Render
	@Inject(method = "renderMountHealth", at = @At("HEAD"), cancellable = true)
	public void modifyRenderMountHealth(DrawContext context, CallbackInfo ci) {
		Event event = new RenderHudElementEvent(null, RenderHudElementEvent.ElementType.HEALTH);
		BThack.EVENT_BUS.activate(event);
		if (event.isCancelled())
			ci.cancel();
	}

	//JumpBar Render
	@Inject(method = "renderMountJumpBar", at = @At("HEAD"), cancellable = true)
	public void modifyRenderMountJumpBar(JumpingMount mount, DrawContext context, int x, CallbackInfo ci) {
		Event event = new RenderHudElementEvent(null, RenderHudElementEvent.ElementType.JUMPBAR);
		BThack.EVENT_BUS.activate(event);
		if (event.isCancelled())
			ci.cancel();
	}

	//Experience Render
	@Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
	public void modifyRenderExperienceBar(DrawContext context, int x, CallbackInfo ci) {
		Event event = new RenderHudElementEvent(null, RenderHudElementEvent.ElementType.EXPERIENCE);
		BThack.EVENT_BUS.activate(event);
		if (event.isCancelled())
			ci.cancel();
	}

	//Item Render
	@Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
	public void modifyRenderHeldItemTooltip(DrawContext context, CallbackInfo ci) {
		Event event = new RenderHudElementEvent(null, RenderHudElementEvent.ElementType.ITEM);
		BThack.EVENT_BUS.activate(event);
		if (event.isCancelled())
			ci.cancel();
	}

	//StatusEffect Render
	@Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
	public void modifyRenderStatusEffectOverlay(DrawContext context, CallbackInfo ci) {
		Event event = new RenderHudElementEvent(null, RenderHudElementEvent.ElementType.STATUS_EFFECTS);
		BThack.EVENT_BUS.activate(event);
		if (event.isCancelled())
			ci.cancel();
	}

	//ScoreboardSidebar Render
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", ordinal = 0))
	public void modifyRenderScoreboardSidebar(InGameHud instance, DrawContext context, ScoreboardObjective objective) {
		Event event = new RenderHudElementEvent(instance, RenderHudElementEvent.ElementType.SCOREBOARD_SIDEBAR);
		BThack.EVENT_BUS.activate(event);
		if (!event.isCancelled())
			this.renderScoreboardSidebar(context, objective);
	}

	//Crosshair Render
	@Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
	public void modifyRenderCrosshair(DrawContext context, CallbackInfo ci) {
		Event event = new RenderHudElementEvent(null, RenderHudElementEvent.ElementType.CROSSHAIRS);
		BThack.EVENT_BUS.activate(event);
		if (event.isCancelled())
			ci.cancel();
	}
}