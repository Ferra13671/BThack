package com.ferra13671.BThack.Events.Events.Block;

import com.ferra13671.BThack.Events.MegaEvents.Event;
import net.minecraft.util.math.BlockPos;

public class UpdateBlockEvent extends Event {

    public final BlockPos pos;

    public UpdateBlockEvent(BlockPos pos) {
        this.pos = pos;
    }
}
