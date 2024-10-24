package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Events.Entity.JumpHeightEvent;
import com.ferra13671.BThack.Events.Entity.SetVelocityEvent;
import com.ferra13671.BThack.Events.PacketEvent;
import com.ferra13671.BThack.Events.Player.PlayerMoveEvent;
import com.ferra13671.BThack.Events.Player.PlayerTraverRotEvent;
import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Grim.GrimFreezeUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.StrafeUtils;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.Player.PlayerUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoFirework;
import com.ferra13671.BThack.mixins.accessor.IEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;

public class ElytraFlight extends Module {

    public static ModeSetting mode;

    //Bounce Settings
    public static NumberSetting jumpHeight;
    public static ModeSetting alwaysPress;
    public static BooleanSetting grimFreeze;

    public static BooleanSetting strafing;

    public static BooleanSetting abusePitch;
    public static NumberSetting pitch;

    public static BooleanSetting fireworkStart;
    public static NumberSetting fireworkPitch;
    public static NumberSetting ignoreSpeed;

    public static BooleanSetting microBoost;
    public static NumberSetting boostStrength;
    public static NumberSetting maxBoost;
    /////////

    //Control Settings
    public static NumberSetting upPitch;
    public static NumberSetting packetPitch;
    public static NumberSetting downPitch;
    public static BooleanSetting allowStrafing;
    public static NumberSetting fireworkDelay;
    public static BooleanSetting noYMove;
    public static BooleanSetting ignoreMovement;

    public static ModeSetting moveMode;
    public static NumberSetting lerpSpeed;
    public static ModeSetting stopMode;
    public static BooleanSetting yAlso;
    public static NumberSetting downSpeed;
    public static BooleanSetting stopPackets;
    public static BooleanSetting onGround;
    /////////

    public ElytraFlight() {
        super("ElytraFlight",
                "lang.module.ElytraFlight",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );

        mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Bounce", "Control")));

        //Bounce Settings
        jumpHeight = new NumberSetting("Jump Height", this, 0.42, 0.05, 0.42, false, () -> mode.getValue().equals("Bounce"));
        alwaysPress = new ModeSetting("Always Press", this, new ArrayList<>(Arrays.asList("None", "Sprint", "Shift", "Multi")), () -> mode.getValue().equals("Bounce"));
        grimFreeze = new BooleanSetting("Grim Freeze", this, false, () -> mode.getValue().equals("Bounce"));
        strafing = new BooleanSetting("Strafing", this, true, () -> mode.getValue().equals("Bounce"));

        abusePitch = new BooleanSetting("Abuse Pitch", this, true, () -> mode.getValue().equals("Bounce"));
        pitch = new NumberSetting("Pitch", this, 33, 0, 90, false, () -> abusePitch.getValue() && mode.getValue().equals("Bounce"));
        fireworkPitch = new NumberSetting("Firework Pitch", this, -1.20, -5, 0, false, () -> abusePitch.getValue() && mode.getValue().equals("Bounce"));
        fireworkStart = new BooleanSetting("Firework Start", this, false, () -> mode.getValue().equals("Bounce"));
        ignoreSpeed = new NumberSetting("Ignore SpedM", this, 17, 10, 27, false, () -> fireworkStart.getValue() && mode.getValue().equals("Bounce"));

        microBoost = new BooleanSetting("Micro Boost", this, false, () -> mode.getValue().equals("Bounce"));
        boostStrength = new NumberSetting("Boost Strength", this, 0.001, 0.0001, 0.01, false, () -> microBoost.getValue() && mode.getValue().equals("Bounce"));
        maxBoost = new NumberSetting("Max Boost", this, 40, 35, 60, false, () -> microBoost.getValue() && mode.getValue().equals("Bounce"));
        /////////

        //Control Settings
        upPitch = new NumberSetting("Up Pitch", this, -40, -90, -5, false, () -> mode.getValue().equals("Control"));
        packetPitch = new NumberSetting("Packet Pitch", this, -1.4, -3.5, -0.8, false, () -> mode.getValue().equals("Control"));
        downPitch = new NumberSetting("Down Pitch", this, 40, 5, 90, false, () -> mode.getValue().equals("Control"));
        allowStrafing = new BooleanSetting("Allow Strafing", this, true, () -> mode.getValue().equals("Control"));
        fireworkDelay = new NumberSetting("FireWork Delay", this, 0, 0, 2500, true, () -> mode.getValue().equals("Control"));
        noYMove = new BooleanSetting("No Y Move", this, true, () -> mode.getValue().equals("Control"));
        ignoreMovement = new BooleanSetting("Ignore Movement", this, true, () -> mode.getValue().equals("Control"));

        moveMode = new ModeSetting("Move Mode", this, new ArrayList<>(Arrays.asList("Instance", "Lerp")), () -> mode.getValue().equals("Control"));
        lerpSpeed = new NumberSetting("Lerp Speed", this, 0.5, 0.05, 0.75, false, () -> mode.getValue().equals("Control") && moveMode.getValue().equals("Lerp"));
        stopMode = new ModeSetting("Stop Mode", this, new ArrayList<>(Arrays.asList("None", "Glide", "Jitter")), () -> mode.getValue().equals("Control"));
        yAlso = new BooleanSetting("Y Also", this, false, () -> mode.getValue().equals("Control") && (stopMode.getValue().equals("Glide") || stopMode.getValue().equals("Jitter")));
        downSpeed = new NumberSetting("Down Speed", this, 0.05, 0.001, 0.1, false, () -> mode.getValue().equals("Control") && yAlso.getValue() && (stopMode.getValue().equals("Glide") || stopMode.getValue().equals("Jitter")));
        stopPackets = new BooleanSetting("Stop Packets", this, false, () -> mode.getValue().equals("Control") && (stopMode.getValue().equals("Glide") || stopMode.getValue().equals("Jitter")));
        onGround = new BooleanSetting("OnGround", this, false, () -> mode.getValue().equals("Control") && (stopMode.getValue().equals("Glide") || stopMode.getValue().equals("Jitter")));
        /////////

        initSettings(
                mode,

                jumpHeight,
                alwaysPress,
                grimFreeze,
                strafing,
                abusePitch,
                pitch,
                fireworkStart,
                fireworkPitch,
                ignoreSpeed,
                microBoost,
                boostStrength,
                maxBoost,

                upPitch,
                packetPitch,
                downPitch,
                allowStrafing,
                fireworkDelay,
                noYMove,
                ignoreMovement,
                moveMode,
                lerpSpeed,
                stopMode,
                yAlso,
                downSpeed,
                stopPackets,
                onGround
        );
    }

