package com.ferra13671.BThack.api.Utils;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class BlockUtils implements Mc {

    public static BlockState getState(BlockPos pos) {
        return mc.world.getBlockState(pos);
    }

    public static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    public static int getId(BlockPos pos) {
        return Block.getRawIdFromState(getState(pos));
    }

    public static String getName(BlockPos pos) {
        return getName(getBlock(pos));
    }

    public static String getName(Block block) {
        return Registries.BLOCK.getId(block).toString();
    }

    public static Block getBlockFromNameOrID(String nameOrId) {
        if (MathUtils.isInteger(nameOrId)) {
            BlockState state = Block.STATE_IDS.get(Integer.parseInt(nameOrId));
            if (state == null)
                return null;

            return state.getBlock();
        }

        try {
            Block block = Registries.BLOCK.getOrEmpty(new Identifier(nameOrId))
                    .orElse(null);
            if (block == null)
                block = Registries.BLOCK.getOrEmpty(new Identifier("minecraft:" + nameOrId)).orElse(null);

            return block;
        } catch (InvalidIdentifierException e) {
            return null;
        }
    }

    public static float getHardness(BlockPos pos) {
        return getState(pos).calcBlockBreakingDelta(mc.player, mc.world, pos);
    }

    private static VoxelShape getOutlineShape(BlockPos pos) {
        return getState(pos).getOutlineShape(mc.world, pos);
    }

    public static Box getBoundingBox(BlockPos pos) {
        VoxelShape shape = getOutlineShape(pos);
        if (!shape.isEmpty()) {
            return shape.getBoundingBox().offset(pos);
        } else {
            return createBox(pos, 0.5, 0.5, 1, false);
        }
    }

    public static boolean canBeClicked(BlockPos pos) {
        return getOutlineShape(pos) != VoxelShapes.empty();
    }

    public static boolean isOpaqueFullCube(BlockPos pos) {
        return getState(pos).isOpaqueFullCube(mc.world, pos);
    }

    public static BlockHitResult raycast(Vec3d from, Vec3d to, RaycastContext.FluidHandling fluidHandling) {
        RaycastContext context = new RaycastContext(from, to,
                RaycastContext.ShapeType.COLLIDER, fluidHandling, mc.player);

        return mc.world.raycast(context);
    }

    public static BlockHitResult raycast(Vec3d from, Vec3d to) {
        return raycast(from, to, RaycastContext.FluidHandling.NONE);
    }

    public static boolean hasLineOfSight(Vec3d from, Vec3d to) {
        return raycast(from, to).getType() == HitResult.Type.MISS;
    }

    public static boolean hasLineOfSight(Vec3d to) {
        return raycast(AimBotUtils.getEyesPos(), to)
                .getType() == HitResult.Type.MISS;
    }

    public static Stream<Box> getBlockCollisions(Box box) {
        Iterable<VoxelShape> blockCollisions =
                mc.world.getBlockCollisions(mc.player, box);

        return StreamSupport.stream(blockCollisions.spliterator(), false)
                .flatMap(shape -> shape.getBoundingBoxes().stream())
                .filter(shapeBox -> shapeBox.intersects(box));
    }

    public static ArrayList<BlockPos> getAllInBox(BlockPos from, BlockPos to) {
        ArrayList<BlockPos> blocks = new ArrayList<>();

        BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()),
                Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()),
                Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));

        for(int x = min.getX(); x <= max.getX(); x++)
            for(int y = min.getY(); y <= max.getY(); y++)
                for(int z = min.getZ(); z <= max.getZ(); z++)
                    blocks.add(new BlockPos(x, y, z));

        return blocks;
    }

    public static ArrayList<BlockPos> getAllInBox(BlockPos center, int range) {
        return getAllInBox(center.add(-range, -range, -range),
                center.add(range, range, range));
    }

    public static Stream<BlockPos> getAllInBoxStream(BlockPos from, BlockPos to) {
        BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()),
                Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()),
                Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));

        Stream<BlockPos> stream = Stream.<BlockPos> iterate(min, pos -> {

            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            x++;

            if(x > max.getX())
            {
                x = min.getX();
                y++;
            }

            if(y > max.getY())
            {
                y = min.getY();
                z++;
            }

            if(z > max.getZ())
                throw new IllegalStateException("Stream limit didn't work.");

            return new BlockPos(x, y, z);
        });

        int limit = (max.getX() - min.getX() + 1)
                * (max.getY() - min.getY() + 1) * (max.getZ() - min.getZ() + 1);

        return stream.limit(limit);
    }

    public static List<BlockPos> getNearbyBlocks(PlayerEntity entityPlayer, double blockRange, boolean motion) {
        List<BlockPos> nearbyBlocks = new ArrayList<>();

        int range = (int) MathUtils.roundNumber(blockRange, 0);

        if (motion) {
            entityPlayer.getPos().add(new Vec3d(entityPlayer.velocity.x, entityPlayer.velocity.y, entityPlayer.velocity.z));
        }

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    nearbyBlocks.add(entityPlayer.getBlockPos().add(x, y, z));
                }
            }
        }

        return nearbyBlocks;
    }

    public static List<BlockPos> getSphere(BlockPos loc, float radius, float height, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleBlocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) radius; x <= cx + radius; x++) {
            for (int z = cz - (int) radius; z <= cz + radius; z++) {
                for (int y = (sphere ? cy - (int) height : cy); y < (cy + height); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1))) {
                        circleBlocks.add(new BlockPos(x, y + plus_y, z));
                    }
                }
            }
        }
        return circleBlocks;
    }

    public static Stream<BlockPos> getAllInBoxStream(BlockPos center, int range) {
        return getAllInBoxStream(center.add(-range, -range, -range),
                center.add(range, range, range));
    }

    public static Box getBox(BlockEntity be) {
        BlockPos pos = be.getPos();

        if(!canBeClicked(pos))
            return null;

        if(be instanceof ChestBlockEntity) {
            return getChestBox(be);
        } else if (be instanceof EnderChestBlockEntity) {
            return getEnderChestBox(be);
        }

        return getBoundingBox(pos);
    }

    private static Box getChestBox(BlockEntity chestBE) {
        BlockState state = chestBE.getCachedState();
        if(!state.contains(ChestBlock.CHEST_TYPE))
            return null;

        ChestType chestType = state.get(ChestBlock.CHEST_TYPE);

        // ignore other block in double chest
        //if(chestType == ChestType.LEFT)
        //    return null;               //It just breaks the renders

        BlockPos pos = chestBE.getPos();
        Box box = getBoundingBox(pos);

        // larger box for double chest
        if(chestType != ChestType.SINGLE)
        {
            BlockPos pos2 = pos.offset(ChestBlock.getFacing(state));

            if(canBeClicked(pos2))
            {
                Box box2 = getBoundingBox(pos2);
                box = box.union(box2);
            }
        }

        return box;
    }

    private static Box getEnderChestBox(BlockEntity chestBE) {

        BlockPos pos = chestBE.getPos();

        return getBoundingBox(pos);
    }

    public static boolean isIntercepted(BlockPos pos) {
        for (Entity entity : mc.world.getEntities()) {
            if (new Box(pos).intersects(entity.getBoundingBox())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCanBeSupplied(BlockPos pos) {
        if (BuildManager.ignoreBlocks.contains(mc.world.getBlockState(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ())).getBlock())) {
            if (BuildManager.ignoreBlocks.contains(mc.world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ())).getBlock())) {
                if (BuildManager.ignoreBlocks.contains(mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).getBlock())) {
                    if (BuildManager.ignoreBlocks.contains(mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).getBlock())) {
                        if (BuildManager.ignoreBlocks.contains(mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1)).getBlock())) {
                            return !BuildManager.ignoreBlocks.contains(mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1)).getBlock());
                        }
                    }
                }
            }
        }
        return true;
    }

    public static Box createBox(BlockPos blockPos, double length, double width, double height, boolean yOnCenter) {
        Vec3d pos = new Vec3d(blockPos.getX() + 0.5, blockPos.getY() + (yOnCenter ? 0.5 : 0), blockPos.getZ() + 0.5);
        return new Box(pos.x - length, (yOnCenter ? (pos.y - height) : pos.y ), pos.z - width, pos.x + length, pos.y + height, pos.z + width);
    }
}
