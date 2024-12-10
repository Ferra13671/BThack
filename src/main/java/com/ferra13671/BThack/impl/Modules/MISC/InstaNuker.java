package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.BThack.impl.Modules.PLAYER.AutoTool;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InstaNuker extends Module {

    public static NumberSetting range;
    public static NumberSetting tickDelay;

    public static BooleanSetting packetSwitch;
    public static BooleanSetting postSwitch;

    public static BooleanSetting sequence;
    public static BooleanSetting pauseIfJump;

    public InstaNuker() {
        super("InstaNuker",
                "lang.module.InstaNuker",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        range = new NumberSetting("Range", this, 4, 1, 15, false);
        tickDelay = new NumberSetting("Tick Delay", this, 2, 0, 5, true);

        packetSwitch = new BooleanSetting("Packet Switch", this, true);
        postSwitch = new BooleanSetting("Post Switch", this, true, packetSwitch::getValue);

        sequence = new BooleanSetting("Sequence", this, true);
        pauseIfJump = new BooleanSetting("Pause If Jump", this, true);


        initSettings(
                range,
                tickDelay,

                packetSwitch,
                postSwitch,

                sequence,
                pauseIfJump
        );
    }

    private final HashMap<Block, ArrayList<BlockPos>> poses = new HashMap<>();
    private final Ticker ticker = new Ticker();

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;
        if (pauseIfJump.getValue())
            if (!mc.player.verticalCollision) return;

        if (ticker.passed(tickDelay.getValue() * 50)) {

            filterAction();
            instaBreakAction();

            ticker.reset();
        }
    }

    public void filterAction() {
        for (BlockPos pos : BlockUtils.getSphere(mc.player.getBlockPos(), (float) range.getValue(), (float) range.getValue(), false, true, 0)) {
            if (pos.getY() >= (int) mc.player.getY()) {
                Block _block = mc.world.getBlockState(pos).getBlock();
                if (BlockUtils.canBreak(pos) || _block != Blocks.OBSIDIAN || _block != Blocks.CRYING_OBSIDIAN) {
                    Block block = mc.world.getBlockState(pos).getBlock();
                    if (!poses.containsKey(block)) {
                        poses.put(block, new ArrayList<>(List.of(pos)));
                        return;
                    }

                    boolean add = true;

                    for (BlockPos pos2 : poses.get(block)) {
                        if (pos2.equals(pos)) {
                            add = false;
                            break;
                        }
                    }
                    if (add)
                        poses.get(block).add(pos);
                }
            }
        }
    }

    public void instaBreakAction() {
        poses.forEach((block, list) -> {
            int slot = AutoTool.getBestSlot(mc.world.getBlockState(list.get(0)), 9);
            if (slot != -1) {
                final int oldSlot = mc.player.getInventory().selectedSlot;
                if (checkSlots(oldSlot, slot)) {
                    if (packetSwitch.getValue()) {
                        if (oldSlot != slot)
                            Managers.NETWORK_MANAGER.sendDefaultPacket(new UpdateSelectedSlotC2SPacket(slot));
                    }
                    for (BlockPos pos : list) {
                        if (MathUtils.getDistance(mc.player.getPos(), pos.toCenterPos()) > range.getValue()) continue;
                        if (!BlockUtils.canBreak(pos)) continue;
                        sendPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos);
                        sendPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, pos);
                    }
                    if (packetSwitch.getValue() && postSwitch.getValue() && oldSlot != slot)
                        Managers.NETWORK_MANAGER.sendDefaultPacket(new UpdateSelectedSlotC2SPacket(oldSlot));
                }
            }
        });
    }

    public void sendPacket(PlayerActionC2SPacket.Action action, BlockPos pos) {
        if (sequence.getValue())
            Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerActionC2SPacket(action, pos, AimBotUtils.getInvertedFacingEntity(mc.player), id));
        else
            Managers.NETWORK_MANAGER.sendDefaultPacket(new PlayerActionC2SPacket(action, pos, AimBotUtils.getInvertedFacingEntity(mc.player)));
    }

    public boolean checkSlots(int oldSlot, int slot) {
        if (!packetSwitch.getValue()) {
            return oldSlot == slot;
        } else {
            return true;
        }
    }
}
