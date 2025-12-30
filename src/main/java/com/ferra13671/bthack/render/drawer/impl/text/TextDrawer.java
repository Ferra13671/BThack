package com.ferra13671.bthack.render.drawer.impl.text;

import com.ferra13671.bthack.render.RectColors;
import com.ferra13671.bthack.render.RenderColor;
import com.ferra13671.bthack.render.drawer.Drawer;
import com.ferra13671.bthack.render.drawer.impl.ColoredTextureDrawer;
import com.ferra13671.bthack.render.font.Glyph;
import com.ferra13671.bthack.render.font.TTFFont;
import com.ferra13671.gltextureutils.GLTexture;
import com.ferra13671.gltextureutils.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TextDrawer implements Drawer {
    private final Map<GLTexture, ColoredTextureDrawer> drawers = new HashMap<>();
    private final TTFFont font;
    private final Runnable preDrawRunnable;

    public TextDrawer(TTFFont font) {
        this(font, null);
    }

    public TextDrawer(TTFFont font, Runnable preDrawRunnable) {
        this.font = font;
        this.preDrawRunnable = preDrawRunnable;
    }

    public TextDrawer text(RenderText text) {
        float x = text.getX();
        float y = text.getY();

        for (Pair<Supplier<RenderColor>, Character[]> component : text.getText().getComponents()) {
            RenderColor color = text.getColor().multiply(component.getLeft().get());

            for (char _char : component.getRight()) {
                Glyph glyph = this.font.getGlyph(_char);

                if (_char == '\n') {
                    x = text.getX();
                    y += glyph.height();
                }

                ColoredTextureDrawer drawer = this.drawers.computeIfAbsent(glyph.instance().getTexture(), texture -> new ColoredTextureDrawer().setTexture(texture));

                drawer.rectSized(
                        x,
                        y,
                        glyph.width(),
                        glyph.height(),
                        RectColors.oneColor(color),
                        glyph.bounds()
                );
                if (text.isShadow())
                    drawer.rectSized(
                            x + 1,
                            y + 1,
                            glyph.width(),
                            glyph.height(),
                            RectColors.oneColor(RenderColor.BLACK),
                            glyph.bounds()
                    );

                x += glyph.width();
            }
        }

        return this;
    }

    @Override
    public Drawer end() {
        this.drawers.forEach((_, drawer) -> drawer.end());
        return this;
    }

    @Override
    public Drawer makeStandalone() {
        this.drawers.forEach((_, drawer) -> drawer.makeStandalone());
        return this;
    }

    @Override
    public Drawer tryDraw() {
        if (this.preDrawRunnable != null)
            this.preDrawRunnable.run();

        this.drawers.forEach((_, drawer) -> drawer.tryDraw());
        return this;
    }

    @Override
    public void close() {
        this.drawers.forEach((_, drawer) -> drawer.close());
    }
}
