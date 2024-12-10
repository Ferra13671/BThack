package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.Text;

/**
 * @author Ferra13671 and Nikitadan4pi
 */

public class AutoDisconnect extends Module {

    public static NumberSetting minHealth;
    public static BooleanSetting autoToggle;

    public static BooleanSetting introvert;
    public static NumberSetting range;
    public static BooleanSetting friends;

    public static BooleanSetting ifHeight;
    public static NumberSetting minHeight;

    public AutoDisconnect() {
        super("AutoDisconnect",
                "lang.module.AutoDisconnect",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        minHealth = new NumberSetting("Min Health", this, 10,1,20,true);
        autoToggle = new BooleanSetting("Auto Toggle", this, true);

        introvert = new BooleanSetting("Introvert", this,false);
        range = new NumberSetting("Range", this, 7, 4, 15, false, introvert::getValue);
        friends = new BooleanSetting("Friends", this, false, introvert::getValue);

        ifHeight = new BooleanSetting("If Height", this, false);
        minHeight = new NumberSetting("Height", this, 30, 0, 300, false, ifHeight::getValue);

        initSettings(
                minHealth,
                autoToggle,

                introvert,
                range,
                friends,

                ifHeight,
                minHeight
        );
    }

    @EventSubscriber
    public void onPlayerTick(ClientTickEvent e) {
        if (nullCheck()) return;

        autoDisconnectAction();

        if (introvert.getValue()) {
            introvertAction();
        }

        if (ifHeight.getValue()) {
            ifHeightAction();
        }
    }

    public void autoDisconnectAction() {
        float playerHP = mc.player.getHealth();
        double minHP = minHealth.getValue();
        if (playerHP <= minHP) {
            mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.of("Your health has reached its minimum limit(" + minHP + "). You have been disconnected.  Your HP: " + playerHP)));
            if (autoToggle.getValue()) {
                toggle();
            }
        }
    }

    public void introvertAction() {
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;

            if (player.distanceTo(mc.player) < (float) range.getValue()) {
                if (friends.getValue()) {
                    mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.of(getChatName() + " You were disconnected because a player was detected near you.")));
                    if (autoToggle.getValue()) {
                        toggle();
                    }
                } else {
                    if (!SocialManagers.FRIENDS.contains(player)) {
                        mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.of(getChatName() + " You were disconnected because a player was detected near you.")));
                        if (autoToggle.getValue()) {
                            toggle();
                        }
                    }
                }
            }
        }
    }

    public void  ifHeightAction() {
        if (mc.player.getY() < minHeight.getValue()) {
            mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.of(String.format("Your position on Y was less than %s. You've been disconnected.", minHeight.getValue()))));
            if (autoToggle.getValue()) {
                toggle();
            }
        }
    }
}