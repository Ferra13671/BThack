package com.ferra13671.BThack.api.Utils;

import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;

public final class ItemsUtils implements Mc {
    public static void useItem(Hand hand) {
        mc.player.swingHand(hand);
        mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(hand, 0));
    }

    public static void useItemNoSwing(Hand hand) {
        mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(hand, 0));
    }

    public static int getBlock() {
        for(int i = 0; i < 9; ++i) {
            if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem) {
                return i;
            }
        }
        return -1;
    }

    public static int getItemDurability(ItemStack itemStack) {
        return itemStack.getMaxDamage() - itemStack.getDamage();
    }

    public static int getItemMaxDurability(ItemStack itemStack) {
        return itemStack.getMaxDamage();
    }

    public static float getItemDurabilityInPercentages(ItemStack itemStack) {
        return 100f - (((float)itemStack.getDamage() / (float)itemStack.getMaxDamage()) * 100f);
    }
}
