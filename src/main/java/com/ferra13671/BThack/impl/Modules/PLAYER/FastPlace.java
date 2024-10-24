package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.IMinecraftClient;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.BlockItem;

import java.util.Arrays;

public class FastPlace extends Module {

    public static ModeSetting mode;

    public FastPlace() {
        super("FastPlace",
                "lang.module.FastPlace",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("Normal", "Ultra"));

        initSettings(
                mode
        );
    }

    @EventSubscriber
    public void onTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.getMainHandStack().getItem() instanceof BlockItem) {
            switch (mode.getValue()) {
                case "Normal" -> mc.itemUseCooldown = 0;
                case "Ultra" -> {
                    if (mc.options.useKey.isPressed()) {
                        for (int i = 0; i < 30; i++) {
                            ((IMinecraftClient) mc).useItem();
                            mc.itemUseCooldown = 0;
                        }
                    }
                }
            }
        }
    }
}
