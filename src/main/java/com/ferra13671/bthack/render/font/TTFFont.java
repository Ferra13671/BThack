package com.ferra13671.bthack.render.font;

import com.ferra13671.bthack.render.RenderColor;
import com.ferra13671.bthack.render.drawer.impl.text.FormattedText;
import com.ferra13671.gltextureutils.Pair;
import io.netty.util.collection.IntObjectHashMap;

import java.awt.*;
import java.util.function.Supplier;

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

    public float getTextHeight() {
        return getGlyph('a').height();
    }

    public float getTextWidth(String s) {
        return getTextWidth(new FormattedText(s));
    }

    public float getTextWidth(FormattedText text) {
        float maxW = 0;
        float w = 0;

        for (Pair<Supplier<RenderColor>, Character[]> component : text.getComponents()) {
            for (char _char : component.getRight()) {
                if (_char == '\n') {
                    maxW = Math.max(maxW, w);
                    w = 0;

                    continue;
                }

                w += getGlyph(_char).width();
            }
        }

        return Math.max(maxW, w);
    }
}
