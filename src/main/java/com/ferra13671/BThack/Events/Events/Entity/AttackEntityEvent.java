package com.ferra13671.BThack.Events.Events.Entity;

import com.ferra13671.BThack.Events.MegaEvents.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class AttackEntityEvent extends Event {
    private final PlayerEntity player;
    private final World world;
    private final Hand hand;
    private final Entity entity;

    public AttackEntityEvent(PlayerEntity player, World world, Hand hand, Entity entity) {
        this.player = player;
        this.world = world;
        this.hand = hand;
        this.entity = entity;
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

    public Entity getEntity() {
        return this.entity;
    }
}
