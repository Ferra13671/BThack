package com.ferra13671.BThack.impl.Modules.PLAYER;


import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemsUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoFirework extends Module {

    public static BooleanSetting swingHand;

    public AutoFirework() {
        super("AutoFirework",
                "lang.module.AutoFirework",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );

        swingHand = new BooleanSetting("Swing Hand", this, true);

        initSettings(
                swingHand
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        useFirework(swingHand.getValue());
        toggle();
    }

    public static void useFirework(boolean swing) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && mc.player.isFallFlying()) {
            int oldSlot = mc.player.getInventory().selectedSlot;
            int inventorySlot = InventoryUtils.findItem(Items.FIREWORK_ROCKET);
            if (inventorySlot != -1) {
                if (inventorySlot < 9)
                    InventoryUtils.swapItem(inventorySlot);
                else
                    InventoryUtils.swapItemOnInventory(oldSlot, inventorySlot);
                ItemsUtils.useItem(Hand.MAIN_HAND, swing);
                if (inventorySlot < 9)
                    InventoryUtils.swapItem(oldSlot);
                else
                    InventoryUtils.swapItemOnInventory(oldSlot, inventorySlot);
            }
        }
    }
}
