package com.ferra13671.BThack.api.Managers;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.mixins.accessor.IClientWorld;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.network.packet.Packet;

public class NetworkManager implements Mc {

    public void sendSequencePacket(SequencedPacketCreator packetCreator) {
        if (mc.getNetworkHandler() == null || mc.world == null) return;
        try (PendingUpdateManager pendingUpdateManager = ((IClientWorld) mc.world).getPendingManager().incrementSequence()){
            int i = pendingUpdateManager.getSequence();
            mc.getNetworkHandler().sendPacket(packetCreator.predict(i));
        }
    }

    public void setDefaultPacket(Packet<?> packet) {
        mc.getNetworkHandler().sendPacket(packet);
    }
}
