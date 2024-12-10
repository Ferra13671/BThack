package com.ferra13671.BThack.impl.Modules.COMBAT.KillAura;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.Player.VelocityUpdateEvent;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.BThack.mixins.accessor.IPlayerInputC2SPacket;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;

/*
It's already better than old KillAura, but it can still sometimes hit badly in Aura mode :)
 */
public class KillAura extends Module {

    public static ModeSetting mode;
    public static ModeSetting attackMode;
    public static NumberSetting range;
    public static ModeSetting rotateMode;
    public static NumberSetting packets;
    public static NumberSetting delay;
    public static NumberSetting postCooldown;

    public static BooleanSetting instaAttack;
    public static NumberSetting lockTicks;

    public static BooleanSetting players;
    public static BooleanSetting mobs;
    public static BooleanSetting teammates;
    public static BooleanSetting friends;
    public static BooleanSetting ignoreWalls;

    public static BooleanSetting clanManager;
    public static ModeSetting clanMode;
    public static ModeSetting targetClan;

    public KillAura() {
        super("KillAura",
                "lang.module.KillAura",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Aura", "TriggerBot")));
        attackMode = new ModeSetting("AttackMode", this, new ArrayList<>(Arrays.asList("CoolDown", "Delay")));
        range = new NumberSetting("Range", this, 4.0, 1, 10, false, () -> mode.getValue().equals("Aura"));
        rotateMode = new ModeSetting("RotateMode", this, new ArrayList<>(Arrays.asList("Packet", "Vanilla", "Grim", "None")), () -> !mode.getValue().equals("TriggerBot"));
        packets = new NumberSetting("Packets", this, 1, 1, 5, true, () -> rotateMode.getValue().equals("Packet") && mode.getValue().equals("Aura"));
        delay = new NumberSetting("Delay(Second)", this, 1.4, 0.1, 4, false, () -> attackMode.getValue().equals("Delay"));
        postCooldown = new NumberSetting("Post Cooldown", this, 0, 0, 100, true, () -> attackMode.getValue().equals("CoolDown"));

        instaAttack = new BooleanSetting("Insta Attack", this, false, () -> mode.getValue().equals("Aura") && !mode.getValue().equals("Grim"));
        lockTicks = new NumberSetting("Lock Ticks", this, 5, 3, 10, false, () -> mode.getValue().equals("Aura") && (mode.getValue().equals("Grim") || !instaAttack.getValue()));

        players = new BooleanSetting("Players", this, true);
        mobs = new BooleanSetting("Mobs", this, true);
        teammates = new BooleanSetting("Teammates", this, false);
        friends = new BooleanSetting("Friends", this, false);
        ignoreWalls = new BooleanSetting("Ignore Walls", this, false);

        clanManager = ClansUtils.getClanManagerSetting(this);
        clanMode = ClansUtils.getClanModeSetting(this, clanManager);
        targetClan = ClansUtils.getClanTargetSetting(this, clanManager, clanMode);

        initSettings(
                mode,
                attackMode,
                range,
                rotateMode,
                packets,
                delay,
                postCooldown,

                instaAttack,
                lockTicks,

                players,
                mobs,
                teammates,
                friends,
                ignoreWalls,

                clanManager,
                clanMode,
                targetClan
        );
    }

    private final Ticker delayTicker = new Ticker();
    private Target targetedEntity;
    private float[] rotations;

    @Override
    public void onEnable() {
        super.onEnable();
        delayTicker.reset();
        targetedEntity = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        delayTicker.reset();
        targetedEntity = null;
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        String killAuraMode = mode.getValue();

        arrayListInfo = killAuraMode + (!killAuraMode.equals("TriggerBot") ? "; " + range.getValue() : "");

        switch (killAuraMode) {
            case "Aura" -> auraMode();
            case "TriggerBot" -> triggerBotMode();
        }
    }

    @EventSubscriber
    public void onPacket(PacketEvent.Send e) {
        if (mode.getValue().equals("Aura")) {
            if (mode.getValue().equals("Grim") || !instaAttack.getValue()) {
                if (targetedEntity == null) {
                    if (rotations != null) {
                        rotations[0] = MathHelper.lerp(mc.getTickDelta(), rotations[0], mc.player.getYaw());
                        rotations[1] = MathHelper.lerp(mc.getTickDelta(), rotations[1], mc.player.getPitch());
                        if (e.getPacket() instanceof PlayerInputC2SPacket) {
                            IPlayerInputC2SPacket packet = (IPlayerInputC2SPacket) e.getPacket();
                            changeInput(packet);
                        }
                        if (e.getPacket() instanceof PlayerMoveC2SPacket packet) {
                            packet.yaw = rotations[0];
                            packet.pitch = rotations[1];
                        }
                        rotations = null;
                    }
                } else {
                    if (e.getPacket() instanceof PlayerInputC2SPacket) {
                        IPlayerInputC2SPacket packet = (IPlayerInputC2SPacket) e.getPacket();
                        changeInput(packet);
                    }
                    if (e.getPacket() instanceof PlayerMoveC2SPacket packet) {
                        packet.yaw = rotations[0];
                        packet.pitch = rotations[1];
                    }
                }
            }
        }
    }

    @EventSubscriber
    public void onSetMove(VelocityUpdateEvent e) {
        if (mode.getValue().equals("Aura")) {
            if (mode.getValue().equals("Grim") || !instaAttack.getValue()) {
                if (targetedEntity != null) {
                    e.setCancelled(true);
                    e.setVelocity(movementInputToVelocity(rotations[0], e.getMovementInput(), e.getSpeed()));
                }
            }
        }
    }

    //---------Aura---------//
    public void auraMode() {
        if (!mc.player.isAlive()) {
            targetedEntity = null;
            return;
        }
        if (!delayPassed()) return;
        if (targetedEntity == null || targetedEntity.lockTicks <= 0) {
            targetSearchAction();
        }

        attackTargetAction();
    }

    public void targetSearchAction() {
        Entity target = null;
        if (players.getValue())
            target = KillAuraUtils.filterPlayers(range.getValue(), friends.getValue(), teammates.getValue(), clanManager.getValue(), clanMode.getValue(), KillAura.targetClan.getValue());
        if (target == null && mobs.getValue())
            target = KillAuraUtils.filterEntity(range.getValue(), entity -> entity instanceof LivingEntity && !(entity instanceof PlayerEntity));

        if (target != null) {
            targetedEntity = new Target(target, 0);
        }
    }

    public void attackTargetAction() {
        if (targetedEntity != null) {
            if (targetedEntity.lockTicks >= (int) lockTicks.getValue()) {
                RotateMode rotateMode = getRotateMode();
                KillAuraUtils.preAttackRotate(rotateMode, rotations, (int) packets.getValue());
                KillAuraUtils.attackNoRotate(targetedEntity.entity);
                //KillAuraUtils.postAttackRotate(rotateMode);
                delayTicker.reset();
                targetedEntity = null;
            } else {
                Vec3d rotateVector = targetedEntity.entity.getPos();
                rotations = AimBotUtils.rotations(rotateVector);
                KillAuraUtils.preAttackRotate(getRotateMode(), rotations, (int) packets.getValue());
                if (mode.getValue().equals("Grim") || !instaAttack.getValue()) {
                    targetedEntity = new Target(targetedEntity.entity, targetedEntity.lockTicks + 1);
                } else {
                    targetedEntity = new Target(targetedEntity.entity, (int) lockTicks.getValue());
                }
            }
        }
    }

    public void changeInput(IPlayerInputC2SPacket packet) {
        float forward = packet._getForward();
        float sideways = packet._getSideways();
        float delta = (mc.player.getYaw() - rotations[0]) * MathHelper.RADIANS_PER_DEGREE;
        float cos = MathHelper.cos(delta);
        float sin = MathHelper.sin(delta);
        packet._setSideways(Math.round(sideways * cos - forward * sin));
        packet._setForward(Math.round(forward * cos + sideways * sin));
    }
    //----------------------//

    //---------TriggerBot---------//
    public void triggerBotMode() {
        if (!delayPassed()) return;
        HitResult objectMouseOver = mc.crosshairTarget;

        if (objectMouseOver instanceof EntityHitResult entityHitResult) {
            Entity ent = entityHitResult.getEntity();

            if (KillAura.players.getValue() && ent instanceof PlayerEntity player) {
                if (KillAuraUtils.filterPlayer(player, friends.getValue(), teammates.getValue(), clanManager.getValue(), clanMode.getValue(), targetClan.getValue())) {
                    KillAuraUtils.attack(ent, RotateMode.NONE, 0);
                }
            }
            if (KillAura.mobs.getValue()) {
                if (KillAuraUtils.isCurrentMob(ent) && ent.isAlive()) {
                    KillAuraUtils.attack(ent, RotateMode.NONE, 0);
                }
            }

            delayTicker.reset();
        }
    }
    //----------------------------//

    public RotateMode getRotateMode() {
        return switch (KillAura.rotateMode.getValue()) {
            case "Packet" -> RotateMode.PACKET;
            case "Vanilla" -> RotateMode.VANILLA;
            case "Grim" -> RotateMode.GRIM;
            default -> RotateMode.NONE;
        };
    }

    public boolean delayPassed() {
        return switch (attackMode.getValue()) {
            case "CoolDown" -> mc.player.getAttackCooldownProgress(0) >= 1.0;
            case "Delay" -> delayTicker.passed((int) (delay.getValue() * 1000));
            default -> true;
        };
    }

    private Vec3d movementInputToVelocity(float yaw, Vec3d movementInput, float speed) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7) {
            return Vec3d.ZERO;
        }
        Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply(speed);
        float f = MathHelper.sin(yaw * MathHelper.RADIANS_PER_DEGREE);
        float g = MathHelper.cos(yaw * MathHelper.RADIANS_PER_DEGREE);
        return new Vec3d(vec3d.x * (double) g - vec3d.z * (double) f, vec3d.y, vec3d.z * (double) g + vec3d.x * (double) f);
    }

    private record Target(Entity entity, int lockTicks) {}
}