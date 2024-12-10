package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import java.util.Arrays;

public class FastFall extends Module {

    public static ModeSetting mode;

    public static NumberSetting fallDistance;

    public static NumberSetting downSpeed;

    public static NumberSetting timerSpeed;

    public FastFall() {
        super("FastFall",
                "lang.module.FastFall",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("Timer", "Velocity", "Pos"));

        fallDistance = new NumberSetting("Fall Dist.", this, 1, 0.1, 5, false);

        downSpeed = new NumberSetting("Down Speed", this, 0.4, 0.35, 1.5, false, () -> mode.getValue().equals("Velocity"));

        timerSpeed = new NumberSetting("Timer Speed", this, 2, 1.1, 5, false, () -> mode.getValue().equals("Timer"));

        initSettings(
                mode,

                fallDistance,

                downSpeed,

                timerSpeed
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();

        ModuleList.elytraFlight.setToggled(false);
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (!mode.getValue().equals("Timer"))
            if (Managers.TICK_MANAGER.getTickModifier() != 1)
                Managers.TICK_MANAGER.applyTickModifier(1);

        switch (mode.getValue()) {
            case "Timer" -> timerAction();
            case "Velocity" -> velocityAction();
            case "Pos" -> posAction();
        }
    }

    public void timerAction() {
        if (ModuleList.timer.isEnabled())
            ModuleList.timer.setToggled(false);

        if (mc.player.verticalCollision)
            Managers.TICK_MANAGER.applyTickModifier(1);
        else
            if (mc.player.fallDistance >= fallDistance.getValue())
                Managers.TICK_MANAGER.applyTickModifierWithFactor(timerSpeed.getValue());
    }

    public void velocityAction() {
        if (mc.player.fallDistance >= fallDistance.getValue())
            mc.player.velocity.y = -downSpeed.getValue();
    }

    public void posAction() {
        if (mc.player.fallDistance >= fallDistance.getValue())
            mc.player.setPosition(mc.player.getX(), mc.player.getY() - mc.player.fallDistance, mc.player.getZ());

    }
}
