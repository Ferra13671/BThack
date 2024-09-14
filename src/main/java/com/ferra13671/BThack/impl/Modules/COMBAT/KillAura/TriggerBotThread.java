package com.ferra13671.BThack.impl.Modules.COMBAT.KillAura;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Social.Friends.FriendList;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class TriggerBotThread extends Thread implements Mc {
    private int delay;

    public void run() {
        try {
            delay = (int) (KillAura.delay.getValue() * 1000);

            HitResult objectMouseOver = mc.crosshairTarget;

            if (objectMouseOver != null) {
                if (objectMouseOver instanceof EntityHitResult entityHitResult) {
                    Entity ent = entityHitResult.getEntity();

                    if (KillAura.players.getValue() && ent != null) {
                        if (KillAura.teammates.getValue()) {
                            attack(ent);
                        } else {
                            if (!mc.player.isTeammate(ent)) {
                                attack(ent);
                            }
                        }
                    }

                    if (KillAura.mobs.getValue()) {
                        if (KillAuraUtils.isCurrentMob(ent) && ent.isAlive()) {
                            switch (KillAura.attackMode.getValue()) {
                                case "CoolDown":
                                    KillAuraUtils.CoolDownAttack(ent, this, (int) KillAura.postCooldown.getValue(), RotateMode.NONE, 0);
                                    break;
                                case "Delay":
                                    KillAuraUtils.DelayAttack(ent, RotateMode.NONE, 0);
                                    try {
                                        sleep(delay);
                                    } catch (InterruptedException ignored) {
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    private void attack(Entity ent) {
        if (KillAura.friends.getValue()) {
            if (ent instanceof PlayerEntity && ((PlayerEntity) ent).getHealth() > 0) {
                switch (KillAura.attackMode.getValue()) {
                    case "CoolDown":
                        KillAuraUtils.CoolDownAttack(ent, this, (int) KillAura.postCooldown.getValue(), RotateMode.NONE, 0);
                        break;
                    case "Delay":
                        KillAuraUtils.DelayAttack(ent, RotateMode.NONE, 0);
                        try {
                            sleep(delay);
                        } catch (InterruptedException ignored) {}
                        break;
                }
            }
        } else {
            if (ent instanceof PlayerEntity && ((PlayerEntity) ent).getHealth() > 0) {
                String name = ent.getDisplayName().getString();

                if (!FriendList.friends.contains(name)) {
                    switch (KillAura.attackMode.getValue()) {
                        case "CoolDown":
                            KillAuraUtils.CoolDownAttack(ent, this, (int) KillAura.postCooldown.getValue(), RotateMode.NONE, 0);
                            break;
                        case "Delay":
                            KillAuraUtils.DelayAttack(ent, RotateMode.NONE, 0);
                            try {
                                sleep(delay);
                            } catch (InterruptedException ignored) {}
                            break;
                    }
                }
            }
        }
    }
}
