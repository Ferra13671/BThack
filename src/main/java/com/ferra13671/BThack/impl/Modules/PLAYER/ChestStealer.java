package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Player.PlayerUtils;
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
                Category.PLAYER,
                false
        );

        stealDelay = new NumberSetting("Steal Delay", this, 100,0,1000,true);

        initSettings(
                stealDelay
        );
    }


    @EventSubscriber
    public void onUpdate(TickEvent.ClientTickEvent e) {
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
                            if (mc.player.currentScreenHandler instanceof GenericContainerScreenHandler && Client.getModuleByName("ChestStealer").isEnabled()) {
                                if (container.getInventory().getStack(index).getItem() != Items.AIR) {
                                    pc.clickSlot(container.syncId, index, 0, SlotActionType.QUICK_MOVE, mc.player);
                                    try {
                                        thread.sleep((long) stealDelay.getValue());
                                    } catch (InterruptedException ignored) {}
                                }

                                if (container.getInventory().isEmpty()) {
                                    pc.closeScreen();
                                    ChatUtils.sendMessage("Closed");
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
