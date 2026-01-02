package com.ferra13671.bthack.render;

import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.features.category.ICategory;
import com.ferra13671.bthack.loader.api.ResourcePath;
import com.ferra13671.gltextureutils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BThackTextures {

    public final GLTexture COMBAT_CATEGORY_ICON = BThackRenderSystem.TEXTURE_LOADER.createTextureBuilder()
            .name("category.combat.icon")
            .info(new ResourcePath("bthack-client", "textures/combat.png"))
            .filtering(TextureFiltering.DEFAULT)
            .wrapping(TextureWrapping.DEFAULT)
            .build();

    public final GLTexture MISC_CATEGORY_ICON = BThackRenderSystem.TEXTURE_LOADER.createTextureBuilder()
            .name("category.misc.icon")
            .info(new ResourcePath("bthack-client", "textures/misc.png"))
            .filtering(TextureFiltering.DEFAULT)
            .wrapping(TextureWrapping.DEFAULT)
            .build();

    public final GLTexture PLAYER_CATEGORY_ICON = BThackRenderSystem.TEXTURE_LOADER.createTextureBuilder()
            .name("category.player.icon")
            .info(new ResourcePath("bthack-client", "textures/player.png"))
            .filtering(TextureFiltering.DEFAULT)
            .wrapping(TextureWrapping.DEFAULT)
            .build();

    public final GLTexture RENDER_CATEGORY_ICON = BThackRenderSystem.TEXTURE_LOADER.createTextureBuilder()
            .name("category.misc.icon")
            .info(new ResourcePath("bthack-client", "textures/render.png"))
            .filtering(TextureFiltering.DEFAULT)
            .wrapping(TextureWrapping.DEFAULT)
            .build();

    public final GLTexture CLIENT_ICON = BThackRenderSystem.TEXTURE_LOADER.createTextureBuilder()
            .name("client.icon")
            .info(new ResourcePath("bthack-client", "textures/icon.png"))
            .filtering(TextureFiltering.SMOOTH)
            .wrapping(TextureWrapping.DEFAULT)
            .build();

    public final GLTexture MENU_ICON = BThackRenderSystem.TEXTURE_LOADER.createTextureBuilder()
            .name("menu.icon")
            .info(new ResourcePath("bthack-client", "textures/menu.png"))
            .filtering(TextureFiltering.SMOOTH)
            .wrapping(TextureWrapping.DEFAULT)
            .build();

    public List<GLTexture> getCategoryIcons() {
        return new ArrayList<>(BThackClient.getInstance().getCategoryManager().getCategories()
                .stream()
                .map(ICategory::getIcon)
                .toList());
    }

    public List<GLTexture> getIcons() {
        return new ArrayList<>(Arrays.asList(
                CLIENT_ICON,
                MENU_ICON
        ));
    }
}
