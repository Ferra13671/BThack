package com.bt.BThack.impl.Module.OTHER.EnemyRadar;

import com.bt.BThack.api.Module.Module;
import com.bt.BThack.api.Utils.ChatUtils;
import com.bt.BThack.api.Utils.Interfaces.Mc;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.server.SPacketDisconnect;

public class EnemyRadarDisconnectThread extends Thread implements Mc {
    public void run() {
        int delay = (int)Module.getSlider("EnemyRadar", "Shutdown Delay");

        ChatUtils.sendMessage(ChatFormatting.RED + "The countdown to disconnecting begins!");

        while (delay != 0) {
            ChatUtils.sendMessage(ChatFormatting.RED + "Disconnect after " + delay + "s.");

            try {
                sleep(1000);
            } catch (InterruptedException ignored) {}

            delay--;
        }

        mc.player.connection.handleDisconnect(new SPacketDisconnect());

        EnemyRadar.pause = false;
    }
}
