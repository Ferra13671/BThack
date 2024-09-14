package com.ferra13671.BThack.api.Utils.Player;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.mixins.accessor.IEntity;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public final class PlayerUtils implements Mc {

    public static boolean isInWater() {
        return !mc.player.firstUpdate && ((IEntity) mc.player).getFluidHeight().getDouble(FluidTags.LAVA) > 0.0;
    }

    public static double calcMoveYaw() {
        float yawIn = mc.player.yaw;
        float moveForward = Math.round(mc.player.input.movementForward);
        float moveString = Math.round(mc.player.input.movementSideways);
        float yaw = yawIn;

        if (yawIn == 0) {
            yaw = mc.player.yaw;
        }

        float strafe = 90 * moveString;
        strafe *= (moveForward != 0F) ? moveForward * 0.5F : 1F;

        yaw -= strafe;
        yaw -= (moveForward < 0F) ? 180 : 0;

        return Math.toRadians(yaw);
    }

    /*
    public static void setAttributeValue(EntityAttribute attribute, double newValue) {
        AttributeContainerAccessor container = (AttributeContainerAccessor) mc.player.getAttributes();

        EntityAttributeInstance entityAttributeInstance = container.getCustom().get(attribute);
        if (entityAttributeInstance != null) {
            entityAttributeInstance.setBaseValue(newValue);
        } else {
            require(attribute).setBaseValue(newValue);
        }
    }

    private static EntityAttributeInstance require(EntityAttribute attribute) {
        DefaultAttributeContainerAccessor attributeContainer = (DefaultAttributeContainerAccessor) ((AttributeContainerAccessor) mc.player).getFallback();
        EntityAttributeInstance entityAttributeInstance = attributeContainer.getInstances().get(attribute);
        if (entityAttributeInstance == null) {
            throw new IllegalArgumentException("Can't find attribute " + Registries.ATTRIBUTE.getId(attribute));
        } else {
            return entityAttributeInstance;
        }
    }

     */

    public static boolean canEntityBeSeen(Entity entity, Entity target) {
        return BlockUtils.hasLineOfSight(AimBotUtils.getEyesPos(entity), target.getBoundingBox().getCenter());
    }

    public static float getEntitySpeed(Entity entity) {
        return (float) (MathUtils.getDistance(entity.getPos(), new Vec3d(entity.prevX, entity.prevY, entity.prevZ)) * 20);
    }


    public static Entity createNewFakePlayer(PlayerEntity parent, String name) {
        return createNewFakePlayer(parent, new GameProfile(UUID.randomUUID(), name));
    }

    public static Entity createNewFakePlayer(PlayerEntity parent, GameProfile profile) {
        OtherClientPlayerEntity entity = new OtherClientPlayerEntity(mc.world, profile);
        entity.copyPositionAndRotation(parent);
        entity.prevYaw = entity.getYaw();
        entity.prevPitch = entity.getPitch();
        entity.headYaw = parent.headYaw;
        entity.prevHeadYaw = entity.headYaw;
        entity.bodyYaw = parent.bodyYaw;
        entity.prevBodyYaw = entity.bodyYaw;
        Byte playerModel = parent.getDataTracker()
                .get(PlayerEntity.PLAYER_MODEL_PARTS);
        entity.dataTracker.set(PlayerEntity.PLAYER_MODEL_PARTS, playerModel);
        entity.getAttributes().setFrom(parent.getAttributes());
        entity.setPose(parent.getPose());
        entity.setHealth(parent.getHealth());
        entity.setAbsorptionAmount(parent.getAbsorptionAmount());
        // setBoundingBox(player.getBoundingBox());
        entity.getInventory().clone(parent.getInventory());
        entity.setId(new AtomicInteger(1000000).incrementAndGet());
        entity.age = 100;

        entity.unsetRemoved();
        mc.world.addEntity(entity);

        return entity;
    }

    public static void removeEntity(Entity entity) {
        mc.world.removeEntity(entity.getId(), Entity.RemovalReason.DISCARDED);
        entity.setRemoved(Entity.RemovalReason.DISCARDED);
    }

    public static void closeScreen() {
        RenderSystem.recordRenderCall(() -> mc.player.closeHandledScreen());
    }
}
