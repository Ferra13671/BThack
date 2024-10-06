package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Events.Events.Block.AttackBlockEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemsUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Set;

public class AutoTool extends Module {
    public AutoTool() {
        super("AutoTool",
                "lang.module.AutoTool",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );
    }

    @EventSubscriber(priority = Integer.MIN_VALUE)
    public void onLeftClick(AttackBlockEvent e) {
        if (nullCheck() || e.isCancelled()) return;

        equipBestSlot(mc.world.getBlockState(e.getBlockPos()));
    }



    public static void equipBestSlot(BlockState blockState) {
        MinecraftClient mc = MinecraftClient.getInstance();

        double bestScore = -1;
        int bestSlot = -1;

        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);

            double score = ItemsUtils.getScore(itemStack, blockState, itemStack2 -> true);
            if (score < 0) continue;

            if (score > bestScore) {
                bestScore = score;
                bestSlot = i;
            }
        }

        if (bestScore != -1 && bestSlot != -1) {
            if (bestSlot < 9) {
                InventoryUtils.swapItem(bestSlot);
            } else {
                for (int a = 0; a < 9; a++) {
                    if (mc.player.getInventory().getStack(a).getItem() == Items.AIR) {
                        InventoryUtils.swapItemOnInventory(a, bestSlot);
                        InventoryUtils.swapItem(a);
                        return;
                    }
                }
                InventoryUtils.swapItemOnInventory(mc.player.getInventory().selectedSlot, bestSlot);
                mc.interactionManager.tick();
            }
        }
    }

    public static int getBestSlot(BlockState blockState, int slots) {
        MinecraftClient mc = MinecraftClient.getInstance();

        double bestScore = -1;
        int bestSlot = -1;

        for (int i = 0; i < slots; i++) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);

            double score = ItemsUtils.getScore(itemStack, blockState, itemStack2 -> true);
            if (score < 0) continue;

            if (score > bestScore) {
                bestScore = score;
                bestSlot = i;
            }
        }

        if (bestScore != -1 && bestSlot != -1) {
            return bestSlot;
        }
        return -1;
    }

    public static final Set<Block> ignoreBlocks = Sets.newHashSet(
            Blocks.BEDROCK,
            Blocks.AIR,
            Blocks.FLOWER_POT,
            Blocks.END_PORTAL,
            Blocks.END_PORTAL_FRAME
    );
}
