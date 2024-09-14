package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;

public class ElytraSwap extends Module {

    public ElytraSwap() {
        super("ElytraSwap",
                "lang.module.ElytraSwap",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        boolean finded = false;
        Item armor = mc.player.getInventory().getArmorStack(2).getItem();
        if (armor instanceof ElytraItem) {
            for (int needSlot = 0; needSlot < 36; needSlot++) {
                if (mc.player.getInventory().getStack(needSlot).getItem() instanceof ArmorItem armorItem) {
                    if (armorItem.getSlotType() == EquipmentSlot.CHEST) {
                        int item;
                        if (needSlot < 9)
                            item = needSlot + 36;
                        else
                            item = needSlot;

                        InventoryUtils.replaceItems(InventoryUtils.CHESTPLATE_SLOT, item, 0);
                        finded = true;
                        toggle();
                        break;
                    }
                }
            }
        } else {
            for (int needSlot = 0; needSlot < 36; needSlot++) {
                if (mc.player.getInventory().getStack(needSlot).getItem() instanceof ElytraItem) {
                    int item;
                    if (needSlot < 9)
                        item = needSlot + 36;
                    else
                        item = needSlot;

                    InventoryUtils.replaceItems(InventoryUtils.CHESTPLATE_SLOT, item, 0);
                    finded = true;
                    toggle();
                    break;
                }
            }
        }
        if (!finded) {
            toggle();
        }
    }
}
