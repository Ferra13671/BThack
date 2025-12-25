package com.ferra13671.bthack.features.module;

import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.features.category.ICategory;
import com.ferra13671.bthack.managers.bind.Bind;
import com.ferra13671.bthack.managers.bind.BindListener;

public class BThackModule implements IModule {
    private final ModuleInfo annotation = getClass().getAnnotation(ModuleInfo.class);
    private final String name = this.annotation.name();
    private final ICategory category = BThackClient.getInstance().getCategoryManager().getCategory(this.annotation.category());
    private final String id;
    private final String descriptionId;

    private boolean enabled = false;
    private Bind bind = Bind.NONE;
    private BindListener bindListener = null;

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
    public Bind getBind() {
        return this.bind;
    }

    @Override
    public void setBind(Bind bind) {
        if (this.bindListener != null)
            BThackClient.getInstance().getBindManager().unregister(this.bindListener);

        this.bind = bind;
        this.bindListener = new BindListener(this.bind, this::setEnabled, () -> this.enabled);
        BThackClient.getInstance().getBindManager().register(this.bindListener);
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
        BThackClient.getInstance().getEventBus().register(this);
    }

    public void onDisable() {
        BThackClient.getInstance().getEventBus().unregister(this);
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
