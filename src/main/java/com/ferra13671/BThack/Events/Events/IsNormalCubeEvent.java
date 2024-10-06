package com.ferra13671.BThack.Events.Events;

import com.ferra13671.BThack.Events.MegaEvents.Event;
import net.minecraft.util.math.BlockPos;

public class IsNormalCubeEvent extends Event {
    public BlockPos pos;

    public IsNormalCubeEvent(BlockPos pos) {
        this.pos = pos;
    }
}
