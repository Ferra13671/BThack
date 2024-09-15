package com.ferra13671.BThack.impl.Modules.COMBAT.KillAura;

import com.ferra13671.BThack.Events.Events.Render.RenderWorldEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;

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
    public static ModeSetting target;


    private AuraThread aura = new AuraThread();
    private TriggerBotThread triggerBot = new TriggerBotThread();

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
        target = ClansUtils.getClanTargetSetting(this, clanManager, clanMode);

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
                target
        );
    }

    @EventSubscriber
    public void onUpdate(RenderWorldEvent.End e) {
        if (nullCheck()) return;

        String killAuraMode = mode.getValue();

        arrayListInfo = killAuraMode + (!killAuraMode.equals("TriggerBot") ? "; " + range.getValue() : "");

        if (mc.player != null) {
            switch (killAuraMode) {
                case "Aura":
                    if (!aura.isAlive()) {
                        aura = new AuraThread();
                        aura.start();
                    }
                    break;
                case "TriggerBot":
                    if (!triggerBot.isAlive()) {
                        triggerBot = new TriggerBotThread();
                        triggerBot.start();
                    }
                    break;
            }
        }
    }
}