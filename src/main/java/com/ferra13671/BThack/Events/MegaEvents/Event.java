package com.ferra13671.BThack.Events.MegaEvents;

import com.ferra13671.BThack.Events.MegaEvents.EventModifiers.EventPhase;

/**
 * @author Bebra_tyan(Blue_tyan)
 * @version 1
 */

public abstract class Event {
    private final EventPhase eventPhase;

    public Event(EventPhase eventPhase) {
        this.eventPhase = eventPhase;
    }

    public Event() {
        this.eventPhase = EventPhase.PRE;
    }

    private boolean cancelled;

    public EventPhase getPhase() {
        return eventPhase;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
