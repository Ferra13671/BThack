package com.ferra13671.BThack.api.Utils.Modules;

import net.minecraft.entity.Entity;

@FunctionalInterface
public interface EntityFilter<T> {

    T get(Entity entity);
}