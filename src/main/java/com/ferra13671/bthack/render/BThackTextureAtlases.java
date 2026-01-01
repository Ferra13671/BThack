package com.ferra13671.bthack.render;

import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.features.category.ICategory;
import com.ferra13671.gltextureutils.atlas.TextureAtlas;

import java.util.ArrayList;

public class BThackTextureAtlases {

    public final TextureAtlas CATEGORIES = new TextureAtlas(
            "category.atlas",
            new ArrayList<>(BThackClient.getInstance().getCategoryManager().getCategories()
                    .stream()
                    .map(ICategory::getIcon)
                    .toList())
    );

}
