package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Events.IsNormalCubeEvent;
import com.ferra13671.BThack.Events.SetOpaqueCubeEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.BlockState;

public class Xray extends Module {

    public static boolean doXray;

    public Xray() {
        super("Xray",
                "lang.module.Xray",
                KeyboardUtils.RELEASE,
                Category.RENDER,
                false
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        super.onEnable();

        doXray = true;

        mc.worldRenderer.reload();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (nullCheck()) return;

        doXray = false;

        mc.worldRenderer.reload();
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onSetOpaque(SetOpaqueCubeEvent e) {
        e.setCancelled(true);
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onIsNormalCube(IsNormalCubeEvent e) {
        if (Xray.doXray) {
            BlockState state = mc.world.getBlockState(e.pos);
            if (state != null) {
                if (Client.blockLists.get("Xray").blocks.contains(state.getBlock())) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
