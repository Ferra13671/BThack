package com.ferra13671.bthack.render;

import com.ferra13671.gltextureutils.atlas.TextureAtlas;

public class BThackTextureAtlases {

    public final TextureAtlas CATEGORIES = new TextureAtlas(
            "category.atlas",
            BThackRenderSystem.TEXTURES.getCategoryIcons()
    );

    public final TextureAtlas ICONS = new TextureAtlas(
            "icons",
            BThackRenderSystem.TEXTURES.getIcons()
    );

}
