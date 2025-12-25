package com.ferra13671.bthack.managers.bind;

import lombok.AllArgsConstructor;

import java.util.function.Function;

@AllArgsConstructor
public enum BindType {
    Toggle(enabled -> !enabled, false, false),
    Hold(_ -> true, true, false),
    HoldReversed(_ -> false, true, true);

    public final Function<Boolean, Boolean> pressStateFunction;
    public final boolean useRelease;
    public final boolean releaseState;
}
