package com.ferra13671.BThack.api.Managers.Destroy;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Interfaces.Pc;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import com.ferra13671.BThack.impl.Modules.MISC.PacketMine.PacketMine;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoTool;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

public class DestroyThread3D extends Thread implements Mc, Pc {
    private ArrayList<Vec3d> schematic;
    private BlockPos startPos;
    private List<Block> ignoreBlocks = new ArrayList<>();

    public void set3DSchematic(ArrayList<Vec3d> sch, BlockPos startPos) {
        schematic = sch;
        this.startPos = startPos;
    }

    public void set3DSchematic(List<Vec3i> sch, BlockPos startPos) {
        ArrayList<Vec3d> _sch = new ArrayList<>();
        for (Vec3i vec3i : sch) {
            _sch.add(new Vec3d(vec3i.getX(), vec3i.getY(), vec3i.getZ()));
        }

        schematic = _sch;
        this.startPos = startPos;
    }

    public void setIgnoreBlocks(List<Block> ignoreBlocks) {
        if (ignoreBlocks != null) {
            //PaskalABC and Java lore:
            ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

            this.ignoreBlocks = ignoreBlocks;
            return;
        }
        this.ignoreBlocks = new ArrayList<>();
    }

    @Override
    public void run() {
        if (mc.player == null || mc.world == null || schematic == null) {
            DestroyManager.isDestroying = false;
            return;
        }
        DestroyManager.currentBlockPos = null;

        DestroyManager.isDestroying = true;

        //float oldYaw = mc.player.yaw;
        //float oldPitch = mc.player.pitch;

        ArrayList<BlockPos> sch = new ArrayList<>();

        for (Vec3d vector : schematic) {
            sch.add(new ModifyBlockPos(new Vec3d(startPos.getX() + vector.x, startPos.getY() + vector.y, startPos.getZ() + vector.z)));
        }

        PacketMine packetMine = (PacketMine) Client.getModuleByName("PacketMine");

        for (BlockPos pos : sch) {
            if (ignoreBlocks.contains(mc.world.getBlockState(pos).getBlock()))
                continue;
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
            }
            DestroyManager.currentBlockPos = null;
            mc.interactionManager.cancelBlockBreaking();
        }

        /*
        if (!packetMine.isEnabled()) {
            mc.player.yaw = oldYaw;
            mc.player.pitch = oldPitch;
        }

         */

        DestroyManager.currentBlockPos = null;

        DestroyManager.isDestroying = false;
    }

    public void reset() {
        DestroyManager.currentBlockPos = null;
        mc.interactionManager.cancelBlockBreaking();
        DestroyManager.isDestroying = false;
    }
}
