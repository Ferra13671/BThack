package com.ferra13671.BThack.api.Managers.Destroy;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoTool;
import net.minecraft.util.math.BlockPos;

public class SimpleDestroyThread extends AbstractDestroyThread {
    BlockPos pos;

    public SimpleDestroyThread(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    protected void destroyAction() {
        if (mc.player == null || mc.world == null) {
            DestroyManager.isDestroying = false;
            return;
        }

        DestroyManager.isDestroying = true;

        //float oldYaw = mc.player.yaw;
        //float oldPitch = mc.player.pitch;

        if (ModuleList.packetMine.isEnabled()) {
            pc.startBlockBreaking(pos, AimBotUtils.getInvertedFacingEntity(mc.player));
            Thread.yield();
        }

        if (BlockUtils.canBreak(pos)) {
            if (ModuleList.packetMine.isEnabled()) {
                ModuleList.packetMine.updateBlock(pos);
                Thread.yield();
            }

            while (BlockUtils.canBreak(pos)) {
                if (ModuleList.packetMine.isEnabled()) {
                    if (!BuildManager.isPossibleRich(pos)) break;
                    if (ModuleList.packetMine.currentBreakingBlock == null)
                        ModuleList.packetMine.updateBlock(pos);
                    DestroyManager.delay(50, this);
                } else {
                    if (BuildManager.isPossibleRich(pos)) {
                        //mc.player.yaw = AimBotUtils.rotations(pos)[0];
                        //mc.player.pitch = AimBotUtils.rotations(pos)[1];
                        AutoTool.equipBestSlot(mc.world.getBlockState(pos));

                        DestroyManager.currentBlockPos = pos;

                        DestroyManager.delay(50, this);
                    } else {
                        break;
                    }
                }
            }

            /*
            if (!packetMine.isEnabled()) {
                mc.player.yaw = oldYaw;
                mc.player.pitch = oldPitch;
            }

             */
        }
    }
}
