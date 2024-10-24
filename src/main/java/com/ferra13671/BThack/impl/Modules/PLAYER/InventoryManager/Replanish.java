package com.ferra13671.BThack.impl.Modules.PLAYER.InventoryManager;

import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Interfaces.Pc;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Ticker;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Ferra13671 and Nikitadan4pi
 */

public class Replanish implements Pc, Mc {



    public Replanish(Module module) {

        InventoryManager.count = new NumberSetting("Item threshold", module, 32,1,63, false, () -> InventoryManager.page.getValue().equals("Replanish"));

        InventoryManager.delayMode = new ModeSetting("Delay Mode", module, Arrays.asList("None", "Ms"), () -> InventoryManager.page.getValue().equals("Replanish"));
        InventoryManager.delay = new NumberSetting("Delay", module, 500, 100, 1000, true, () -> InventoryManager.delayMode.getValue().equals("Ms") && InventoryManager.page.getValue().equals("Replanish"));

        
    }

    private final Ticker delayTicker = new Ticker();
    private final ArrayList<ItemInfo> itemInfos = new ArrayList<>();

    public void onEnable() {
        delayTicker.reset();
    }

    public void onTick(TickEvent.ClientTickEvent e) {

        if (InventoryManager.delayMode.getValue().equals("Ms")) {
            if (delayTicker.passed(InventoryManager.delay.getValue())) {
                findAction();
                action();
                delayTicker.reset();
            }
        } else {
            findAction();
            action();
        }
    }

    public void action() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isEmpty() || !stack.isStackable()) {
                continue;
            }
            if (stack.getCount() == 1 || stack.getCount() <= InventoryManager.count.getValue()) {
                for (ItemInfo itemInfo : itemInfos) {
                    if (itemInfo.item == stack.getItem() && itemInfo.slot != i) {
                        int slotId = itemInfo.slot < 9 ? itemInfo.slot + 36 : itemInfo.slot;
                        pc.clickSlot(0, slotId, 0, SlotActionType.PICKUP, mc.player);
                        pc.tick();
                        pc.clickSlot(0, i + 36, 0, SlotActionType.PICKUP, mc.player);
                        pc.tick();
                        pc.clickSlot(0, slotId, 0, SlotActionType.PICKUP, mc.player);
                        pc.tick();
                        return;
                    }
                }
            }
        }
    }

    public void findAction() {
        itemInfos.clear();
        for (int i = 9; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack != null && !stack.isEmpty()) {
                itemInfos.add(new ItemInfo(stack.getItem(), i));
            } else {
                itemInfos.add(new ItemInfo(Items.AIR, i));
            }
        }
    }

    private record ItemInfo(Item item, int slot) {}
}