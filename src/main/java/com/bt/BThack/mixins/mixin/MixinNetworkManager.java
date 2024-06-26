package com.bt.BThack.mixins.mixin;

import com.bt.BThack.Client;
import com.bt.BThack.impl.Events.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetworkManager.class})
public class MixinNetworkManager {
   @Inject(
      method = {"sendPacket(Lnet/minecraft/network/Packet;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSendPacket(Packet packet, CallbackInfo callbackInfo) {
      PacketEvent packetEvent = new PacketEvent.Send(packet);
      MinecraftForge.EVENT_BUS.post(packetEvent);
      if (packetEvent.isCanceled()) {
         callbackInfo.cancel();
      }

   }

   @Inject(
      method = {"channelRead0"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onChannelRead(ChannelHandlerContext channelHandlerContext, Packet packet, CallbackInfo callbackInfo) {
      PacketEvent packetEvent = new PacketEvent.Receive(packet);
      MinecraftForge.EVENT_BUS.post(packetEvent);
      if (packetEvent.isCanceled()) {
         callbackInfo.cancel();
      }

   }

   @Inject(
      method = {"exceptionCaught"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_, CallbackInfo callbackInfo) {
      if (p_exceptionCaught_2_ instanceof IOException && Client.getModuleByName("AntiPacketKick").isEnabled()) {
         callbackInfo.cancel();
      }

   }
}
