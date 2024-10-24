package com.ferra13671.BThack.impl.Modules.MISC.PacketMine;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.Events.Block.AttackBlockEvent;
import com.ferra13671.BThack.Events.Block.UseBlockEvent;
import com.ferra13671.BThack.Events.Render.RenderWorldEvent;
import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoTool;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

public class PacketMine extends Module {

    public static BooleanSetting swingHand;
    public static BooleanSetting visibleBreaking;
    public static BooleanSetting packetRotate;
    public static BooleanSetting clientDestroy;
    public static BooleanSetting extraPackets;

    public static BooleanSetting removeUse;
    public static BooleanSetting stopPackets;

    public static BooleanSetting speedMine;
    public static NumberSetting mineSpeed;

    public static BooleanSetting conveyorMode;

    public static BooleanSetting doubleMode;
    public static NumberSetting fastSpeed;
    public static NumberSetting normalSpeed;
    public static BooleanSetting switchToOld;

    public static BooleanSetting inventoryMode;
    public static NumberSetting hotbarSlot;

    public static BooleanSetting renderBox;
    public static NumberSetting boxRed;
    public static NumberSetting boxGreen;
    public static NumberSetting boxBlue;


    public PacketMine() {
        super("PacketMine",
                "lang.module.PacketMine",
                KeyboardUtils.RELEASE,
                Category.MISC,
                false
        );

        swingHand = new BooleanSetting("Swing Hand", this, false);
        visibleBreaking = new BooleanSetting("Visible Breaking", this, false);
        packetRotate = new BooleanSetting("Packet Rotate", this, true);
        clientDestroy = new BooleanSetting("Client Destroy", this, false);
        extraPackets = new BooleanSetting("Extra Packets", this, false);

        removeUse = new BooleanSetting("Remove If Use", this, true);
        stopPackets = new BooleanSetting("Stop Packets", this, true, removeUse::getValue);

        speedMine = new BooleanSetting("Speed Mine", this, false, () -> !doubleMode.getValue());
        mineSpeed = new NumberSetting("Mine Speed", this, 1.2, 1, 10, false, () -> speedMine.getValue() && !doubleMode.getValue());

        conveyorMode = new BooleanSetting("Conveyor Mode", this, false);

        doubleMode = new BooleanSetting("Double Mode", this, false, conveyorMode::getValue);
        fastSpeed = new NumberSetting("Fast Speed", this, 5, 5, 50, false, () -> conveyorMode.getValue() && doubleMode.getValue());
        normalSpeed = new NumberSetting("Normal Speed", this, 1.05, 0.9, 1.3, false, () -> conveyorMode.getValue() && doubleMode.getValue());
        switchToOld = new BooleanSetting("Switch To Old", this, false, () -> conveyorMode.getValue() && doubleMode.getValue());

        inventoryMode = new BooleanSetting("Inventory Mode", this, false);
        hotbarSlot = new NumberSetting("Hotbar Slot", this, 1, 1, 9, true, inventoryMode::getValue);

        renderBox = new BooleanSetting("Render Box", this, true);
        boxRed = new NumberSetting("Box Red", this, 0, 0, 255, true, renderBox::getValue);
        boxGreen = new NumberSetting("Box Green", this, 255, 0, 255, true, renderBox::getValue);
        boxBlue = new NumberSetting("Box Blue", this, 0, 0, 255, true, renderBox::getValue);

        initSettings(
                swingHand,
                visibleBreaking,
                packetRotate,
                clientDestroy,
                extraPackets,

                removeUse,
                stopPackets,

                speedMine,
                mineSpeed,

                conveyorMode,

                doubleMode,
                fastSpeed,
                normalSpeed,
                switchToOld,

                inventoryMode,
                hotbarSlot,

                renderBox,
                boxRed,
                boxGreen,
                boxBlue
        );

    }

    private boolean doubleFast;
    private BreakingBlock doubleBlock;
    private boolean firstSkip = true;

