package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Events.Events.PacketEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;

public class NoServerSlot extends Module {

    public NoServerSlot() {
        super("NoServerSlot",
                "lang.module.NoServerSlot",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof UpdateSelectedSlotS2CPacket) {
            event.cancel();
            mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
        }
    }
}
