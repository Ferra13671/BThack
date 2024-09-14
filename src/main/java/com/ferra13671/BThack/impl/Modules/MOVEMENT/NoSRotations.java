package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Events.Events.PacketEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.IPlayerPositionLookS2CPacket;
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
        if (e.getPacket() instanceof PlayerPositionLookS2CPacket) {
            PlayerPositionLookS2CPacket packet = (PlayerPositionLookS2CPacket)e.getPacket();
            ((IPlayerPositionLookS2CPacket) packet).setYaw(mc.player.getYaw());
            ((IPlayerPositionLookS2CPacket) packet).setPitch(mc.player.getPitch());
        }
    }
}
