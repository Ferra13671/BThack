package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Events.Block.AttackBlockEvent;
import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.impl.Modules.MISC.PacketMine.BreakingBlock;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoTool;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class SuperInstaMine extends Module {

    public static BooleanSetting packetSwitch;

    public static NumberSetting length;
    public static NumberSetting extraHeight;
    public static NumberSetting extraWidth;

    public static NumberSetting packetDelay;

    public SuperInstaMine() {
        super("SuperInstaMine",
                "lang.module.SuperInstaMine",
                KeyboardUtils.RELEASE,
                Category.MISC,
                false
        );

        packetSwitch = new BooleanSetting("Packet Switch", this, true);

        length = new NumberSetting("Length", this, 1, 1, 3, true);
        extraHeight = new NumberSetting("Extra Height", this, 0, 0, 2, true);
        extraWidth = new NumberSetting("Extra Width", this, 0, 0, 2, true);

        packetDelay = new NumberSetting("Packet Delay", this, 0, 0, 5, true);

        initSettings(
                packetSwitch,

                length,
                extraHeight,
                extraWidth,

                packetDelay
        );
    }

    private final ArrayList<BreakingBlock> breakingBlocks = new ArrayList<>();

    @Override
    public void onEnable() {
        super.onEnable();
        Client.getModuleByName("PacketMine").setToggled(false);
        breakingBlocks.clear();
    }

    @EventSubscriber
    public void onTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        ThreadManager.startNewThread("SuperInstaMine", thread -> {
            for (BreakingBlock breakingBlock : breakingBlocks) {
                int slot = AutoTool.getBestSlot(mc.world.getBlockState(breakingBlock.blockPos), 9);
                int oldSlot = mc.player.getInventory().selectedSlot;
                if (slot == -1) continue;
                if (!packetSwitch.getValue()) {
                    if (oldSlot != slot) continue;
                }
                if (!mc.world.isAir(breakingBlock.blockPos) && mc.world.getBlockState(breakingBlock.blockPos).getBlock() != Blocks.BEDROCK) {
                    if (packetSwitch.getValue())
                        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
                    mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakingBlock.blockPos, AimBotUtils.getInvertedFacingEntity(mc.player), 0));
                    mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakingBlock.blockPos, AimBotUtils.getInvertedFacingEntity(mc.player), 0));
                    if (packetSwitch.getValue())
                        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(oldSlot));

                    if (packetDelay.getValue() > 0) {
                        try {
                            thread.sleep((int) packetDelay.getValue());
                        } catch (Exception ignored) {}
                    }
                }
            }
            breakingBlocks.removeIf(breakingBlock -> mc.world.isAir(breakingBlock.blockPos) || mc.world.getBlockState(breakingBlock.blockPos).getBlock() == Blocks.BEDROCK);
        });
    }

    @EventSubscriber
    public void onAttack(AttackBlockEvent e) {
        if (e.getBlockPos() == null) return;

        BlockPos pos = e.getBlockPos();
        breakingBlocks.add(new BreakingBlock(pos));
        addBlocks(pos, (int) length.getValue(), (int) extraWidth.getValue(), (int) extraHeight.getValue());
    }

    public void addBlocks(BlockPos pos, int length, int extraWidth, int extraHeight) {
        for (int i = 0; i < length + 1; i++) {
            switch (AimBotUtils.getFacing(mc.player)) {
                case WEST, EAST -> {
                    for (int i2 = -1 - extraHeight; i2 < 1 + extraHeight + 1; i2++) {
                        for (int i3 = -1 - extraWidth; i3 < 1 + extraWidth + 1; i3++) {
                            breakingBlocks.add(new BreakingBlock(pos.add(i, i2, i3)));
                        }
                    }
                }
                case NORTH, SOUTH -> {
                    for (int i2 = -1 - extraHeight; i2 < 1 + extraHeight + 1; i2++) {
                        for (int i3 = -1 - extraWidth; i3 < 1 + extraWidth + 1; i3++) {
                            breakingBlocks.add(new BreakingBlock(pos.add(i3, i2, i)));
                        }
                    }
                }
                case UP, DOWN -> {
                    for (int i2 = -1 - extraHeight; i2 < 1 + extraHeight + 1; i2++) {
                        for (int i3 = -1 - extraWidth; i3 < 1 + extraWidth + 1; i3++) {
                            breakingBlocks.add(new BreakingBlock(pos.add(i2, i, i3)));
                        }
                    }
                }
            }
        }
    }
}
