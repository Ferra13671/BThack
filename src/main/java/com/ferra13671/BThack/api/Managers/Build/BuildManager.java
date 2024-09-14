package com.ferra13671.BThack.api.Managers.Build;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.RotateUtils;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

//It's hardly used anywhere. I don't know what to do with it.
public class BuildManager implements Mc {

    public static final Set<Block> ignoreBlocks = Sets.newHashSet(
            Blocks.AIR,
            Blocks.CAVE_AIR,
            Blocks.WATER,
            //Blocks.WATER_CAULDRON,
            Blocks.LAVA
            //Blocks.LAVA_CAULDRON
    );

    public static final Set<Block> airs = Sets.newHashSet(
            Blocks.AIR,
            Blocks.CAVE_AIR,
            Blocks.VOID_AIR
    );

    private static final List<Block> shiftBlocks = Arrays.asList(
            Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE,
            Blocks.BIRCH_TRAPDOOR, Blocks.BAMBOO_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR, Blocks.CHERRY_TRAPDOOR,
            Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER,
            Blocks.ACACIA_TRAPDOOR, Blocks.ENCHANTING_TABLE, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX,
            Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX
    );

    public static boolean isBuilding = false;

    public static void placeBlock(BlockPos pos) {
        if (!mc.world.getBlockState(pos).isReplaceable()) return;
        FacingBlock block = checkNearBlocksExtended(pos);
        BlockHitResult bhr;
        bhr = new BlockHitResult(new Vec3d((double) block.pos().getX() + Math.random(), block.pos().getY() + 0.99f, (double) block.pos().getZ() + Math.random()), block.direction(), block.pos(), false);

        float[] rotations = AimBotUtils.rotations(bhr.getPos());
        boolean sneak = BuildManager.needSneak(mc.world.getBlockState(bhr.getBlockPos()).getBlock()) && !mc.player.isSneaking();

        if (sneak)
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));

        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), rotations[0], rotations[1], mc.player.isOnGround()));
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, bhr);
        mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));

        mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround()));
    }

    protected static boolean check(ArrayList<BlockPos> positions) {
        for (BlockPos pos : positions) {
            if (ignoreBlocks.contains(mc.world.getBlockState(pos).getBlock())) {
                return true;
            }
        }
        return false;
    }

    public static void delay(long milliseconds, BlockPos pos, Thread thread) {
        for (long i = 0; i < milliseconds; i += 10) {
            //RenderManager.addRenderBox(pos, new BoxColor(0, 1, 0, 1, 0, 1, 0, 0.5f));

            try {
                thread.sleep(10);
            } catch (InterruptedException ignored) {}
        }
    }

    public static boolean needSneak(Block in) {
        return shiftBlocks.contains(in);
    }

    public static boolean puckUpPlaceBlocks(boolean swap) {
        if (mc.player.getMainHandStack().getItem() instanceof BlockItem) return true;

        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock().getDefaultState().isSolid()) {
                    if (swap) {
                        if (i < 9) {
                            InventoryUtils.swapItem(i);
                        } else {
                            for (int a = 0; a < 9; a++) {
                                if (mc.player.getInventory().getStack(a).getItem() == Items.AIR) {
                                    InventoryUtils.swapItemOnInventory(a, i);
                                    mc.interactionManager.tick();
                                    InventoryUtils.swapItem(a);
                                    return true;
                                }
                            }
                            InventoryUtils.swapItemOnInventory(mc.player.getInventory().selectedSlot, i);
                            mc.interactionManager.tick();
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static FacingBlock checkNearBlocksExtended(BlockPos blockPos) {
        FacingBlock ret;

        ret = checkNearBlocks(blockPos);
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(-1, 0, 0));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(1, 0, 0));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(0, 0, 1));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(0, 0, -1));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(-2, 0, 0));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(2, 0, 0));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(0, 0, 2));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(0, 0, -2));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(0, -1, 0));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(1, -1, 0));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(-1, -1, 0));
        if (ret != null) return ret;

        ret = checkNearBlocks(blockPos.add(0, -1, 1));
        if (ret != null) return ret;

        return checkNearBlocks(blockPos.add(0, -1, -1));
    }

    public static FacingBlock checkNearBlocks(@NotNull BlockPos blockPos) {
        if (mc.world.getBlockState(blockPos.add(0, -1, 0)).isSolid())
            return new FacingBlock(blockPos.add(0, -1, 0), Direction.UP);

        else if (mc.world.getBlockState(blockPos.add(-1, 0, 0)).isSolid())
            return new FacingBlock(blockPos.add(-1, 0, 0), Direction.EAST);

        else if (mc.world.getBlockState(blockPos.add(1, 0, 0)).isSolid())
            return new FacingBlock(blockPos.add(1, 0, 0), Direction.WEST);

        else if (mc.world.getBlockState(blockPos.add(0, 0, 1)).isSolid())
            return new FacingBlock(blockPos.add(0, 0, 1), Direction.NORTH);

        else if (mc.world.getBlockState(blockPos.add(0, 0, -1)).isSolid())
            return new FacingBlock(blockPos.add(0, 0, -1), Direction.SOUTH);
        return null;
    }
}
