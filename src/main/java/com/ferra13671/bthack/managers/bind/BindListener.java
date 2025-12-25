package com.ferra13671.bthack.managers.bind;

import com.ferra13671.bthack.utils.KeyAction;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record BindListener(Bind bind, Consumer<Boolean> activateConsumer, Supplier<Boolean> currentStateSupplier) {

    public void onKey(KeyAction action) {
        if (action == KeyAction.Press)
            this.activateConsumer.accept(this.bind.getType().pressStateFunction.apply(this.currentStateSupplier.get()));
        else if (action == KeyAction.Release && this.bind.getType().useRelease)
            this.activateConsumer.accept(this.bind.getType().releaseState);
    }
}
