package com.ferra13671.bthack.render;

import com.ferra13671.gltextureutils.*;
import com.ferra13671.gltextureutils.loader.FileEntry;
import com.ferra13671.gltextureutils.loader.TextureLoaders;

public class BThackTextures {

    public final GLTexture COMBAT_CATEGORY_ICON = TextureLoaders.FILE_ENTRY.createTextureBuilder()
            .name("category.combat.icon")
            .info(new FileEntry("assets/bthack-client/textures/combat.png", PathMode.INSIDE_JAR))
            .filtering(TextureFiltering.DEFAULT)
            .wrapping(TextureWrapping.DEFAULT)
            .build();

    public final GLTexture MISC_CATEGORY_ICON = TextureLoaders.FILE_ENTRY.createTextureBuilder()
            .name("category.misc.icon")
            .info(new FileEntry("assets/bthack-client/textures/misc.png", PathMode.INSIDE_JAR))
            .filtering(TextureFiltering.DEFAULT)
            .wrapping(TextureWrapping.DEFAULT)
            .build();

    public final GLTexture PLAYER_CATEGORY_ICON = TextureLoaders.FILE_ENTRY.createTextureBuilder()
            .name("category.player.icon")
            .info(new FileEntry("assets/bthack-client/textures/player.png", PathMode.INSIDE_JAR))
            .filtering(TextureFiltering.DEFAULT)
            .wrapping(TextureWrapping.DEFAULT)
            .build();

    public final GLTexture RENDER_CATEGORY_ICON = TextureLoaders.FILE_ENTRY.createTextureBuilder()
            .name("category.misc.icon")
            .info(new FileEntry("assets/bthack-client/textures/render.png", PathMode.INSIDE_JAR))
            .filtering(TextureFiltering.DEFAULT)
            .wrapping(TextureWrapping.DEFAULT)
            .build();
}
