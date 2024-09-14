package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Events.Events.IsNormalCubeEvent;
import com.ferra13671.BThack.Events.Events.SetOpaqueCubeEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.HashSet;

public class Xray extends Module {

    public static boolean doXray;
    public static HashSet<Block> xrayBlocks = new HashSet<>();
    public static HashSet<String> xrayBlockNames = new HashSet<>();

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
    public void onSetOpaque(SetOpaqueCubeEvent e) {
        e.setCancelled(true);
    }

    @EventSubscriber
    public void onIsNormalCube(IsNormalCubeEvent e) {
        if (Xray.doXray) {
            BlockState state = mc.world.getBlockState(e.pos);
            if (state != null) {
                if (Xray.xrayBlocks.contains(state.getBlock())) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