    public BreakingBlock currentBreakingBlock;
    private final ArrayList<BreakingBlock> conveyorBlocks = new ArrayList<>();
    private float destroyDelta = 0;

    private int currentInventoryModeSlot = -1;
    private int currentInventoryModeHotbarSlot = -1;

    @Override
    public void onEnable() {
        super.onEnable();

        currentBreakingBlock = null;
        conveyorBlocks.clear();
        currentInventoryModeSlot = -1;
        doubleFast = true;
        firstSkip = true;

        Client.getModuleByName("SuperInstaMine").setToggled(false);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        currentBreakingBlock = null;
        conveyorBlocks.clear();
        if (!nullCheck())
            packetRemoveItem();
        doubleFast = true;
        firstSkip = true;
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    public void onAttackBlock(AttackBlockEvent e) {
        if (nullCheck() || DestroyManager.isDestroying) return;
        if (e.getBlockPos() == null) return;

        if (!updateBlock(e.getBlockPos()))
            e.setCancelled(true);
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    public void onUseBlock(UseBlockEvent e) {
        if (e.blockHitResult.getBlockPos() == null) return;
        if (currentBreakingBlock == null) return;
        if (!removeUse.getValue()) return;
        if (conveyorMode.getValue() && conveyorContains(e.getBlockHitResult().getBlockPos())) {
            conveyorRemove(e.getBlockHitResult().getBlockPos());
            e.setCancelled(true);
        }
        if (e.blockHitResult.getBlockPos() == currentBreakingBlock.blockPos) {
            e.setCancelled(true);
            if (stopPackets.getValue()) {
                mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, currentBreakingBlock.blockPos, Direction.DOWN));
                mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, currentBreakingBlock.blockPos, AimBotUtils.getInvertedFacingEntity(mc.player), 0));
            }