    //Bounce field
    public static boolean fireworkUsed = false;

    //Control field
    public static boolean fireworkCheck = false;
    public float controlYaw = Float.MIN_VALUE;
    public float controlPitch = Float.MIN_VALUE;
    public float packetControlPitch = Float.MIN_VALUE;

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        super.onEnable();
        fireworkUsed = false;

        fireworkCheck = false;
        controlYaw = Float.MIN_VALUE;
        controlPitch = Float.MIN_VALUE;
        packetControlPitch = Float.MIN_VALUE;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (nullCheck()) return;

        // Bounce Disable
        mc.options.forwardKey.setPressed(false);
        mc.options.jumpKey.setPressed(false);

        mc.options.sneakKey.setPressed(false);
        mc.player.setSneaking(false);
        mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));

        mc.options.sprintKey.setPressed(false);
        mc.player.setSprinting(false);
        mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));

        fireworkUsed = false;
        /////////

        // Control Disable
        fireworkCheck = false;
        controlYaw = Float.MIN_VALUE;
        controlPitch = Float.MIN_VALUE;
        packetControlPitch = Float.MIN_VALUE;
        /////////
    }

    @EventSubscriber
    public void onTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;
        if (mc.player.getInventory().getArmorStack(2).getItem() != Items.ELYTRA) return;

        switch (mode.getValue()) {
            case "Bounce" -> bounceMode();
            case "Control" -> controlMode();
        }
    }

    @EventSubscriber
    public void onJumpHeight(JumpHeightEvent e) {
        if (nullCheck() || !mode.getValue().equals("Bounce")) return;
        if (mc.player.getInventory().getArmorStack(2).getItem() != Items.ELYTRA) return;
        e.setJumpHeight((float) jumpHeight.getValue());
    }

    @EventSubscriber
    public void onSend(PacketEvent.Send e) {
        if (nullCheck()) return;
        if (mc.player.getInventory().getArmorStack(2).getItem() != Items.ELYTRA) return;

        switch (mode.getValue()) {
            case "Bounce" -> bouncePacket(e);
            case "Control" -> controlPacket(e);
        }
    }

    @EventSubscriber
    public void onReceive(PacketEvent.Receive e) {
        if (mode.getValue().equals("Control")) {
            if (!isMoving() && stopMode.getValue().equals("Glide") || stopMode.getValue().equals("Jitter") && stopPackets.getValue()) {
                if (e.getPacket() instanceof CommonPingS2CPacket)
                    e.setCancelled(true);
            }
        }
    }

    @EventSubscriber
    public void onTravelRot(PlayerTraverRotEvent e) {
        if (Client.getModuleByName("ElytraFlight").isEnabled()) {
            float yaw = e.yaw;
            float pitch = e.pitch;

            switch (ElytraFlight.mode.getValue()) {
                case "Bounce" -> {
                    pitch = bouncePitchRotate(pitch);
                    yaw = bounceYawRotate(pitch);
                }
                case "Control" -> {
                    yaw = controlYawRotate(yaw);
                    pitch = controlPitchRotate(pitch, false);
                }
            }
            e.yaw = yaw;
            e.pitch = pitch;
        }
    }

    @EventSubscriber
    public void onSetVelocity(SetVelocityEvent e) {
        if (mode.getValue().equals("Control")) {
            if (!isMoving()) {
                if (stopMode.getValue().equals("Glide")) {
                    e.setVelocity(yAlso.getValue() ? new Vec3d(0, -downSpeed.getValue(), 0) : new Vec3d(0, e.getVelocity().y, 0));
                }
                if (stopMode.getValue().equals("Jitter")) {
                    e.setVelocity(yAlso.getValue() ? new Vec3d(0, mc.player.age % 2 == 0 ? downSpeed.getValue() : -downSpeed.getValue(), 0) : new Vec3d(0, e.getVelocity().y, 0));
                }
            } else {
                if (noYMove.getValue()) {
                    if (!mc.options.jumpKey.isPressed() && !mc.options.sneakKey.isPressed()) {
                        Vec3d vec3d = e.getVelocity();
                        vec3d.y = -0.0001;   // No Y Motion lol
                        e.setVelocity(vec3d);
                    }
                }
            }
        }
    }

    @EventSubscriber
    public void onMove(PlayerMoveEvent e) {
        if (mc.player.input.movementSideways > 0) {
            mc.player.input.movementSideways = 1;
        } else if (mc.player.input.movementSideways < 0) {
            mc.player.input.movementSideways = -1;
        }
        if (mc.player.input.movementForward > 0) {
            mc.player.input.movementForward = 1;
        } else if (mc.player.input.movementForward < 0) {
            mc.player.input.movementForward = -1;
        }
    }


    //   Bounce Mode
    public void bounceMode() {
        mc.options.forwardKey.setPressed(true);

        IEntity player = (IEntity) mc.player;

        mc.options.jumpKey.setPressed(true);

        switch (alwaysPress.getValue()) {
            case "Sprint":
                if (!mc.player.isSprinting())
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
                mc.player.setSprinting(true);
                break;
            case "Shift":
                if (!mc.player.isSneaking())
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
                mc.player.setSneaking(true);
                break;
            case "Multi":
                if (!mc.player.isSprinting())
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
                mc.player.setSprinting(true);
                if (!mc.player.isSneaking())
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
                mc.player.setSneaking(true);
                break;
        }

        if (!player.invokeGetFlag(7)) {
            player.invokeSetFlag(7, true);
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        }

        mc.player.onGround = mc.player.verticalCollision;
        GrimUtils.sendPreActionGrimPackets(mc.player.getYaw(), mc.player.getPitch());
        GrimUtils.sendPreActionGrimPackets(mc.player.getYaw(), mc.player.getPitch());

        if (microBoost.getValue()) {
            if (PlayerUtils.getEntitySpeed(mc.player) < maxBoost.getValue()) {
                mc.player.velocity.x *= 1 + boostStrength.getValue();
                mc.player.velocity.z *= 1 + boostStrength.getValue();
            }
        }

        if (fireworkStart.getValue()) {
            if (!fireworkUsed) {
                if (PlayerUtils.getEntitySpeed(mc.player) < ignoreSpeed.getValue()) {
                    AutoFirework.useFirework(false);
                }
                fireworkUsed = true;
            }
        }

        if (grimFreeze.getValue()) {
            if (mc.player.verticalCollision) {
                GrimFreezeUtils.freeze(70);
            } else {
                GrimFreezeUtils.stopFreeze();
            }
        }
    }

    public void bouncePacket(PacketEvent.Send e) {
        if (e.getPacket() instanceof PlayerMoveC2SPacket packet) {
            packet.onGround = mc.player.verticalCollision;
            if (abusePitch.getValue()) {
                packet.pitch = bouncePitchRotate(packet.pitch);
                packet.yaw = bounceYawRotate(packet.yaw);
                if (Client.fireWorkManager.isUsingFireWork()) {
                    packet.pitch = (float) fireworkPitch.getValue();
                }
            }
        }
    }

    public float bouncePitchRotate(float standardValue) {
        if (ElytraFlight.abusePitch.getValue()) {
            float pitch = (float) (Client.fireWorkManager.isUsingFireWork() ? ElytraFlight.fireworkPitch.getValue() : ElytraFlight.pitch.getValue());
            if (strafing.getValue()) {
                if (KeyboardUtils.isKeyDown(mc.options.jumpKey.getDefaultKey().getCode())) pitch = Math.max(pitch - 25, -90);
                if (KeyboardUtils.isKeyDown(mc.options.sneakKey.getDefaultKey().getCode())) pitch = Math.min(pitch + 25, 90);
            }
            return pitch;
        }
        return standardValue;
    }

    public float bounceYawRotate(float standardValue) {
        if (strafing.getValue()) {
            return StrafeUtils.getPlayerDirectionOnKeybindings();
        }
        return standardValue;
    }
    /////////////////////


    //   Control Mode
    public void controlMode() {
        if (moveMode.getValue().equals("Lerp")) updateLerp();

        IEntity player = (IEntity) mc.player;

        if (!player.invokeGetFlag(7)) {
            player.invokeSetFlag(7, true);
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        }

        if (isMoving() || ignoreMovement.getValue()) {
            if (Client.fireWorkManager.timePassedFromLastFireWorkUse((int) fireworkDelay.getValue()) && !fireworkCheck) {
                AutoFirework.useFirework(false);
                fireworkCheck = true;
            }
        }
        if (Client.fireWorkManager.isUsingFireWork())
            fireworkCheck = false;
    }

    public void controlPacket(PacketEvent.Send e) {
        if (e.getPacket() instanceof PlayerMoveC2SPacket packet) {
            if (stopMode.getValue().equals("Glide") || stopMode.getValue().equals("Jitter")) {
                packet.yaw = isMoving() ? controlYawRotate(mc.player.yaw) : mc.player.age % 2 == 0 ? controlYawRotate(mc.player.yaw) : -controlYawRotate(mc.player.yaw);
                if (onGround.getValue() && !isMoving()) {
                    packet.onGround = true;
                }
            } else {
                packet.yaw = controlYawRotate(mc.player.yaw);
            }
            packet.pitch = controlPitchRotate(mc.player.pitch, true);

            //if (!isMoving() && stopMode.getValue().equals("Glide")) {
            //    e.setPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), packet.yaw, packet.pitch, mc.player.onGround));
            //}
        }
    }

    public float controlYawRotate(float standardValue) {
        switch (moveMode.getValue()) {
            case "Instance" -> standardValue = calcYaw();
            case "Lerp" -> standardValue = controlYaw;
        }
        return standardValue;
    }

    public float controlPitchRotate(float standardValue, boolean packet) {
        switch (moveMode.getValue()) {
            case "Instance" -> standardValue = calcPitch(packet);
            case "Lerp" -> standardValue = packet ? packetControlPitch  : controlPitch;
        }
        return standardValue;
    }

    public boolean isMoving() {
        if (allowStrafing.getValue()) {
            return mc.options.forwardKey.isPressed() || mc.options.backKey.isPressed() || mc.options.leftKey.isPressed() || mc.options.rightKey.isPressed();
        } else {
            return mc.options.forwardKey.isPressed();
        }
    }

    private float calcYaw() {
        float yaw = mc.player.yaw;
        if (allowStrafing.getValue()) {
            yaw = StrafeUtils.getPlayerDirectionOnKeybindings();
        }
        return yaw;
    }

    private float calcPitch(boolean packet) {
        float pitch = packet ? (float) packetPitch.getValue() : -0.8f;
        if (mc.options.sneakKey.isPressed()) {
            pitch += (float) downPitch.getValue();
        }
        if (mc.options.jumpKey.isPressed()) {
            pitch += (float) upPitch.getValue();
        }
        return pitch;
    }

    private void updateLerp() {
        if (controlYaw == Float.MIN_VALUE) controlYaw = mc.player.yaw;
        if (controlPitch == Float.MIN_VALUE) controlPitch = mc.player.pitch;
        controlYaw = MathHelper.lerp((float) lerpSpeed.getValue(), controlYaw, calcYaw());
        controlPitch = MathHelper.lerp((float) lerpSpeed.getValue(), controlPitch, calcPitch(false));
        packetControlPitch = MathHelper.lerp((float) lerpSpeed.getValue(), packetControlPitch, calcPitch(true));
    }
    /////////////////////
}
