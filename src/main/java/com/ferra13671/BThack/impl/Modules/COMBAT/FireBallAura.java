package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.BThack.impl.Modules.COMBAT.KillAura.RotateMode;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.*;

public class FireBallAura extends Module {

    public static NumberSetting range;
    public static BooleanSetting packetRotate;

    public FireBallAura() {
        super("FireBallAura",
                "lang.module.FireBallAura",
                KeyboardUtils.RELEASE,
                Category.COMBAT,
                false
        );

        range = new NumberSetting("Range", this, 3, 2, 6, false);
        packetRotate = new BooleanSetting("Packet Rotate", this, true);

        initSettings(
                range,
                packetRotate
        );
    }

    private final Set<Entity> fireBalls = Sets.newHashSet();

    @EventSubscriber
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        arrayListInfo = "" + range.getValue();

        ArrayList<Entity> entities = new ArrayList<>();

        for (Entity entity : mc.world.getEntities()) {
            entities.add(entity);
        }

        Entity fireBall =entities.stream().filter(entity -> entity instanceof FireballEntity).min(Comparator.comparing(
                entity -> entity.distanceTo(mc.player))).filter(entity -> entity.distanceTo(mc.player) <= range.getValue()).orElse(null);

        if (fireBall != null) {
            if (!fireBalls.contains(fireBall)) {
                if (packetRotate.getValue()) {
                    float[] oldRot = new float[]{mc.player.yaw, mc.player.pitch};
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(AimBotUtils.rotations(fireBall)[0], AimBotUtils.rotations(fireBall)[1], mc.player.onGround));

                    KillAuraUtils.DelayAttack(fireBall, RotateMode.NONE, 0);
                    fireBalls.add(fireBall);

                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(oldRot[0], oldRot[1], mc.player.onGround));
                } else {
                    KillAuraUtils.DelayAttack(fireBall, RotateMode.NONE, 0);
                    fireBalls.add(fireBall);
                }
            }
        }

        fireBalls.removeIf(entity -> !entities.contains(entity));
    }
}
