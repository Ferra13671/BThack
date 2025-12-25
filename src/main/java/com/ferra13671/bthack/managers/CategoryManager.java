package com.ferra13671.bthack.managers;

import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.features.category.ICategory;
import com.ferra13671.bthack.features.module.IModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class CategoryManager {
    private final HashMap<String, ICategory> categories = new HashMap<>();
    private final HashMap<ICategory, List<IModule>> modules = new HashMap<>();

    public <T extends ICategory> T register(T category) {
        this.categories.put(category.getId(), category);
        return category;
    }

    public <T extends ICategory> T getCategory(String id, Class<T> clazz) {
        return (T) getCategory(id);
    }

    public ICategory getCategory(String id) {
        return this.categories.get(id);
    }

    public void forEach(Consumer<ICategory> consumer) {
        this.categories.values().forEach(consumer);
    }

    public <T extends IModule> T registerModule(T module) {
        if (!this.categories.containsValue(module.getCategory()))
            BThackClient.getINSTANCE().getLogger().error("Category '{}', which is used in module '{}', was not registered in category manager.", module.getCategory().getId(), module.getId());

        this.modules.computeIfAbsent(module.getCategory(), _ -> new ArrayList<>()).add(module);

        return module;
    }

    public <T extends IModule> T getModule(ICategory category, String id, Class<T> clazz) {
        return (T) getModule(category, id);
    }

    public IModule getModule(ICategory category, String id) {
        if (!this.categories.containsValue(category))
            BThackClient.getINSTANCE().getLogger().error("Category '{}' was not registered in category manager", category);

        for (IModule module : this.modules.computeIfAbsent(category, _ -> new ArrayList<>()))
            if (module.getId().equals(id))
                return module;

        return null;
    }
}
