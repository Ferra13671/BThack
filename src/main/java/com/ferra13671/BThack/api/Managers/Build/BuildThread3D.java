package com.ferra13671.BThack.api.Managers.Build;

import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuildThread3D extends AbstractBuildThread {

    private ArrayList<Vec3d> buildSchematic3D;


    public void set3DSchematic(int delayTicks, ArrayList<Vec3d> buildSchematic3D, BlockPos startPos) {
        this.delayTicks = delayTicks;
        this.buildSchematic3D = buildSchematic3D;
        this.startPos = startPos;
    }

    public void set3DSchematic(int delayTicks, List<Vec3i> buildSchematic3D, BlockPos startPos) {
        this.delayTicks = delayTicks;
        ArrayList<Vec3d> sch = new ArrayList<>();
        for (Vec3i vec3i : buildSchematic3D) {
            sch.add(new Vec3d(vec3i.getX(), vec3i.getY(), vec3i.getZ()));
        }
        this.buildSchematic3D = sch;
        this.startPos = startPos;
    }

    public void setNeedBlocks(List<Block> blocks) {
        this.blocks = Objects.requireNonNullElseGet(blocks, ArrayList::new);
    }

    @Override
    protected void buildAction() {
        if (mc.player == null || mc.world == null) return;

        BuildManager.isBuilding = true;

        ArrayList<BlockPos> positions = new ArrayList<>();

        for (Vec3d vector : buildSchematic3D) {
            positions.add(new ModifyBlockPos(new Vec3d(startPos.getX() + vector.x, startPos.getY() + vector.y, startPos.getZ() + vector.z)));
        }

        while (BuildManager.check(positions)) {
            for (BlockPos pos : positions) {
                if (!BuildManager.pickUpPlaceBlocks(true, blocks)) {
                    return;
                }
                if (BuildManager.isPossibleRich(pos)) {
                    Block block = mc.world.getBlockState(pos).getBlock();

                    if (BuildManager.ignoreBlocks.contains(block)) {
                        FacingBlock fBlock = BuildManager.checkNearBlocksExtended(pos);
                        if (fBlock == null) {
                            continue;
                        }

                        BuildManager.placeBlock(pos);
                        BuildManager.delay((long) (delayTicks == 0 ? 1 : (delayTicks * mc.renderTickCounter.tickTime)), pos, this);
                    }
                } else {
                    return;
                }
            }
        }
    }
}
