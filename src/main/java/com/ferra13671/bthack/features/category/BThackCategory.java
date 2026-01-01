package com.ferra13671.bthack.features.category;

import com.ferra13671.gltextureutils.GLTexture;

public record BThackCategory(String getName, String getId, GLTexture getIcon) implements ICategory {
}
