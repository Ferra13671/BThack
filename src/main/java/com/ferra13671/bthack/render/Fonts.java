package com.ferra13671.bthack.render;

import com.ferra13671.bthack.loader.api.ClientLoader;
import com.ferra13671.bthack.loader.api.ResourcePath;
import com.ferra13671.bthack.render.font.TTFFont;
import lombok.SneakyThrows;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;

public enum Fonts {
    SEMIBOLD(ClientLoader.getResource(new ResourcePath("bthack-client", "SF-Pro-Rounded-Semibold.otf"))),
    LIGHT(ClientLoader.getResource(new ResourcePath("bthack-client", "SF-Pro-Rounded-Light.otf")));

    public final Font font;
    private final HashMap<Integer, TTFFont> fonts = new HashMap<>();

    @SneakyThrows
    Fonts(InputStream fontStream) {
        this.font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
    }

    public TTFFont getOrLoad(int size) {
        return this.fonts.computeIfAbsent(size, s -> new TTFFont(this.font.deriveFont((float) s)));
    }
}
