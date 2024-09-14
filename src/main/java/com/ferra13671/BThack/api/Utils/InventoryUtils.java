package com.ferra13671.BThack.api.Utils;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public final class InventoryUtils implements Mc {

    public static final int HEAD_SLOT = 5;
    public static final int CHESTPLATE_SLOT = 6;
    public static final int LEGS_SLOT = 7;
    public static final int FEET_SLOT = 8;
    public static final int OFFHAND_SLOT = 45;

    public static void swapItem(int needSlot) {
        mc.player.getInventory().selectedSlot = needSlot;
        mc.interactionManager.tick();
    }

    public static void swapItemOnInventory(int needHotbarSlot ,int inventorySlot) {
        mc.interactionManager.tick();
        mc.interactionManager.clickSlot(0, inventorySlot, needHotbarSlot, SlotActionType.SWAP, mc.player);
        mc.interactionManager.tick();
    }

    public static int findItem(ItemStack stack) {
        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getStack(i) == stack) {
                return i;
            }
        }
        return -1;
    }

    public static int findItem(Item item) {
        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    public static ItemStack getItem(int hotbarSlot) {
        return mc.player.getInventory().getStack(hotbarSlot);
    }

    public static void replaceItems(int slot1, int slot2, int delay) {
        ThreadManager.startNewThread(thread -> {
            mc.interactionManager.clickSlot(0, slot1, 1, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.tick();
            if (delay > 0) {
                try {
                    thread.sleep(delay);
                } catch (InterruptedException ignored) {}
            }
            mc.interactionManager.clickSlot(0, slot2, 1, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.tick();
            if (delay > 0) {
                try {
                    thread.sleep(delay);
                } catch (InterruptedException ignored) {}
            }
            mc.interactionManager.clickSlot(0, slot1, 1, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.tick();
        });
    }
}
