package com.ferra13671.BThack.impl.Modules.PLAYER;


import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemsUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoPearl extends Module {
    int oldSlot = -1;

    public AutoPearl() {

        super("AutoPearl",
                "lang.module.AutoPearl",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );

    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        if (mc.player != null) {
            oldSlot = mc.player.getInventory().selectedSlot;
            int inventorySlot = InventoryUtils.findItem(Items.ENDER_PEARL);
            if (inventorySlot != -1) {
                if (inventorySlot < 9)
                    InventoryUtils.swapItem(inventorySlot);
                else
                    InventoryUtils.swapItemOnInventory(oldSlot, inventorySlot);
                ItemsUtils.useItem(Hand.MAIN_HAND);
                if (inventorySlot < 9)
                    InventoryUtils.swapItem(oldSlot);
                else
                    InventoryUtils.swapItemOnInventory(oldSlot, inventorySlot);
            }
            toggle();
        }
    }
}
