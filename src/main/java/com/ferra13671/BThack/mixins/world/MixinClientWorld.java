package com.ferra13671.BThack.mixins.world;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Events.SoundPlayEvent;
import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.impl.Modules.WORLD.CloudsColor;
import com.ferra13671.BThack.impl.Modules.WORLD.CustomDayTime;
import com.ferra13671.BThack.impl.Modules.WORLD.SkyColor;
import com.ferra13671.BThack.impl.Modules.WORLD.WorldElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld extends World {

    @Shadow @Final private MinecraftClient client;

    @Shadow @Final private ClientWorld.Properties clientWorldProperties;

    protected MixinClientWorld(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(method = "getCloudsColor", at = @At("HEAD"), cancellable = true)
    public void getCloudColor(float p_getCloudColour_1_, CallbackInfoReturnable<Vec3d> cir) {
        if (Client.getModuleByName("CloudsColor").isEnabled()) {
            cir.setReturnValue(new Vec3d(CloudsColor.cloudsRed.getValue() / 255, CloudsColor.cloudsGreen.getValue() / 255, CloudsColor.cloudsBlue.getValue() / 255));
        }
    }

    @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
    public void getSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        if (Client.getModuleByName("SkyColour").isEnabled()) {
            cir.cancel();
            cir.setReturnValue(new Vec3d(SkyColor.skyRed.getValue() / 255.0f, SkyColor.skyGreen.getValue() / 255.0f, SkyColor.skyBlue.getValue() / 255.0f));
        }
    }

    @Inject(method = "method_23787", at = @At("HEAD"), cancellable = true)
    public void modifyStarBrightness(float f, CallbackInfoReturnable<Float> cir) {
        if (Client.getModuleByName("WorldElements").isEnabled()) {
            cir.setReturnValue((float) WorldElements.starBrightness.getValue());
        }
    }

    @Inject(method = "playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZJ)V", at = @At("HEAD"), cancellable = true)
    public void modifyPlaySound(double x, double y, double z, SoundEvent soundEvent, SoundCategory soundCategory, float volume, float pitch, boolean useDistance, long seed, CallbackInfo ci) {
        SoundPlayEvent event = new SoundPlayEvent(x, y, z, soundEvent, soundCategory, volume, pitch, useDistance);
        BThack.EVENT_BUS.activate(event);
        ci.cancel();
        if (event.isCancelled())
            return;
        double d = this.client.gameRenderer.getCamera().getPos().squaredDistanceTo(event.x, event.y, event.z);
        PositionedSoundInstance positionedSoundInstance = new PositionedSoundInstance(event.soundEvent, event.soundCategory, event.volume, event.pitch, Random.create(seed), event.x, event.y, event.z);
        if (event.useDistance && d > 100.0) {
            double e = Math.sqrt(d) / 40.0;
            this.client.getSoundManager().play(positionedSoundInstance, (int)(e * 20.0));
        } else {
            this.client.getSoundManager().play(positionedSoundInstance);
        }
    }

    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void startWorldTick(CallbackInfo ci) {
        BThack.EVENT_BUS.activate(new TickEvent.WorldTickEvent());
    }

    @Inject(method = "setTimeOfDay", at = @At("HEAD"), cancellable = true)
    public void modifyGetTimeOfDay(long timeOfDay, CallbackInfo ci) {
        if (Client.getModuleByName("CustomDayTime").isEnabled()) {
            ci.cancel();
            if (CustomDayTime.time < 0L) {
                CustomDayTime.time = -CustomDayTime.time;
                getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false, null);
            } else {
                getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(true, null);
            }
            clientWorldProperties.setTimeOfDay(CustomDayTime.time);
        }
    }
}
