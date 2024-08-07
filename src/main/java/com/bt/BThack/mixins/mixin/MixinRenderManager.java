package com.bt.BThack.mixins.mixin;

import com.bt.BThack.impl.Events.OnUpdateWalkingPlayerEvent;
import com.bt.BThack.impl.Events.RenderEntityEvent;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderManager.class, priority = 114514)
public class MixinRenderManager {
    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    public void renderEntityPre(Entity entity, double x, double y, double z, float yaw, float partialTicks, boolean debug, CallbackInfo ci) {
        if (entity == null || !RenderEntityEvent.isRenderingEntities()) return;

        RenderEntityEvent eventAll = new RenderEntityEvent.All(entity, OnUpdateWalkingPlayerEvent.Phase.PRE);
        MinecraftForge.EVENT_BUS.post(eventAll);
        if (eventAll.isCanceled())
            ci.cancel();

        if (!(entity instanceof EntityLivingBase)) {
            RenderEntityEvent eventModel = new RenderEntityEvent.Model(entity, OnUpdateWalkingPlayerEvent.Phase.PRE);
            MinecraftForge.EVENT_BUS.post(eventModel);
        }
    }

    @Inject(method = "renderEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/Render;doRender(Lnet/minecraft/entity/Entity;DDDFF)V", shift = At.Shift.AFTER))
    public void renderEntityPeri(Entity entity, double x, double y, double z, float yaw, float partialTicks, boolean debug, CallbackInfo ci) {
        if (entity == null || !RenderEntityEvent.isRenderingEntities()) return;

        RenderEntityEvent event = new RenderEntityEvent.All(entity, OnUpdateWalkingPlayerEvent.Phase.PERI);
        MinecraftForge.EVENT_BUS.post(event);

        if (!(entity instanceof EntityLivingBase)) {
            RenderEntityEvent eventModel = new RenderEntityEvent.Model(entity, OnUpdateWalkingPlayerEvent.Phase.POST);
            MinecraftForge.EVENT_BUS.post(eventModel);
        }
    }

    @Inject(method = "renderEntity", at = @At("RETURN"))
    public void renderEntityPost(Entity entity, double x, double y, double z, float yaw, float partialTicks, boolean debug, CallbackInfo ci) {
        if (entity == null || !RenderEntityEvent.isRenderingEntities()) return;

        RenderEntityEvent event = new RenderEntityEvent.All(entity, OnUpdateWalkingPlayerEvent.Phase.POST);
        MinecraftForge.EVENT_BUS.post(event);
    }
}