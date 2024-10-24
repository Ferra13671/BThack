package com.ferra13671.BThack.Events.Block;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.util.math.BlockPos;

public class UpdateBlockEvent extends Event {

    public final BlockPos pos;

    public UpdateBlockEvent(BlockPos pos) {
        this.pos = pos;
    }
}
