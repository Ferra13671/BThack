package com.ferra13671.bthack.render.font;

import com.ferra13671.bthack.render.TextureBounds;

public record Glyph(char symbol, TextureBounds bounds, float width, float height, GlyphMap instance) {
}
