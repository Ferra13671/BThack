package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.item.BowItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class FastBow extends Module {

    public static NumberSetting delay;

    public FastBow() {
        super("FastBow",
                "lang.module.FastBow",
                KeyboardUtils.RELEASE,
                Category.COMBAT,
                false
        );
                                             //2.14 is the smallest value the server can handle
        delay = new NumberSetting("Delay", this, 5.0, 2.14, 20.0, false);

        initSettings(delay);
    }

    @EventSubscriber
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.getInventory().getMainHandStack().getItem() instanceof BowItem && mc.player.isUsingItem() && (float)this.getItemInUseMaxCount() >= delay.getValue()) {
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
            mc.player.clearActiveItem();
        }
    }

    public int getItemInUseMaxCount() {
        return mc.player.isUsingItem() ? mc.player.activeItemStack.getMaxUseTime() - mc.player.itemUseTimeLeft : 0;
    }
}
