package com.ferra13671.BThack.impl.Modules.COMBAT.KillAura;

import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.ArrayList;
import java.util.Arrays;

public class KillAura extends Module {

    public static ModeSetting mode;
    public static ModeSetting attackMode;
    public static NumberSetting range;
    public static ModeSetting rotateMode;
    public static NumberSetting packets;
    public static NumberSetting delay;
    public static NumberSetting postCooldown;

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
                Category.COMBAT,
                false
        );

        mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Aura", "TriggerBot")));
        attackMode = new ModeSetting("AttackMode", this, new ArrayList<>(Arrays.asList("CoolDown", "Delay")));
        range = new NumberSetting("Range", this, 4.0, 1, 10, false, () -> mode.getValue().equals("Aura"));
        rotateMode = new ModeSetting("RotateMode", this, new ArrayList<>(Arrays.asList("Packet", "Vanilla", "Grim", "None")), () -> !mode.getValue().equals("TriggerBot"));
        packets = new NumberSetting("Packets", this, 1, 1, 5, true, () -> rotateMode.getValue().equals("Packet") && mode.getValue().equals("Aura"));
        delay = new NumberSetting("Delay(Second)", this, 1.4, 0.1, 4, false, () -> attackMode.getValue().equals("Delay"));
        postCooldown = new NumberSetting("Post Cooldown", this, 0, 0, 100, true, () -> attackMode.getValue().equals("CoolDown"));
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

    @Override
    public void onEnable() {
        super.onEnable();
        delayTicker.reset();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        delayTicker.reset();
    }

    @EventSubscriber
    public void onTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        String killAuraMode = mode.getValue();

        arrayListInfo = killAuraMode + (!killAuraMode.equals("TriggerBot") ? "; " + range.getValue() : "");

        switch (killAuraMode) {
            case "Aura" -> auraMode();
            case "TriggerBot" -> triggerBotMode();
        }
    }


    //---------Aura---------//
    public void auraMode() {
        PlayerEntity target = KillAuraUtils.filterPlayers(range.getValue(), friends.getValue(), teammates.getValue(), clanManager.getValue(), clanMode.getValue(), KillAura.targetClan.getValue());
        Entity entity = KillAuraUtils.filterEntity(range.getValue());

        if (players.getValue()) {
            if (auraAttack(target)) return;
        }

        if (mobs.getValue()) {
            if (entity instanceof LivingEntity livingEntity)
                auraAttack(livingEntity);
        }
    }

    public boolean auraAttack(LivingEntity entity) {
        if (entity != null && KillAuraUtils.canBeSeeTarget(ignoreWalls, entity) && entity.isAlive()) {
            RotateMode rotateMode = getRotateMode();
            int packets = (int) KillAura.packets.getValue();
            attackInternal(entity, rotateMode, packets);
            return true;
        }
        return false;
    }
    //----------------------//

    //---------TriggerBot---------//
    public void triggerBotMode() {
        HitResult objectMouseOver = mc.crosshairTarget;

        if (objectMouseOver instanceof EntityHitResult entityHitResult) {
            Entity ent = entityHitResult.getEntity();

            if (KillAura.players.getValue() && ent instanceof PlayerEntity player) {
                if (KillAuraUtils.filterPlayer(player, friends.getValue(), teammates.getValue(), clanManager.getValue(), clanMode.getValue(), targetClan.getValue())) {
                    attackInternal(ent, RotateMode.NONE, 0);
                }
            }
            if (KillAura.mobs.getValue()) {
                if (KillAuraUtils.isCurrentMob(ent) && ent.isAlive()) {
                    attackInternal(ent, RotateMode.NONE, 0);
                }
            }
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

    public void attackInternal(Entity entity, RotateMode rotateMode, int packets) {
        switch (KillAura.attackMode.getValue()) {
            case "CoolDown" ->
                ThreadManager.startNewThread(thread ->
                        KillAuraUtils.CoolDownAttack(entity, thread, (int) KillAura.postCooldown.getValue(), rotateMode, 0)
                );
            case "Delay" -> {
                if (delayTicker.passed((int) (delay.getValue() * 1000))) {
                    KillAuraUtils.attack(entity, rotateMode, packets);
                    delayTicker.reset();
                }
            }
        }
    }
}