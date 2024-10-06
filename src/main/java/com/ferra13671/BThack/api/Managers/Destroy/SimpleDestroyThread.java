package com.ferra13671.BThack.api.Managers.Destroy;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Interfaces.Pc;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.impl.Modules.MISC.PacketMine.PacketMine;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoTool;
import net.minecraft.util.math.BlockPos;

public class SimpleDestroyThread extends Thread implements Mc, Pc {
    BlockPos pos;

    public SimpleDestroyThread(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void run() {
        if (mc.player == null || mc.world == null) {
            DestroyManager.isDestroying = false;
            return;
        }

        DestroyManager.isDestroying = true;

        float oldYaw = mc.player.yaw;
        float oldPitch = mc.player.pitch;

        if (Client.getModuleByName("PacketMine").isEnabled()) {
            pc.startBlockBreaking(pos, AimBotUtils.getInvertedFacingEntity(mc.player));
            Thread.yield();
        }

        PacketMine packetMine = (PacketMine) Client.getModuleByName("PacketMine");

        if (DestroyManager.isNeedToBreak(pos)) {
            if (packetMine.isEnabled()) {
                packetMine.updateBlock(pos);
                Thread.yield();
            }

            while (DestroyManager.isNeedToBreak(pos)) {
                if (packetMine.isEnabled()) {
                    if (!BuildManager.isPossibleRich(pos)) break;
                    if (packetMine.currentBreakingBlock == null)
                        packetMine.updateBlock(pos);
                    DestroyManager.delay(50, this);
                } else {
                    if (BuildManager.isPossibleRich(pos)) {
                        mc.player.yaw = AimBotUtils.rotations(pos)[0];
                        mc.player.pitch = AimBotUtils.rotations(pos)[1];
                        AutoTool.equipBestSlot(mc.world.getBlockState(pos));

                        DestroyManager.currentBlockPos = pos;

                        DestroyManager.delay(50, this);
                    } else {
                        break;
                    }
                }
            }

            if (!packetMine.isEnabled()) {
                mc.player.yaw = oldYaw;
                mc.player.pitch = oldPitch;
            }
        }

        DestroyManager.currentBlockPos = null;

        mc.interactionManager.cancelBlockBreaking();

        DestroyManager.isDestroying = false;
    }
}
