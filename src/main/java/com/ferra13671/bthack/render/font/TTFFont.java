package com.ferra13671.bthack.render.font;

import io.netty.util.collection.IntObjectHashMap;

import java.awt.*;

public class TTFFont {
    public static final int DEFAULT_GLYPHS_IN_MAP = 500;

    private final Font font;
    private final int glyphsInMap;
    private final IntObjectHashMap<GlyphMap> glyphMaps = new IntObjectHashMap<>();

    public TTFFont(Font font) {
        this(font, DEFAULT_GLYPHS_IN_MAP);
    }

    public TTFFont(Font font, int glyphsInMap) {
        this.font = font;
        this.glyphsInMap = glyphsInMap;
    }

    public Glyph getGlyph(char _char) {
        GlyphMap glyphMap = this.glyphMaps.computeIfAbsent(_char - (_char % this.glyphsInMap), index -> new GlyphMap(this.font, index, this.glyphsInMap));

        return glyphMap.getGlyph(_char);
    }
}
