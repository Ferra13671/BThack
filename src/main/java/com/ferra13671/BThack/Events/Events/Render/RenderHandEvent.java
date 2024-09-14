package com.ferra13671.BThack.Events.Events.Render;

import com.ferra13671.BThack.Events.MegaEvents.Event;
import net.minecraft.util.Hand;

public class RenderHandEvent extends Event {

    private final Hand hand;

    public RenderHandEvent(Hand hand) {
        this.hand = hand;
    }

    public Hand getHand() {
        return hand;
    }
}
