package com.ferra13671.BThack.mixins.entity;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Events.Player.PlayerMoveEvent;
import com.ferra13671.BThack.impl.Modules.MOVEMENT.NoSlow;
import com.ferra13671.BThack.impl.Modules.MOVEMENT.SafeWalk;
import com.ferra13671.MegaEvents.Base.Event;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.JumpingMount;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {

    @Shadow @Final protected MinecraftClient client;

    @Shadow public float prevNauseaIntensity;

    @Shadow public float nauseaIntensity;

    @Shadow private float mountJumpStrength;

    @Shadow private int field_3938;

    @Shadow public Input input;

    @Shadow protected abstract void startRidingJump();

    @Shadow public abstract float getMountJumpStrength();

    @Shadow @Nullable public abstract JumpingMount getJumpingMount();

    @Shadow protected abstract boolean isCamera();

    @Shadow public int underwaterVisibilityTicks;

    @Shadow public boolean falling;

    @Shadow @Final public ClientPlayNetworkHandler networkHandler;

    @Shadow protected abstract boolean canSprint();

    @Shadow protected int ticksLeftToDoubleTapSprint;

    @Shadow protected abstract boolean canStartSprinting();


    @Shadow protected abstract void pushOutOfBlocks(double x, double z);

    @Shadow public int ticksToNextAutojump;

    @Shadow public abstract boolean shouldSlowDown();

    @Shadow public boolean inSneakingPose;

    @Shadow protected abstract boolean isWalking();

    @Shadow protected abstract void updateNausea();

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    protected boolean clipAtLedge() {
        return super.clipAtLedge() || (Client.getModuleByName("SafeWalk").isEnabled() && !SafeWalk.mode.getValue().equals("Legit Shift"));
    }

    @Override
    protected Vec3d adjustMovementForSneaking(Vec3d movement, MovementType type) {
        Vec3d result = super.adjustMovementForSneaking(movement, type);

        if(movement != null) {
            ((SafeWalk) Client.getModuleByName("SafeWalk")).onClipAtLedge(!movement.equals(result));
        }

        return result;
    }

    @Inject(method = "updateNausea", at = @At("HEAD"), cancellable = true)
    public void modifyUpdateNausea(CallbackInfo ci) {
        if (Client.getModuleByName("PortalGod").isEnabled()) {
            ci.cancel();

            prevNauseaIntensity = nauseaIntensity;
            float f = 0.0F;
            if (this.inNetherPortal) {

                if (this.nauseaIntensity == 0.0F) {
                    this.client.getSoundManager().play(PositionedSoundInstance.ambient(SoundEvents.BLOCK_PORTAL_TRIGGER, this.random.nextFloat() * 0.4F + 0.8F, 0.25F));
                }

                f = 0.0125F;
                this.inNetherPortal = false;
            } else if (this.hasStatusEffect(StatusEffects.NAUSEA) && !this.getStatusEffect(StatusEffects.NAUSEA).isDurationBelow(60)) {
                f = 0.006666667F;
            } else if (this.nauseaIntensity > 0.0F) {
                f = -0.05F;
            }

            this.nauseaIntensity = MathHelper.clamp(this.nauseaIntensity + f, 0.0F, 1.0F);
            this.tickPortalCooldown();
        }
    }

    @Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
    public void modifyTickMovement(CallbackInfo ci) {
        if (Client.isOptionActivated("NoSlow", NoSlow.useItems)) {
            ci.cancel();

            if (ticksLeftToDoubleTapSprint > 0) {
                --ticksLeftToDoubleTapSprint;
            }

            if (!(client.currentScreen instanceof DownloadingTerrainScreen)) {
                updateNausea();
            }

            boolean bl = input.jumping;
            boolean bl2 = input.sneaking;
            boolean bl3 = isWalking();
            inSneakingPose = !getAbilities().flying && !isSwimming() && !hasVehicle() && canChangeIntoPose(EntityPose.CROUCHING) && (isSneaking() || !isSleeping() && !canChangeIntoPose(EntityPose.STANDING));
            float f = MathHelper.clamp(0.3F + EnchantmentHelper.getSwiftSneakSpeedBoost(this), 0.0F, 1.0F);
            input.tick(shouldSlowDown(), f);
            client.getTutorialManager().onMovement(input);

            boolean bl4 = false;
            if (ticksToNextAutojump > 0) {
                --ticksToNextAutojump;
                bl4 = true;
                input.jumping = true;
            }

            if (!this.noClip) {
                pushOutOfBlocks(getX() - (double)getWidth() * 0.35, getZ() + (double)getWidth() * 0.35);
                pushOutOfBlocks(getX() - (double)getWidth() * 0.35, getZ() - (double)getWidth() * 0.35);
                pushOutOfBlocks(getX() + (double)getWidth() * 0.35, getZ() - (double)getWidth() * 0.35);
                pushOutOfBlocks(getX() + (double)getWidth() * 0.35, getZ() + (double)getWidth() * 0.35);
            }

            if (bl2) {
                this.ticksLeftToDoubleTapSprint = 0;
            }

            boolean bl5 = canStartSprinting();
            boolean bl6 = hasVehicle() ? getVehicle().isOnGround() : isOnGround();
            boolean bl7 = !bl2 && !bl3;
            if ((bl6 || isSubmergedInWater()) && bl7 && bl5) {
                if (ticksLeftToDoubleTapSprint <= 0 && !client.options.sprintKey.isPressed()) {
                    ticksLeftToDoubleTapSprint = 7;
                } else {
                    setSprinting(true);
                }
            }

            if ((!isTouchingWater() || isSubmergedInWater()) && bl5 && client.options.sprintKey.isPressed()) {
                setSprinting(true);
            }

            boolean bl8;
            if (isSprinting()) {
                bl8 = !input.hasForwardMovement() || !canSprint();
                boolean bl9 = bl8 || this.horizontalCollision && !collidedSoftly || isTouchingWater() && !isSubmergedInWater();
                if (isSwimming()) {
                    if (!isOnGround() && !input.sneaking && bl8 || !isTouchingWater()) {
                        setSprinting(false);
                    }
                } else if (bl9) {
                    setSprinting(false);
                }
            }

            bl8 = false;
            if (getAbilities().allowFlying) {
                if (client.interactionManager.isFlyingLocked()) {
                    if (!getAbilities().flying) {
                        getAbilities().flying = true;
                        bl8 = true;
                        sendAbilitiesUpdate();
                    }
                } else if (!bl && input.jumping && !bl4) {
                    if (abilityResyncCountdown == 0) {
                        abilityResyncCountdown = 7;
                    } else if (!isSwimming()) {
                        getAbilities().flying = !getAbilities().flying;
                        bl8 = true;
                        sendAbilitiesUpdate();
                        abilityResyncCountdown = 0;
                    }
                }
            }

            if (input.jumping && !bl8 && !bl && !getAbilities().flying && !hasVehicle() && !isClimbing()) {
                ItemStack itemStack = getEquippedStack(EquipmentSlot.CHEST);
                if (itemStack.isOf(Items.ELYTRA) && ElytraItem.isUsable(itemStack) && checkFallFlying()) {
                    networkHandler.sendPacket(new ClientCommandC2SPacket(this, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                }
            }

            falling = isFallFlying();
            if (isTouchingWater() && input.sneaking && shouldSwimInFluids()) {
                knockDownwards();
            }

            int i;
            if (isSubmergedIn(FluidTags.WATER)) {
                i = isSpectator() ? 10 : 1;
                underwaterVisibilityTicks = MathHelper.clamp(underwaterVisibilityTicks + i, 0, 600);
            } else if (underwaterVisibilityTicks > 0) {
                isSubmergedIn(FluidTags.WATER);
                underwaterVisibilityTicks = MathHelper.clamp(underwaterVisibilityTicks - 10, 0, 600);
            }

            if (getAbilities().flying && isCamera()) {
                i = 0;
                if (input.sneaking) {
                    --i;
                }

                if (input.jumping) {
                    ++i;
                }

                if (i != 0) {
                    setVelocity(getVelocity().add(0.0, ((float)i * getAbilities().getFlySpeed() * 3.0F), 0.0));
                }
            }

            JumpingMount jumpingMount = getJumpingMount();
            if (jumpingMount != null && jumpingMount.getJumpCooldown() == 0) {
                if (field_3938 < 0) {
                    ++field_3938;
                    if (field_3938 == 0) {
                        mountJumpStrength = 0.0F;
                    }
                }

                if (bl && !input.jumping) {
                    field_3938 = -10;
                    jumpingMount.setJumpStrength(MathHelper.floor(getMountJumpStrength() * 100.0F));
                    startRidingJump();
                } else if (!bl && input.jumping) {
                    field_3938 = 0;
                    mountJumpStrength = 0.0F;
                } else if (bl) {
                    ++field_3938;
                    if (field_3938 < 10) {
                        mountJumpStrength = (float) field_3938 * 0.1F;
                    } else {
                        mountJumpStrength = 0.8F + 2.0F / (float)(field_3938 - 9) * 0.1F;
                    }
                }
            } else {
                mountJumpStrength = 0.0F;
            }

            super.tickMovement();
            if (isOnGround() && getAbilities().flying && !client.interactionManager.isFlyingLocked()) {
                getAbilities().flying = false;
                sendAbilitiesUpdate();
            }
        }
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void modifyMove(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        if (((Object) this) == client.player) {
            Event event = new PlayerMoveEvent();
            BThack.EVENT_BUS.activate(event);
            if (event.isCancelled())
                ci.cancel();
        }
    }
}
