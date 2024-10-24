package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.Events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Friends.FriendsUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class NoFriendDamage extends Module {
    public NoFriendDamage() {
        super("NoFriendDamage",
                "lang.module.NoFriendDamage",
                KeyboardUtils.RELEASE,
                Category.COMBAT,
                false
        );
    }

    @EventSubscriber
    public void onPacket(AttackEntityEvent e) {
        if (nullCheck()) return;

        if (FriendsUtils.isFriend(e.getEntity().getDisplayName().getString())) {
            e.setCancelled(true);
        }
    }
}
