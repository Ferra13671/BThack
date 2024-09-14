package com.ferra13671.BThack.impl.Modules.PLAYER;


import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.Text;

public class AutoDisconnect extends Module {

    public static NumberSetting minHealth;

    public AutoDisconnect() {
        super("AutoDisconnect",
                "lang.module.AutoDisconnect",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );

        minHealth = new NumberSetting("Min Health", this, 10,1,20,true);

        initSettings(
                minHealth
        );
    }

    @EventSubscriber
    public void onPlayerTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player != null) {
            float playerHP = mc.player.getHealth();
            double minHP = minHealth.getValue();
            if (playerHP <= minHP) {
                mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.of("Your health has reached its minimum limit(" + minHP + "). You have been disconnected.  Your HP: " + playerHP)));
            }
        }
    }
}