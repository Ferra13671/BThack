package com.ferra13671.bthack.render.font;

import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.gltextureutils.GLTexture;
import com.ferra13671.gltextureutils.TextureFiltering;
import com.ferra13671.gltextureutils.TextureWrapping;
import com.ferra13671.gltextureutils.loader.TextureLoaders;
import io.netty.util.collection.IntObjectHashMap;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Closeable;

public class GlyphMap implements Closeable {
    @Getter
    private final Font font;
    @Getter
    private final int index;
    @Getter
    private final int glyphsInMap;
    private IntObjectHashMap<Glyph> glyphs;
    @Getter
    private GLTexture texture;

    public GlyphMap(Font font, int index, int glyphsInMap) {
        this.font = font;
        this.index = index;
        this.glyphsInMap = glyphsInMap;

        generate(1024, 1024);
    }

    private void generate(int width, int height) {
        IntObjectHashMap<Glyph> glyphs = new IntObjectHashMap<>();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        graphics.setFont(this.font);
        FontMetrics fontMetrics = graphics.getFontMetrics();

        float xStep = 0;
        float yStep = 0;

        float symbolHeight = fontMetrics.getAscent() + fontMetrics.getDescent();

        for (int i = 0; i < this.glyphsInMap; i++) {
            char symbol = (char) (this.index + i);
            String s = Character.toString(symbol);

            float symbolWidth = fontMetrics.charWidth(symbol);

            if (xStep + symbolWidth > width) {
                yStep += symbolHeight;
                xStep = 0;
            }

            if (yStep + symbolHeight > height) {
                BThackClient.getInstance().getLogger().info("GlyphMap requires a texture with sizes greater than {}x{}. The texture size has been changed to {}x{}.", width, height, width * 2, height * 2);
                graphics.dispose();
                generate(width * 2, height * 2);
                return;
            }

            graphics.drawString(s, xStep, yStep + fontMetrics.getAscent());
            Glyph glyph = new Glyph(
                    symbol,
                    xStep / width,
                    yStep / height,
                    (xStep + symbolWidth) / width,
                    (yStep + symbolHeight) / height,
                    symbolWidth,
                    symbolHeight,
                    this
            );
            glyphs.put(symbol, glyph);

            xStep += symbolWidth;
        }

        this.glyphs = glyphs;
        BThackRenderSystem.registerRenderCall(() ->
            this.texture = TextureLoaders.BUFFERED_IMAGE.createTextureBuilder()
                    .name(String.format("GlyphMap[%s, %s]", this.font.getName(), this.index))
                    .info(bufferedImage)
                    .filtering(TextureFiltering.SMOOTH)
                    .wrapping(TextureWrapping.DEFAULT)
                    .build()
        );
    }

    public Glyph getGlyph(char _char) {
        return this.glyphs.get(_char);
    }

    @Override
    public void close() {
        this.texture.delete();
    }
}
