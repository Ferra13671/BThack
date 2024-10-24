package com.ferra13671.BThack.api.Utils.Grim;

import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public final class GrimUtils implements Mc {

    public static void sendPreActionGrimPackets(float rotYaw, float rotPitch) {
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), rotYaw, rotPitch, mc.player.isOnGround()));
    }

    public static void sendPostActionGrimPackets() {
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround()));
    }
}
