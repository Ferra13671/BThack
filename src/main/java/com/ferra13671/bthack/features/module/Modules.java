package com.ferra13671.bthack.features.module;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public class Modules {
    @Getter
    private static final List<IModule> modules = new LinkedList<>();



    private static <T extends IModule> T register(T module) {
        modules.addLast(module);
        return module;
    }
}
