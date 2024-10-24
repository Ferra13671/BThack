package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemsUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.TntBlock;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public class TNTIgniter extends Module {
    
    public static ModeSetting swap;
    public static BooleanSetting allowInventory;
    
    public TNTIgniter() {
        super("TNTIgniter",
                "lang.module.TNTIgniter",
                KeyboardUtils.RELEASE,
                Category.COMBAT,
                false
        );
        
        swap = new ModeSetting("Swap", this, Arrays.asList("Client", "Packet"));
        allowInventory = new BooleanSetting("Allow Inventory", this, true);
        
        initSettings(
                swap,
                allowInventory
        );
    }
    
    @EventSubscriber
    public void onTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        List<BlockPos> poses = BlockUtils.getAllInBox(mc.player.getBlockPos(), 4).stream().
                filter(pos -> mc.world.getBlockState(pos).getBlock() instanceof TntBlock).toList();
        
        for (BlockPos pos : poses) {
            int slot = InventoryUtils.findItem(Items.FLINT_AND_STEEL, allowInventory.getValue() ? 36 : 9);
            if (slot == -1) return;
            int oldSlot = mc.player.getInventory().selectedSlot;
            
            swapAction(oldSlot, slot, false);
            ItemsUtils.useItemOnBlock(pos.add(0, 1, 0));
            swapAction(oldSlot, slot, true);
        }
    }

    public void swapAction(int oldSlot, int slot, boolean post) {
        if (!post && oldSlot == slot) return;

        switch (swap.getValue()) {
            case "Client" -> {
                if (slot < 9) {
                    InventoryUtils.swapItem((post ? oldSlot : slot));
                } else {
                    InventoryUtils.swapItemOnInventory(oldSlot, slot);
                }
            }
            case "Packet" -> {
                if (slot < 9)
                    InventoryUtils.packetSwapItem((post ? oldSlot : slot));
                else
                    InventoryUtils.swapItemOnInventory(oldSlot, slot);
            }
        }
    }
}
