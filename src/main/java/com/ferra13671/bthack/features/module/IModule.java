package com.ferra13671.bthack.features.module;

import com.ferra13671.bthack.features.category.ICategory;

public interface IModule {

    String getName();

    String getId();

    ICategory getCategory();

    String getDescriptionId();

    void setEnabled(boolean enabled);

    boolean isEnabled();
}
