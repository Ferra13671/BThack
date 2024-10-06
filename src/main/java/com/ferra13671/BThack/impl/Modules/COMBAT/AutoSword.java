package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.Events.Events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.AutoSwordUtils;
import net.minecraft.entity.decoration.EndCrystalEntity;

public class AutoSword extends Module {
    public AutoSword() {
        super("AutoSword",
                "lang.module.AutoSword",
                KeyboardUtils.RELEASE,
                Category.COMBAT,
                false
        );
    }

    @EventSubscriber
    public void onPacket(AttackEntityEvent e) {
        if (e.getEntity() instanceof EndCrystalEntity || e.getPlayer() != mc.player) return;

        int inventorySlot = AutoSwordUtils.getSword();
        if (inventorySlot != -1) {
            InventoryUtils.swapItem(inventorySlot);
        }
    }
}
