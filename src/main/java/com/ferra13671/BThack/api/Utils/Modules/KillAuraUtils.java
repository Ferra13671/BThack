package com.ferra13671.BThack.api.Utils.Modules;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Social.Clans.Clan;
import com.ferra13671.BThack.api.Social.Clans.ClanStatus;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Social.Friends.FriendsUtils;
import com.ferra13671.BThack.api.Utils.Player.PlayerUtils;
import com.ferra13671.BThack.impl.Modules.COMBAT.KillAura.RotateMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.Comparator;

public final class KillAuraUtils implements Mc {
    public static void CoolDownAttack(Entity target, Thread thread, int postCooldownDelay, RotateMode rotateMode, int packets) {
        if (mc.player.getAttackCooldownProgress(0) >= 1.0) {
            try {
                if (postCooldownDelay > 0 && thread != null)
                    thread.sleep(postCooldownDelay);
            } catch (InterruptedException ignored) {}
            switch (rotateMode) {
                case PACKET -> {
                    for (int i = 0; i < packets; i++)
                        AimBotUtils.packetRotateToEntity(target);
                }
                case VANILLA -> AimBotUtils.rotateToEntity(target);
            }
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    public static void DelayAttack(Entity target, RotateMode rotateMode, int packets) {
        switch (rotateMode) {
            case PACKET -> {
                for (int i = 0; i < packets; i++)
                    AimBotUtils.packetRotateToEntity(target);
            }
            case VANILLA -> AimBotUtils.rotateToEntity(target);
        }
        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    public static PlayerEntity filterPlayers(NumberSetting range, BooleanSetting friends, BooleanSetting teammates, BooleanSetting clanManager, ModeSetting clanMode, ModeSetting targetClan) {

        return mc.world.getPlayers().stream().filter(entityPlayer -> entityPlayer != mc.player && !isFriend(entityPlayer, friends) && !isTeammate(entityPlayer, teammates) && isSuccessfulClanMember(entityPlayer, clanManager, clanMode, targetClan) && entityPlayer.isAlive()).min(Comparator.comparing(entityPlayer ->
                entityPlayer.distanceTo(mc.player))).filter(entityPlayer -> entityPlayer.distanceTo(mc.player) <= range.getValue()).orElse(null);
    }

    public static Entity filterEntity(double range) {
        ArrayList<Entity> entities = new ArrayList<>();
        for (Entity entity : mc.world.getEntities()) {
            entities.add(entity);
        }

        return entities.stream().filter(entity1 -> entity1 != mc.player && entity1.isAlive()).min(Comparator.comparing(entity1 ->
                entity1.distanceTo(mc.player))).filter(entity1 -> entity1.distanceTo(mc.player) <= range).orElse(null);
    }

    public static boolean canBeSeeTarget(BooleanSetting ignoreWalls, Entity target) {
        if (!ignoreWalls.getValue()) {
            return PlayerUtils.canEntityBeSeen(mc.player, target);
        } else {
            return true;
        }
    }

    public static boolean isCurrentMob(Entity entity) {
        return entity instanceof MobEntity || entity instanceof FlyingEntity || entity instanceof PassiveEntity || entity instanceof AmbientEntity || entity instanceof WaterCreatureEntity || entity instanceof GolemEntity;
    }

    public static boolean isCurrentMonster(Entity entity) {
        return entity instanceof MobEntity || entity instanceof GolemEntity;
    }

    public static boolean isCurrentAnimal(Entity entity) {
        return entity instanceof PassiveEntity;
    }

    private static boolean isFriend(PlayerEntity player, BooleanSetting friends) {
        if (!friends.getValue()) {
            return FriendsUtils.isFriend(player);
        }
        return false;
    }

    private static boolean isTeammate(PlayerEntity player, BooleanSetting teammates) {
        if (!teammates.getValue()) {
            return mc.player.isTeammate(player);
        }
        return false;
    }

    public static boolean isSuccessfulClanMember(PlayerEntity player, BooleanSetting clanManager, ModeSetting clanMode, ModeSetting targetClan) {
        if (clanManager.getValue()) {
            ArrayList<Clan> clans = ClansUtils.getClansFromMember(player.getDisplayName().getString());
            switch (clanMode.getValue()) {
                case "Only Enemy":
                    if (!clans.isEmpty()) {
                        for (Clan clan : clans) {
                            if (ClanStatus.ENEMY.getName().contains(clan.getStatus())) {
                                return true;
                            }
                        }
                        return false;
                    } else {
                        return true;
                    }
                case "Neutral Also":
                    if (!clans.isEmpty()) {
                        for (Clan clan : clans) {
                            if (ClanStatus.ENEMY.getName().contains(clan.getStatus()) || ClanStatus.NEUTRAL.getName().contains(clan.getStatus())) {
                                return true;
                            }
                        }
                        return false;
                    } else {
                        return true;
                    }
                case "Target Clan":
                    if (!clans.isEmpty()) {
                        for (Clan clan : clans) {
                            if (clan.getName().equals(targetClan.getValue())) {
                                return true;
                            }
                        }
                        return false;
                    } else {
                        return true;
                    }
                case "All Clans":
                default:
                    return true;
            }
        } else {
            return true;
        }
    }
}
