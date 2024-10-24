package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BTbot.api.Utils.Generate.GenerateNumber;
import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class ItemRandomizer extends Module {

    public ItemRandomizer() {
        super("ItemRandomizer",
                "lang.module.ItemRandomizer",
                KeyboardUtils.RELEASE,
                Category.MISC,
                false
        );
    }

    @EventSubscriber
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;


        InventoryUtils.swapItem(GenerateNumber.generateInt(0, 8));
    }
}
