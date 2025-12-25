package com.ferra13671.bthack.features.module;

import com.ferra13671.bthack.features.module.impl.TestModule;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public class Modules {
    @Getter
    private static final List<IModule> modules = new LinkedList<>();

    public static final TestModule test = register(new TestModule());

    private static <T extends IModule> T register(T module) {
        modules.addLast(module);
        return module;
    }
}