            currentBreakingBlock = null;
        }
    }

    public boolean updateBlock(BlockPos pos) {
        if (conveyorMode.getValue()) {
            if (currentBreakingBlock != null) {
                if (!conveyorContains(pos)) {
                    conveyorBlocks.add(new BreakingBlock(pos));
                }
                return false;
            }
        }

        currentBreakingBlock = new BreakingBlock(pos);
        return true;
    }

    private void checkDestroyDelta() {
        int oldSlot = mc.player.getInventory().selectedSlot;

        int bestSlot = AutoTool.getBestSlot(mc.world.getBlockState(currentBreakingBlock.blockPos), inventoryMode.getValue() ? 36 : 9);
        if (bestSlot != -1) {
            if (bestSlot < 9) {
                InventoryUtils.swapItem(bestSlot);
            } else {
                InventoryUtils.swapItemOnInventory(oldSlot, bestSlot);
            }
        }


        boolean ground = mc.player.onGround;
        mc.player.onGround = true;

        destroyDelta = mc.world.getBlockState(currentBreakingBlock.blockPos).calcBlockBreakingDelta(mc.player, mc.player.getWorld(), currentBreakingBlock.blockPos);
        mc.player.onGround = ground;

        if (bestSlot < 9) {
            InventoryUtils.swapItem(oldSlot);
        } else {
            InventoryUtils.swapItemOnInventory(oldSlot, bestSlot);
        }
    }

    @EventSubscriber
    public void onRender(RenderWorldEvent.Last e) {
        if (!renderBox.getValue() || currentBreakingBlock == null) return;

        ArrayList<RenderBox> renderBoxes = new ArrayList<>();
        renderBoxes.add(
                new RenderBox(
                        BlockUtils.createBox(
                                currentBreakingBlock.blockPos,
                                currentBreakingBlock.currentDestroyProgress / 2,
                                currentBreakingBlock.currentDestroyProgress / 2,
                                currentBreakingBlock.currentDestroyProgress / 2,
                                true
                        ),
                        (float) boxRed.getValue() / 255f,
                        (float) boxGreen.getValue() / 255f,
                        (float) boxBlue.getValue() / 255f,
                        1,
                        (float) boxRed.getValue() / 255f,
                        (float) boxGreen.getValue() / 255f,
                        (float) boxBlue.getValue() / 255f,
                        0.3f
                )
        );

        if (conveyorMode.getValue()) {
            if (doubleMode.getValue() && doubleBlock != null) {
                if (DestroyManager.isNeedToBreak(doubleBlock.blockPos)) {
                    renderBoxes.add(
                            new RenderBox(
                                    BlockUtils.createBox(
                                            doubleBlock.blockPos,
                                            doubleBlock.currentDestroyProgress / 2,
                                            doubleBlock.currentDestroyProgress / 2,
                                            doubleBlock.currentDestroyProgress / 2,
                                            true
                                    ),
                                    (float) boxRed.getValue() / 255f,
                                    (float) boxGreen.getValue() / 255f,
                                    (float) boxBlue.getValue() / 255f,
                                    1,
                                    (float) boxRed.getValue() / 255f,
                                    (float) boxGreen.getValue() / 255f,
                                    (float) boxBlue.getValue() / 255f,
                                    0.3f
                            )
                    );
                }
            }
            for (BreakingBlock pos : conveyorBlocks) {
                renderBoxes.add(
                        new RenderBox(
                                BlockUtils.createBox(
                                        pos.blockPos,
                                        0.1,
                                        0.1,
                                        0.1,
                                        true
                                ),
                                1,
                                1,
                                0,
                                1,
                                1,
                                1,
                                0,
                                0.3f
                        )
                );
            }
        }

        BThackRender.boxRender.prepareBoxRender();
        BThackRender.boxRender.renderBoxes(renderBoxes);
        BThackRender.boxRender.stopBoxRender();
    }

    @EventSubscriber
    public void onTick(TickEvent.ClientTickEvent e) {
        if (nullCheck() || mc.isPaused()) return;

        if ((!conveyorMode.getValue() || conveyorBlocks.isEmpty()) && currentBreakingBlock == null) doubleBlock = null;

        if (!conveyorMode.getValue())
            if (!conveyorBlocks.isEmpty())
                conveyorBlocks.clear();

        if (conveyorBlocks.isEmpty()) firstSkip = true;



        if (currentBreakingBlock == null) {
            doubleFast = true;
            destroyDelta = 0;
            return;
        }

        if (destroyDelta == 0) {
            checkDestroyDelta();
        }


        if (!updateBreak(currentBreakingBlock, true)) {

            packetRemoveItem();

            if (conveyorMode.getValue()) {
                for (BreakingBlock breakingBlock : conveyorBlocks) {
                    if (breakingBlock.equals(currentBreakingBlock)) {
                        conveyorBlocks.remove(breakingBlock);
                        break;
                    }
                }
                if (!firstSkip) {
                    conveyorBlocks.remove(0);
                } else {
                    firstSkip = false;
                }

                currentBreakingBlock = null;
                if (!conveyorBlocks.isEmpty()) {
                    updateBlock(conveyorBlocks.get(0).blockPos);
                }
            } else {
                currentBreakingBlock = null;
            }
        }
    }

    public boolean updateBreak(BreakingBlock breakingBlock, boolean reset) {
        Block block = mc.world.getBlockState(breakingBlock.blockPos).getBlock();

        if (mc.world.isAir(breakingBlock.blockPos) || block == Blocks.BEDROCK) {
            packetRemoveItem();

            if (reset) destroyDelta = 0;
            return false;
        }

        if (packetRotate.getValue()) {
            float[] rot = AimBotUtils.rotations(breakingBlock.blockPos);

            GrimUtils.sendPreActionGrimPackets(rot[0], rot[1]);
        }

        if (reset && currentInventoryModeSlot == -1) {
            packetEquipItem();
        }

        try {
            if (!breakingBlock.startDestroying) {
                if (extraPackets.getValue()) {
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.yaw, mc.player.pitch, true));
                }

                mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakingBlock.blockPos, AimBotUtils.getInvertedFacingEntity(mc.player), 0));
                if (swingHand.getValue()) mc.player.swingHand(Hand.MAIN_HAND);
                breakingBlock.startDestroying = true;
            } else {
                if (swingHand.getValue()) mc.player.swingHand(Hand.MAIN_HAND);
                breakingBlock.currentDestroyProgress += destroyDelta * (doubleMode.getValue() && conveyorMode.getValue() && !conveyorBlocks.isEmpty() ? (doubleFast && (firstSkip || conveyorBlocks.size() > 1) ? fastSpeed.getValue() : normalSpeed.getValue()) : (speedMine.getValue() ? mineSpeed.getValue() : 1));
                if (visibleBreaking.getValue()) mc.world.setBlockBreakingInfo(mc.player.getId(), breakingBlock.blockPos, (int)(breakingBlock.currentDestroyProgress * 10.0F));

            }
            if (breakingBlock.currentDestroyProgress >= 1) {
                breakingBlock.currentDestroyProgress = 1;
                if (extraPackets.getValue()) {
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.yaw, mc.player.pitch, true));
                }

                if (clientDestroy.getValue())
                    mc.interactionManager.breakBlock(breakingBlock.blockPos);

                mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, breakingBlock.blockPos, Direction.DOWN));
                mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakingBlock.blockPos, AimBotUtils.getInvertedFacingEntity(mc.player), 0));

                packetRemoveItem();


                if (reset) destroyDelta = 0;
                doubleFast = !doubleFast;
                if (!doubleFast) {
                    doubleBlock = currentBreakingBlock;
                } else {
                    if (doubleBlock != null && conveyorMode.getValue() && doubleMode.getValue() && switchToOld.getValue()) {
                        mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, doubleBlock.blockPos, AimBotUtils.getInvertedFacingEntity(mc.player), 0));
                        //mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, doublePos, Direction.DOWN));
                        mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, doubleBlock.blockPos, AimBotUtils.getInvertedFacingEntity(mc.player), 0));
                    }
                }
                return false;
            }
        } catch (Exception ignored) {}

        if (breakingBlock.blockPos != null) {
            if (mc.world.isAir(breakingBlock.blockPos)) {
                if (reset) destroyDelta = 0;
                return false;
            }
        }
        return true;
    }

    private void packetEquipItem() {
        int bestSlot = AutoTool.getBestSlot(mc.world.getBlockState(currentBreakingBlock.blockPos), inventoryMode.getValue() ? 36 : 8);
        if (bestSlot != -1) {
            if (bestSlot < 9) {
                mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(bestSlot));
            } else {
                currentInventoryModeHotbarSlot = (int) hotbarSlot.getValue() - 1;
                pc.packetClickSlot(0, bestSlot, currentInventoryModeHotbarSlot, SlotActionType.SWAP, mc.player);
                currentInventoryModeSlot = bestSlot;
                mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(currentInventoryModeHotbarSlot));
            }
        }
    }

    private void packetRemoveItem() {
        if (currentInventoryModeSlot != -1 && currentInventoryModeHotbarSlot != -1) {
            pc.packetClickSlot(0, currentInventoryModeSlot, currentInventoryModeHotbarSlot, SlotActionType.SWAP, mc.player);
            currentInventoryModeSlot = -1;
            currentInventoryModeHotbarSlot = -1;
        }
        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
    }

    private boolean conveyorContains(BlockPos pos) {
        for (BreakingBlock breakingBlock : conveyorBlocks) {
            if (breakingBlock.blockPos.equals(pos))
                return true;
        }
        return false;
    }

    private void conveyorRemove(BlockPos pos) {
        for (BreakingBlock breakingBlock : conveyorBlocks) {
            if (breakingBlock.blockPos == pos) {
                conveyorBlocks.remove(breakingBlock);
                return;
            }
        }
    }
}
