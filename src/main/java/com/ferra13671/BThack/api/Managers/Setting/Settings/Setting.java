package com.ferra13671.BThack.api.Managers.Setting.Settings;

import com.ferra13671.BThack.api.Module.Module;

import java.util.function.Supplier;

public abstract class Setting {
    private final String name;
    public final Module module;

    public final Supplier<Boolean> dependence;

    protected Setting(String name, Module module, Supplier<Boolean> dependence) {
        this.name = name;
        this.module = module;
        this.dependence = dependence;
    }


    public String getName() {
        return name;
    }

    public Module getModule() {
        return module;
    }

    public abstract void toDefault();
}
