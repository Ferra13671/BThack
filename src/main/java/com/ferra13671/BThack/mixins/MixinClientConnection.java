package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Events.Events.DisconnectEvent;
import com.ferra13671.BThack.Events.Events.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientConnection.class, priority = Integer.MAX_VALUE)
public class MixinClientConnection {

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void modifyChannelRend0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        PacketEvent packetEvent = new PacketEvent.Receive(packet);
        BThack.EVENT_BUS.activate(packetEvent);
        if (packetEvent.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "sendImmediately", at = @At("HEAD"), cancellable = true)
    public void modifySendImmediately(Packet<?> packet, PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
        PacketEvent packetEvent = new PacketEvent.Send(packet);
        BThack.EVENT_BUS.activate(packetEvent);
        if (packetEvent.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "sendImmediately", at = @At("RETURN"), cancellable = true)
    public void modifySendPacketPost(Packet<?> packet, PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
        PacketEvent event = new PacketEvent.Send.Post(packet);
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "disconnect", at = @At(value = "HEAD"))
    private void hookDisconnect(Text disconnectReason, CallbackInfo ci) {
        BThack.EVENT_BUS.activate(new DisconnectEvent());
    }
}
