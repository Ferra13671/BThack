package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Events.PacketEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.IPlayerPositionLookS2CPacket;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class NoSRotations extends Module {
    public NoSRotations() {
        super("NoSRotations",
                "lang.module.NoSRotations",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );
    }

    @EventSubscriber
    public void onPacket(PacketEvent.Receive e) {
        if (nullCheck()) return;
        if (e.getPacket() instanceof PlayerPositionLookS2CPacket packet) {
            ((IPlayerPositionLookS2CPacket) packet).setYaw(0);
            ((IPlayerPositionLookS2CPacket) packet).setPitch(0);
        }
    }
}
