package com.ferra13671.BThack.impl.Modules.COMBAT.KillAura;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class AuraThread extends Thread implements Mc {
    private int delay;

    public void run() {
        try {
            delay = (int) (KillAura.delay.getValue() * 1000);

            PlayerEntity target = KillAuraUtils.filterPlayers(KillAura.range, KillAura.friends, KillAura.teammates, KillAura.clanManager, KillAura.clanMode, KillAura.target);
            Entity entity = KillAuraUtils.filterEntity(KillAura.range.getValue());

            if (KillAura.players.getValue() && target != null && KillAuraUtils.canBeSeeTarget(KillAura.ignoreWalls, target)) {
                attack(target);
            }


            if (KillAura.mobs.getValue()) {
                if (entity != null) {
                    if (KillAuraUtils.canBeSeeTarget(KillAura.ignoreWalls, entity) && entity.isAlive() && KillAuraUtils.isCurrentMob(entity)) {
                        RotateMode rotateMode;
                        int packets = (int) KillAura.packets.getValue();
                        switch (KillAura.rotateMode.getValue()) {
                            case "Packet" -> rotateMode = RotateMode.PACKET;
                            case "Vanilla" -> rotateMode = RotateMode.VANILLA;
                            default -> rotateMode = RotateMode.NONE;
                        }
                        switch (KillAura.attackMode.getValue()) {
                            case "CoolDown":
                                if (KillAura.rotateMode.getValue().equals("Packet")) {
                                    if (mc.player.getAttackCooldownProgress(0) >= 1.0)
                                        AimBotUtils.packetRotateToEntity(entity);
                                }
                                KillAuraUtils.CoolDownAttack(entity, this, (int) KillAura.postCooldown.getValue(), rotateMode, packets);
                                break;
                            case "Delay":
                                if (KillAura.rotateMode.getValue().equals("Packet")) {
                                    AimBotUtils.packetRotateToEntity(entity);
                                }
                                KillAuraUtils.DelayAttack(entity, rotateMode, packets);
                                try {
                                    sleep(delay);
                                } catch (InterruptedException ignored) {}
                                break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    private void attack(PlayerEntity target) {
        if (target.getHealth() > 0) {
            RotateMode rotateMode;
            int packets = (int) KillAura.packets.getValue();
            switch (KillAura.rotateMode.getValue()) {
                case "Packet" -> rotateMode = RotateMode.PACKET;
                case "Vanilla" -> rotateMode = RotateMode.VANILLA;
                default -> rotateMode = RotateMode.NONE;
            }
            switch (KillAura.attackMode.getValue()) {
                case "CoolDown":
                    if (KillAura.rotateMode.getValue().equals("Packet")) {
                        if (mc.player.getAttackCooldownProgress(0) >= 1.0)
                            AimBotUtils.packetRotateToEntity(target);
                    }
                    KillAuraUtils.CoolDownAttack(target, this, (int) KillAura.postCooldown.getValue(), rotateMode, packets);
                    break;
                case "Delay":
                    if (KillAura.rotateMode.getValue().equals("Packet")) {
                        AimBotUtils.packetRotateToEntity(target);
                    }
                    KillAuraUtils.DelayAttack(target, rotateMode, packets);
                    try {
                        sleep(delay);
                    } catch (InterruptedException ignored) {}
                    break;
            }
        }
    }
}
