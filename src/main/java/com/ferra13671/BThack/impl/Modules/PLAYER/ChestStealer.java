package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class ChestStealer extends Module {
    public static boolean active = false;

    public static NumberSetting stealDelay;

    public ChestStealer() {
        super("ChestStealer",
                "lang.module.ChestStealer",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        stealDelay = new NumberSetting("Steal Delay", this, 100,0,1000,true);

        initSettings(
                stealDelay
        );
    }


    @EventSubscriber
    public void onUpdate(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.currentScreenHandler instanceof GenericContainerScreenHandler) {
            if (!active) {
                ThreadManager.startNewThread(thread -> {
                    if (mc.player.currentScreenHandler instanceof GenericContainerScreenHandler container) {
                        if (container.getInventory().isEmpty()) {
                            while (mc.currentScreen instanceof GenericContainerScreen) {
                                try {
                                    thread.sleep(100);
                                } catch (InterruptedException ignored) {}
                            }
                            ChestStealer.active = false;
                            thread.stop();
                        }
                        for (int index = 0; index < container.slots.size(); ++index) {
                            if (mc.player.currentScreenHandler instanceof GenericContainerScreenHandler && ModuleList.chestStealer.isEnabled()) {
                                if (container.getInventory().getStack(index).getItem() != Items.AIR) {
                                    pc.clickSlot(container.syncId, index, 0, SlotActionType.QUICK_MOVE);
                                    try {
                                        thread.sleep((long) stealDelay.getValue());
                                    } catch (InterruptedException ignored) {}
                                }

                                if (container.getInventory().isEmpty()) {
                                    pc.closeScreen();
                                    ChestStealer.active = false;
                                    break;
                                }
                            }
                        }
                    }
                });
                active = true;
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        active = false;
    }
}
