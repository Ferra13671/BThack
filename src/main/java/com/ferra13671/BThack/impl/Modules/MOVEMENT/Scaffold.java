package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Build.FacingBlock;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Scaffold extends Module {

    public Scaffold() {
        super("Scaffold",
                "lang.module.Scaffold",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );
    }
    private FacingBlock currentblock;

    @EventSubscriber
    public void onTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        preAction();
        postAction();
    }

    public void preAction() {
        if (!BuildManager.puckUpPlaceBlocks(false)) return;
        currentblock = null;

        BlockPos blockPos = BlockPos.ofFloored(mc.player.getX(), mc.player.getY() - 1, mc.player.getZ());
        if (!mc.world.getBlockState(blockPos).isReplaceable()) return;

        currentblock = BuildManager.checkNearBlocksExtended(blockPos);
    }

    public void postAction() {
        float offset = 0.3f;

        if (mc.world.getBlockCollisions(mc.player, mc.player.getBoundingBox().expand(-offset, 0, -offset).offset(0, -0.5, 0)).iterator().hasNext())
            return;

        if (currentblock == null) return;
        if (!(mc.player.getMainHandStack().getItem() instanceof BlockItem)) {
            if (!BuildManager.puckUpPlaceBlocks(true)) return;
        }

        BlockHitResult bhr;
        bhr = new BlockHitResult(new Vec3d((double) currentblock.pos().getX() + Math.random(), currentblock.pos().getY() + 0.99f, (double) currentblock.pos().getZ() + Math.random()), currentblock.direction(), currentblock.pos(), false);

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
}
