package com.ferra13671.BThack.Events.Events.Block;

import com.ferra13671.BThack.Events.MegaEvents.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AttackBlockEvent extends Event {
    private final PlayerEntity player;
    private final World world;
    private final Hand hand;
    private final BlockPos blockPos;
    private final Direction direction;

    public AttackBlockEvent(PlayerEntity player, World world, Hand hand, BlockPos blockPos, Direction direction) {
        this.player = player;
        this.world = world;
        this.hand = hand;
        this.blockPos = blockPos;
        this.direction = direction;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public World getWorld() {
        return this.world;
    }

    public Hand getHand() {
        return this.hand;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public Direction getDirection() {
        return this.direction;
    }
}
