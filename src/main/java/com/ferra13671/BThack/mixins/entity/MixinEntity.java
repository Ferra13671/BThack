package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Events.Entity.SetVelocityEvent;
import com.ferra13671.BThack.Events.Player.ChangePlayerLookEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Modules.NoRotateMathUtils;
import com.ferra13671.BThack.impl.Modules.MOVEMENT.NoRotate;
import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class MixinEntity implements Mc {


    @Shadow public Vec3d velocity;

    @Shadow public abstract Text getDisplayName();

    @Shadow public float yaw;

    @Shadow protected UUID uuid;

    @Shadow protected abstract void setFlag(int index, boolean value);

    @Inject(method = "setVelocity(DDD)V", at = @At("HEAD"), cancellable = true)
    public void modifySetVelocity(double x, double y, double z, CallbackInfo ci){
        if ((Object) this != mc.player) return;
        SetVelocityEvent event = new SetVelocityEvent(new Vec3d(x, y, z));

        BThack.EVENT_BUS.activate(event);

        if (!event.isCancelled()) {
            this.velocity = event.getVelocity();
        }
        ci.cancel();
    }

    @Inject(method = "setVelocity(Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), cancellable = true)
    public void modifySetVelocityVec3d(Vec3d velocity, CallbackInfo ci) {
        if ((Object) this != mc.player) return;
        SetVelocityEvent event = new SetVelocityEvent(velocity);

        BThack.EVENT_BUS.activate(event);

        if (!event.isCancelled()) {
            this.velocity = event.getVelocity();
        }
        ci.cancel();
    }

    @Inject(method = "setYaw", at = @At("HEAD"), cancellable = true)
    public void modifySetYaw(float yaw, CallbackInfo ci) {
        if (mc.player == null || mc.world == null) return;
        if (Client.getModuleByName("NoRotate").isEnabled() && this.getDisplayName().getString().equals(mc.player.getDisplayName().getString())) {
            this.yaw = NoRotateMathUtils.rotateYawMath(mc.player);
            ci.cancel();
        }
    }

    @Inject(method = "setPitch", at = @At("HEAD"), cancellable = true)
    public void modifySetPitch(float pitch, CallbackInfo ci) {
        if (mc.player == null || mc.world == null) return;
        if (Client.getModuleByName("NoRotate").isEnabled() && this.getDisplayName().getString().equals(mc.player.getDisplayName().getString())) {
            if (NoRotate.blockPitch.getValue()) {
                mc.player.pitch = NoRotateMathUtils.RotatePitchMath(mc.player);
                ci.cancel();
            }
        }
    }

    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    public void modifyChangeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        Event event = new ChangePlayerLookEvent(cursorDeltaX, cursorDeltaY);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "setFlag", at = @At("HEAD"), cancellable = true)
    public void modifySetFlag(int index, boolean value, CallbackInfo ci) {
        if (mc.player == null || mc.world == null) return;
        if (this.uuid == mc.player.getUuid()) {
            if (Client.getModuleByName("ElytraFlight").isEnabled()) {
                if (index == 7 && !value) {
                    this.setFlag(7, true);
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                    ci.cancel();
                }
            }
        }
    }
}
