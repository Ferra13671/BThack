package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BTbot.impl.AntiAFK.Start.StartAntiAFK;
import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.util.Formatting;

public class AntiAFK extends Module {

    private static boolean correct = false;

    public static NumberSetting walkRadius;
    public static NumberSetting delay;
    public static NumberSetting messageSize;

    public AntiAFK() {
        super("AntiAFK",
                "lang.module.AntiAFK",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );

        walkRadius = new NumberSetting("Walking radius", this, 3.0,1,10,false);
        delay = new NumberSetting("Delay(Seconds)", this, 3.0,0.5,10,false);
        messageSize = new NumberSetting("Message size", this, 15,5,30,true);

        initSettings(
                walkRadius,
                delay,
                messageSize
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        if (!StartAntiAFK.active) {
            super.onEnable();
            correct = true;
            StartAntiAFK.start();
        } else {
            ChatUtils.sendMessage(Formatting.YELLOW + "AntiAFK is already enabled. Please turn it off before using the module.");
            correct = false;
            setToggled(false);
        }
    }

    @EventSubscriber
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (correct) {
            StartAntiAFK.walkRadius = walkRadius.getValue();
            StartAntiAFK.messageSize = (int) messageSize.getValue();
            StartAntiAFK.delay = delay.getValue();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (nullCheck()) return;
        if (correct) {
            if (StartAntiAFK.active) {
                StartAntiAFK.stop();
            }
        }
    }
}
