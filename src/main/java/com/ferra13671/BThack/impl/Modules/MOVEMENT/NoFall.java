package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Events.Events.PacketEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {

    public NoFall() {
        super("NoFall",
                "lang.module.NoFall",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );
    }

    boolean stop;


    @EventSubscriber
    public void onPacketSend(PacketEvent.Send e) {
        if (e.getPacket() instanceof PlayerMoveC2SPacket packet) {
            packet.onGround = true;
        }
    }
}
