package com.ferra13671.bthack.render;

import com.ferra13671.gltextureutils.GLTexture;
import com.ferra13671.gltextureutils.PathMode;
import com.ferra13671.gltextureutils.TextureFiltering;
import com.ferra13671.gltextureutils.TextureWrapping;
import com.ferra13671.gltextureutils.loader.FileEntry;
import com.ferra13671.gltextureutils.loader.TextureLoaders;

public class BThackTextures {

    public final GLTexture combatCategoryIcon = TextureLoaders.FILE_ENTRY.createTextureBuilder()
            .name("category.combat.icon")
            .info(new FileEntry("assets/bthack-client/textures/combat.png", PathMode.INSIDE_JAR))
            .filtering(TextureFiltering.DEFAULT)
            .wrapping(TextureWrapping.DEFAULT)
            .build();

    public final GLTexture miscCategoryIcon = TextureLoaders.FILE_ENTRY.createTextureBuilder()
            .name("category.misc.icon")
            .info(new FileEntry("assets/bthack-client/textures/misc.png", PathMode.INSIDE_JAR))
            .filtering(TextureFiltering.DEFAULT)
            .wrapping(TextureWrapping.DEFAULT)
            .build();

    public final GLTexture playerCategoryIcon = TextureLoaders.FILE_ENTRY.createTextureBuilder()
            .name("category.player.icon")
            .info(new FileEntry("assets/bthack-client/textures/player.png", PathMode.INSIDE_JAR))
            .filtering(TextureFiltering.DEFAULT)
            .wrapping(TextureWrapping.DEFAULT)
            .build();

    public final GLTexture renderCategoryIcon = TextureLoaders.FILE_ENTRY.createTextureBuilder()
            .name("category.misc.icon")
            .info(new FileEntry("assets/bthack-client/textures/render.png", PathMode.INSIDE_JAR))
            .filtering(TextureFiltering.DEFAULT)
            .wrapping(TextureWrapping.DEFAULT)
            .build();
}
