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

public class Introvert extends Module {

    public static NumberSetting range;
    public static BooleanSetting friends;

    public Introvert() {
        super("Introvert",
                "lang.module.Introvert",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );

        range = new NumberSetting("Range", this, 7, 4, 15, false);
        friends = new BooleanSetting("Friends", this, false);

        initSettings(
                range,
                friends
        );
    }

    @EventSubscriber
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        arrayListInfo = "" + range.getValue();

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;

            if (player.distanceTo(mc.player) < (float) range.getValue()) {
                if (friends.getValue()) {
                    mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.of("[Introvert] You were disconnected because a player was detected near you.")));
                } else {
                    if (!FriendsUtils.isFriend(player)) {
                        mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.of("[Introvert] You were disconnected because a player was detected near you.")));
                    }
                }
            }
        }
    }
}
