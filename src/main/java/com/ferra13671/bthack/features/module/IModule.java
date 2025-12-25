package com.ferra13671.bthack.features.module;

import com.ferra13671.bthack.features.category.ICategory;
import com.ferra13671.bthack.managers.bind.Bind;

public interface IModule {

    String getName();

    String getId();

    Bind getBind();

    void setBind(Bind bind);

    ICategory getCategory();

    String getDescriptionId();

    void setEnabled(boolean enabled);

    boolean isEnabled();
}
