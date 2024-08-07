package com.bt.BThack.mixins.mixin;

import com.bt.BThack.System.Client;
import com.bt.BThack.api.Module.Module;
import com.bt.BThack.api.Utils.Interfaces.Mc;
import com.bt.BThack.impl.Module.MOVEMENT.NewElytraFlight.NewElytraFlight;
import com.bt.BThack.impl.Module.RENDER.Chams.Chams;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer implements Mc {



    @Inject(method = "applyRotations(Lnet/minecraft/client/entity/AbstractClientPlayer;FFF)V", at = @At("RETURN"))
    protected void applyRotations(AbstractClientPlayer entityLiving, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci) {
        if (entityLiving == mc.player && (Client.getModuleByName("ElytraFlight").isEnabled() && NewElytraFlight.isFlying && !Module.getCheckbox("ElytraFlight", "Auto Landing"))) {
            Vec3d vec3d = entityLiving.getLook(partialTicks);
            double d0 = entityLiving.motionX * entityLiving.motionX + entityLiving.motionZ * entityLiving.motionZ;
            double d1 = vec3d.x * vec3d.x + vec3d.z * vec3d.z;

            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (entityLiving.motionX * vec3d.x + entityLiving.motionZ * vec3d.z) / (Math.sqrt(d0) * Math.sqrt(d1));
                double d3 = entityLiving.motionX * vec3d.z - entityLiving.motionZ * vec3d.x;
                GlStateManager.rotate(-((float) (Math.signum(d3) * Math.acos(d2)) * 180.0F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
            }
        }
    }

    @Inject(method = "renderLeftArm", at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;swingProgress:F", opcode = 181), cancellable = true)
    public void renderLeftArmPre(AbstractClientPlayer abstractClientPlayer, CallbackInfo callbackInfo) {
        if (abstractClientPlayer == mc.player && Client.getModuleByName("Chams").isEnabled() && Module.getCheckbox("Chams", "Left Arm")) {
            try {
                Chams.chamsRender.renderLeftArmPre(abstractClientPlayer, callbackInfo);
            } catch (Exception ignored) {}
        }
    }

    @Inject(method = "renderLeftArm", at = @At(value = "RETURN"), cancellable = true)
    public void renderLeftArmPost(AbstractClientPlayer abstractClientPlayer, CallbackInfo callbackInfo) {
        if (abstractClientPlayer == mc.player && Client.getModuleByName("Chams").isEnabled() && Module.getCheckbox("Chams", "Left Arm")) {
            Chams.chamsRender.renderLeftArmPost(abstractClientPlayer, callbackInfo);
        }
    }

    @Inject(method = "renderRightArm", at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;swingProgress:F", opcode = 181), cancellable = true)
    public void renderRightArmPre(AbstractClientPlayer abstractClientPlayer, CallbackInfo callbackInfo) {
        if (abstractClientPlayer == mc.player && Client.getModuleByName("Chams").isEnabled() && Module.getCheckbox("Chams", "Right Arm")) {
            try {
                Chams.chamsRender.renderRightArmPre(abstractClientPlayer, callbackInfo);
            } catch (Exception ignored) {}
        }
    }

    @Inject(method = "renderRightArm", at = @At(value = "RETURN"), cancellable = true)
    public void renderRightArmPost(AbstractClientPlayer abstractClientPlayer, CallbackInfo callbackInfo) {
        if (abstractClientPlayer == mc.player && Client.getModuleByName("Chams").isEnabled() && Module.getCheckbox("Chams", "Right Arm")) {
            Chams.chamsRender.renderRightArmPost(abstractClientPlayer, callbackInfo);
        }
    }
}
