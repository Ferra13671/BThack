package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemsUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ElytraReplace extends Module {

    public static NumberSetting minDurability;

    public ElytraReplace() {
        super("ElytraReplace",
                "lang.module.ElytraReplace",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );

        minDurability = new NumberSetting("Min Durability", this, 15, 1, 100, true);

        initSettings(
                minDurability
        );
    }

    boolean needReplace = true;

    @EventSubscriber
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        ItemStack stack = mc.player.getInventory().getArmorStack(2);

        if (stack.getItem() == Items.ELYTRA) {
            if (ItemsUtils.getItemDurability(stack) < minDurability.getValue()) {
                if (!needReplace) return;
                for (int i = 0; i < 36; i++) {
                    ItemStack item = mc.player.getInventory().getStack(i);

                    if (item.getItem() == Items.ELYTRA) {
                        int durability = ItemsUtils.getItemDurability(item);

                        if (durability > minDurability.getValue()) {
                            int needItem = i;

                            if (needItem < 9) {
                                needItem = needItem + 36;
                            }

                            InventoryUtils.replaceItems(6, needItem, 50);
                            needReplace = false;
                        }
                    }
                }
            } else {
                needReplace = true;
            }
        }
    }
}
