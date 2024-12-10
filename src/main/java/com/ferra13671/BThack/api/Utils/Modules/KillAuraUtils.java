package com.ferra13671.BThack.api.Utils.Modules;

import com.ferra13671.BTbot.api.Utils.Generate.GenerateNumber;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Social.Clans.Clan;
import com.ferra13671.BThack.api.Social.Clans.ClanStatus;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.PlayerUtils;
import com.ferra13671.BThack.impl.Modules.COMBAT.KillAura.RotateMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;

public final class KillAuraUtils implements Mc {

    public static void attack(Entity target, RotateMode rotateMode, int packets) {
        attackInternal(target, rotateMode, packets);
    }

    private static void attackInternal(Entity target, RotateMode rotateMode, int packets) {
        Vec3d rotateVector = target.getPos();
        double xLength = Math.abs(target.getBoundingBox().maxX - target.getBoundingBox().minX);
        double yLength = Math.abs(target.getBoundingBox().maxY - target.getBoundingBox().minY);
        double zLength = Math.abs(target.getBoundingBox().maxZ - target.getBoundingBox().minZ);
        rotateVector.x += GenerateNumber.generateDouble(-(xLength / 3), xLength / 3);
        rotateVector.y += GenerateNumber.generateDouble(-(yLength / 3), yLength / 3);
        rotateVector.z += GenerateNumber.generateDouble(-(zLength / 3), zLength / 3);
        float[] rotations = AimBotUtils.rotations(rotateVector);
        preAttackRotate(rotateMode, rotations, packets);
        attackNoRotate(target);
        postAttackRotate(rotateMode);
    }

    public static void attackNoRotate(Entity target) {
        if (mc.player.isSprinting())
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
        mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
        if (mc.player.isSneaking()) {
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
        }
    }

    public static void preAttackRotate(RotateMode rotateMode, float[] rotations, int packets) {
        switch (rotateMode) {
            case PACKET -> {
                for (int i = 0; i < packets; i++) {
                    AimBotUtils.packetRotate(rotations[0], rotations[1]);
                }
            }
            case VANILLA -> AimBotUtils.rotate(rotations[0], rotations[1]);
            case GRIM -> {
                //mc.player.networkHandler.sendPacket(new PlayerInputC2SPacket(mc.player.input.movementSideways, mc.player.input.movementForward, mc.player.input.jumping, mc.player.input.sneaking));
                //GrimUtils.sendPostActionGrimPackets();
                //mc.player.networkHandler.sendPacket(new PlayerInputC2SPacket(mc.player.input.movementSideways, mc.player.input.movementForward, mc.player.input.jumping, mc.player.input.sneaking));
                GrimUtils.sendPreActionGrimPackets(rotations[0], rotations[1]);
                //mc.player.networkHandler.sendPacket(new PlayerInputC2SPacket(mc.player.input.movementSideways, mc.player.input.movementForward, mc.player.input.jumping, mc.player.input.sneaking));
            }
        }
    }

    public static void postAttackRotate(RotateMode rotateMode) {
        if (rotateMode == RotateMode.GRIM) {
            mc.player.networkHandler.sendPacket(new PlayerInputC2SPacket(mc.player.input.movementSideways, mc.player.input.movementForward, mc.player.input.jumping, mc.player.input.sneaking));
            GrimUtils.sendPostActionGrimPackets();
        }
    }

    public static PlayerEntity filterPlayers(double range, boolean friends, boolean teammates, boolean clanManager, String clanMode, String targetClan, EntityFilter<Boolean> extraFilter) {
        return mc.world.getPlayers().stream().filter(entityPlayer -> filterPlayer(entityPlayer, friends, teammates, clanManager, clanMode, targetClan) && extraFilter.get(entityPlayer)).min(Comparator.comparing(entityPlayer ->
                entityPlayer.distanceTo(mc.player))).filter(entityPlayer -> entityPlayer.distanceTo(mc.player) <= range).orElse(null);
    }

    public static PlayerEntity filterPlayers(double range, boolean friends, boolean teammates, boolean clanManager, String clanMode, String targetClan) {
        return filterPlayers(range, friends, teammates, clanManager, clanMode, targetClan, (entity) -> true);
    }

    public static boolean filterPlayer(PlayerEntity entityPlayer, boolean friends, boolean teammates, boolean clanManager, String clanMode, String targetClan) {
        return entityPlayer != mc.player && !isFriend(entityPlayer, friends) && !isTeammate(entityPlayer, teammates) && isSuccessfulClanMember(entityPlayer, clanManager, clanMode, targetClan) && entityPlayer.isAlive();
    }

    public static Entity filterEntity(double range, EntityFilter<Boolean> extraFilter) {
        ArrayList<Entity> entities = new ArrayList<>();
        for (Entity entity : mc.world.getEntities()) {
            entities.add(entity);
        }

        return entities.stream().filter(entity1 -> entity1 != mc.player && entity1.isAlive() && extraFilter.get(entity1)).min(Comparator.comparing(entity1 ->
                entity1.distanceTo(mc.player))).filter(entity1 -> entity1.distanceTo(mc.player) <= range).orElse(null);
    }

    public static Entity filterEntity(double range) {
        return filterEntity(range, (entity) -> true);
    }

    public static boolean canBeSeeTarget(BooleanSetting ignoreWalls, Entity target) {
        return canBeSeeTarget(ignoreWalls.getValue(), target);
    }

    public static boolean canBeSeeTarget(boolean ignoreWalls, Entity target) {
        if (!ignoreWalls) {
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

    public static boolean isFriend(PlayerEntity player, BooleanSetting friends) {
        return isFriend(player, friends.getValue());
    }

    public static boolean isFriend(PlayerEntity player, boolean friends) {
        if (!friends) {
            return SocialManagers.FRIENDS.contains(player);
        }
        return false;
    }

    public static boolean isTeammate(PlayerEntity player, BooleanSetting teammates) {
        return isTeammate(player, teammates.getValue());
    }

    public static boolean isTeammate(PlayerEntity player, boolean teammates) {
        if (!teammates) {
            return mc.player.isTeammate(player);
        }
        return false;
    }

    public static boolean isSuccessfulClanMember(PlayerEntity player, BooleanSetting clanManager, ModeSetting clanMode, ModeSetting targetClan) {
        return isSuccessfulClanMember(player, clanManager.getValue(), clanMode.getValue(), targetClan.getValue());
    }

    public static boolean isSuccessfulClanMember(PlayerEntity player, boolean clanManager, String clanMode, String targetClan) {
        if (clanManager) {
            ArrayList<Clan> clans = ClansUtils.getClansFromMember(player.getDisplayName().getString());
            switch (clanMode) {
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
                            if (clan.getName().equals(targetClan)) {
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
