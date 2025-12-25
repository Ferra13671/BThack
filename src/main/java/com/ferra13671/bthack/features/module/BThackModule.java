package com.ferra13671.bthack.features.module;

import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.features.category.ICategory;

public class BThackModule implements IModule {
    private final ModuleInfo annotation = getClass().getAnnotation(ModuleInfo.class);
    private final String name = this.annotation.name();
    private final ICategory category = BThackClient.getINSTANCE().getCategoryManager().getCategory(this.annotation.category());
    private final String id;
    private final String descriptionId;

    private boolean enabled = false;

    public BThackModule() {
        this.id = name.toLowerCase().replace(" ", "-");
        this.descriptionId = String.join(".", "modules", "bthack", this.id);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ICategory getCategory() {
        return this.category;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getDescriptionId() {
        return this.descriptionId;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (isEnabled() && !enabled) {
            this.enabled = false;
            onDisable();
        }
        if (!isEnabled() && enabled) {
            this.enabled = true;
            onEnable();
        }
    }

    public void onEnable() {
        BThackClient.getINSTANCE().getEventBus().register(this);
    }

    public void onDisable() {
        BThackClient.getINSTANCE().getEventBus().unregister(this);
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
