package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Build.BuildThread3D;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Arrays;

public class Scaffold extends Module {

    public Scaffold() {
        super("Scaffold",
                "lang.module.Scaffold",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );
    }

    @EventSubscriber
    public void onTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        action();
    }

    public void action() {
        if (!BuildManager.pickUpPlaceBlocks(false) || BuildManager.isBuilding) return;


        BlockPos blockPos = BlockPos.ofFloored(mc.player.getX(), mc.player.getY() - 1, mc.player.getZ());
        if (!mc.world.getBlockState(blockPos).isReplaceable()) return;

        BuildThread3D thread3D = new BuildThread3D();
        thread3D.set3DSchematic(0 ,Arrays.asList(new Vec3i(0,0,0)), blockPos);
        thread3D.start();
    }
}
