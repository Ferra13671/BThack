package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Events.Events.PacketEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

public class XCarry extends Module {
    public XCarry() {
        super("XCarry",
                "lang.module.XCarry",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );
    }

    @EventSubscriber
    public void onSendPacket(PacketEvent.Send e) {
        if (e.getPacket() instanceof CloseHandledScreenC2SPacket)
        {
            e.setCancelled(true);
        }
    }
}
