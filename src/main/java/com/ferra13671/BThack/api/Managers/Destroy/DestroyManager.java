package com.ferra13671.BThack.api.Managers.Destroy;


import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Interfaces.Pc;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class DestroyManager implements Mc, Pc {
    public static boolean isDestroying = false;
    public static BlockPos currentBlockPos;

    private boolean isInteractDestroying = false;



    @EventSubscriber
    public void onTick(TickEvent.ClientTickEvent e) {
        if (mc.player == null || mc.world == null || mc.isPaused()) return;

        if (currentBlockPos == null) {
            isInteractDestroying = false;
            return;
        }



        try {
            if (!pc.isBreakingBlock() && !isInteractDestroying) {
                if (pc.startBlockBreaking(currentBlockPos, AimBotUtils.getInvertedFacingEntity(mc.player))) {
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
                isInteractDestroying = true;
            } else {
                if (pc.updateBlockBreaking(currentBlockPos, AimBotUtils.getInvertedFacingEntity(mc.player))) {
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
            }
        } catch (Exception ignored) {}
    }

    public static boolean isNeedToBreak(BlockPos pos) {
        return !mc.world.isAir(pos) && !BuildManager.ignoreBlocks.contains(mc.world.getBlockState(pos).getBlock()) && !(mc.world.getBlockState(pos).getBlock() instanceof FlowerBlock) && mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK;
    }






    public static void delay(long milliseconds, Thread thread) {
        try {
            thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {}
    }
}
