package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.SoundSystem.SoundSystem;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.ToggleSound;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ferra13671 and Nikitadan4pi
 */

public class PearlPhase extends Module {

    public static ModeSetting mode;

    public static BooleanSetting autoToggle;
    public static NumberSetting phaseDelay;

    public static BooleanSetting swingHand;
    public static NumberSetting phasePitch;

    public PearlPhase() {

        super("PearlPhase",
                "lang.module.PearlPhase",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("One", "Always"));

        autoToggle = new BooleanSetting("Auto Toggle", this, false, () -> mode.getValue().equals("Always"));
        phaseDelay = new NumberSetting("Phase Delay", this, 100, 100, 500, true, () -> mode.getValue().equals("Always"));

        swingHand = new BooleanSetting("Swing Hand", this, true);
        phasePitch = new NumberSetting("Pitch", this, 85, 0, 180, true);

        initSettings(
                mode,

                autoToggle,
                phaseDelay,

                swingHand,
                phasePitch
        );
    }

    private final List<Vec3i> phasePoses = Arrays.asList(
            new Vec3i(1, 0, 0),
            new Vec3i(1, 0, 1),
            new Vec3i(1, 0, -1),
            new Vec3i(-1, 0, 0),
            new Vec3i(-1, 0, 1),
            new Vec3i(-1, 0, -1),
            new Vec3i(0, 0, 1),
            new Vec3i(0, 0, -1)
    );
    private final Ticker ticker = new Ticker();

    @Override
    public void playOffSound() {
        if (mode.getValue().equals("Always"))
            if (ModuleList.toggleSound.isEnabled())
                SoundSystem.playSound(Sounds.MODULE_OFF, (float) ToggleSound.volume.getValue());
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }
        ticker.reset();

        if (mode.getValue().equals("One")) {
            pearlPhaseAction(mc.player.getYaw());
            toggle();
        } else
            super.onEnable();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.horizontalCollision) {
            if (ticker.passed(phaseDelay.getValue())) {
                BlockPos pos = new ModifyBlockPos(mc.player.getX(), mc.player.getY(), mc.player.getZ());
                if (!BlockUtils.isOpaqueFullCube(pos) || mc.world.isAir(pos)) {
                    double minLength = 1000;
                    BlockPos minPos = null;
                    for (Vec3i vec : phasePoses) {
                        BlockPos tempPos = pos.add(vec);
                        double tempLength = MathUtils.getDistance(mc.player.getPos(), tempPos.toCenterPos());
                        if (tempLength < minLength) {
                            minLength = tempLength;
                            minPos = tempPos;
                        }
                    }
                    if (minPos != null) {
                        pearlPhaseAction(AimBotUtils.rotations(minPos)[0]);
                        if (autoToggle.getValue())
                            setToggled(false);
                        ticker.reset();
                    }
                }
            }
        } else
            ticker.reset();
    }

    public void pearlPhaseAction(float yaw) {
        GrimUtils.sendPreActionGrimPackets(yaw, (float) phasePitch.getValue());

        ItemUtils.useItem(Items.ENDER_PEARL, swingHand.getValue());

        GrimUtils.sendPostActionGrimPackets();
    }

}
