package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Events.Events.PacketEvent;
import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;

import java.util.ArrayList;
import java.util.Arrays;

public class NoSwing extends Module {

    public static ModeSetting mode;

    public NoSwing() {
        super("NoSwing",
                "lang.module.NoSwing",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );

        mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Client", "Server")));

        initSettings(
                mode
        );
    }

    @EventSubscriber
    public void onPacket(PacketEvent.Send e) {
        if (mode.getValue().equals("Server")  && e.getPacket() instanceof HandSwingC2SPacket) {
            e.setCancelled(true);
        }
    }

    @EventSubscriber
    public void onClientTick(TickEvent.ClientTickEvent e) {
        mc.player.handSwinging = false;
        mc.player.handSwingTicks = 0;
        mc.player.handSwingProgress = 0.0f;
        mc.player.lastHandSwingProgress = 0.0f;
    }
}