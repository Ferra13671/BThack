package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.impl.Modules.MOVEMENT.NoSlow;
import com.ferra13671.BThack.impl.Modules.MOVEMENT.SafeWalk;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    protected boolean clipAtLedge() {
        return super.clipAtLedge() || (Client.getModuleByName("SafeWalk").isEnabled() && !SafeWalk.mode.getValue().equals("Legit Shift"));
    }

    @Override
    protected Vec3d adjustMovementForSneaking(Vec3d movement, MovementType type) {
        Vec3d result = super.adjustMovementForSneaking(movement, type);

        if(movement != null) {
            ((SafeWalk) Client.getModuleByName("SafeWalk")).onClipAtLedge(!movement.equals(result));
        }

        return result;
    }

    @Redirect(method = "updateNausea", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;closeHandledScreen()V", ordinal = 0))
    public void modifyCloseHandledScreenOnUpdateNausea(ClientPlayerEntity instance) {
        if (!Client.getModuleByName("PortalGod").isEnabled())
            instance.closeHandledScreen();
    }

    @Redirect(method = "updateNausea", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 0))
    public void modifyCloseScreenOnUpdateNausea(MinecraftClient instance, Screen screen) {
        if (!Client.getModuleByName("PortalGod").isEnabled())
            instance.setScreen(screen);
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), require = 0)
    public boolean modifyIsUsingItemOnTickMovement(ClientPlayerEntity instance) {
        if (Client.isOptionActivated("NoSlow", NoSlow.useItems)) {
            return false;
        }
        return instance.isUsingItem();
    }
}
