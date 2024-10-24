package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Friends.FriendsUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
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

    public AutoDisconnect() {
        super("AutoDisconnect",
                "lang.module.AutoDisconnect",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );

        minHealth = new NumberSetting("Min Health", this, 10,1,20,true);
        autoToggle = new BooleanSetting("AutoToggle", this, true);

        introvert = new BooleanSetting("Introvert", this,false);
        range = new NumberSetting("Range", this, 7, 4, 15, false, introvert::getValue);
        friends = new BooleanSetting("Friends", this, false, introvert::getValue);

        initSettings(
                minHealth,
                autoToggle,

                introvert,
                range,
                friends
        );
    }

    @EventSubscriber
    public void onPlayerTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player != null) {
            float playerHP = mc.player.getHealth();
            double minHP = minHealth.getValue();
            if (playerHP <= minHP) {
                mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.of("Your health has reached its minimum limit(" + minHP + "). You have been disconnected.  Your HP: " + playerHP)));
                if (autoToggle.getValue()) {
                    toggle();
                }
            }
        }

        if (introvert.getValue()) {
            for (PlayerEntity player : mc.world.getPlayers()) {
                if (player == mc.player) continue;

                if (player.distanceTo(mc.player) < (float) range.getValue()) {
                    if (friends.getValue()) {
                        mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.of(getChatName() + " You were disconnected because a player was detected near you.")));
                        if (autoToggle.getValue()) {
                            toggle();
                        }
                    } else {
                        if (!FriendsUtils.isFriend(player)) {
                            mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.of(getChatName() + " You were disconnected because a player was detected near you.")));
                            if (autoToggle.getValue()) {
                                toggle();
                            }
                        }
                    }
                }
            }
        }
    }
}