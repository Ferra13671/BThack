package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Events.Events.PacketEvent;
import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.Destroy.DestroyThread3D;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Arrays;

public class Sprint extends Module {

    public static ModeSetting mode;

    public Sprint() {
        super("Sprint",
                "lang.module.Sprint",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );

        mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Standard", "Legal")));

        initSettings(
                mode
        );
    }

    @EventSubscriber
    public void onPlayerTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        String _mode = mode.getValue();

        arrayListInfo = _mode;

        switch (_mode) {
            case "Standard":
                if (!mc.player.isSprinting()) {
                    try {
                        if (needSprint()) {
                            mc.player.setSprinting(true);
                        }
                    } catch (Exception ignored) {}
                }
                break;
            case "Legal":
                mc.options.sprintKey.setPressed(!mc.player.isFallFlying());
                break;
        }
    }

    public static boolean needSprint() {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.player == null || mc.world == null) return false;

        return
                !mc.options.sneakKey.isPressed()
                && !mc.player.isFallFlying()
                && mc.player.getHungerManager().getFoodLevel() > 6
                && isMoving()
                && !mc.player.getAbilities().flying;
    }

    public static boolean isMoving() {
        MinecraftClient mc = MinecraftClient.getInstance();

        return
                !mc.player.isSneaking()
                && mc.player.input.movementForward > 0
                        && !mc.player.horizontalCollision;
    }
}
