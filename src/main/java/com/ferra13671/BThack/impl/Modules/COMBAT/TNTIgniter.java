package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.TntBlock;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public class TNTIgniter extends Module {

    public static NumberSetting range;
    
    public static ModeSetting swap;
    public static BooleanSetting allowInventory;
    
    public TNTIgniter() {
        super("TNTIgniter",
                "lang.module.TNTIgniter",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        range = new NumberSetting("Range", this, 4, 4, 10, true);
        
        swap = new ModeSetting("Swap", this, Arrays.asList("Client", "Packet"));
        allowInventory = new BooleanSetting("Allow Inventory", this, true);
        
        initSettings(
                range,

                swap,
                allowInventory
        );
    }
    
    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        List<BlockPos> poses = BlockUtils.getAllInBox(mc.player.getBlockPos(), (int) range.getValue()).stream().
                filter(pos -> mc.world.getBlockState(pos).getBlock() instanceof TntBlock).toList();
        
        for (BlockPos pos : poses) {
            int slot = InventoryUtils.findItem(Items.FLINT_AND_STEEL, allowInventory.getValue() ? 36 : 9);
            if (slot == -1) return;
            int oldSlot = mc.player.getInventory().selectedSlot;

            InventoryUtils.swapAction(oldSlot, slot, false, swap.getValue());
            ItemUtils.useItemOnBlock(pos.add(0, 1, 0));
            InventoryUtils.swapAction(oldSlot, slot, true, swap.getValue());
        }
    }
}
