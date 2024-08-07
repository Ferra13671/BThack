package com.bt.BThack.mixins.mixin;

import com.bt.BThack.impl.Events.PlayerTravelEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayer.class, priority = Integer.MAX_VALUE)
public abstract class MixinEntityPlayer extends EntityLivingBase {

    public MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travel(float strafe, float vertical, float forward, CallbackInfo info) {
        //noinspection ConstantConditions
        if (EntityPlayerSP.class.isAssignableFrom(this.getClass())) {
            PlayerTravelEvent event = new PlayerTravelEvent();
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isCanceled()) {
                move(MoverType.SELF, motionX, motionY, motionZ);
                info.cancel();
            }
        }
    }
}