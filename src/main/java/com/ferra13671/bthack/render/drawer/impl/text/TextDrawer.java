package com.ferra13671.bthack.render.drawer.impl.text;

import com.ferra13671.bthack.render.RectColors;
import com.ferra13671.bthack.render.RenderColor;
import com.ferra13671.bthack.render.drawer.Drawer;
import com.ferra13671.bthack.render.drawer.impl.ColoredTextureRectDrawer;
import com.ferra13671.bthack.render.font.Glyph;
import com.ferra13671.bthack.render.font.TTFFont;
import com.ferra13671.gltextureutils.GLTexture;
import com.ferra13671.gltextureutils.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

//TODO text formatting
public class TextDrawer implements Drawer {
    private final Map<GLTexture, ColoredTextureRectDrawer> drawers = new HashMap<>();
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

                ColoredTextureRectDrawer drawer = this.drawers.computeIfAbsent(glyph.instance().getTexture(), texture -> new ColoredTextureRectDrawer().setTexture(texture));

                drawer.rectSized(
                        x,
                        y,
                        glyph.width(),
                        glyph.height(),
                        glyph.u1(),
                        glyph.v1(),
                        glyph.u2(),
                        glyph.v2(),
                        RectColors.oneColor(color)
                );
                if (text.isShadow())
                    drawer.rectSized(
                            x + 1,
                            y + 1,
                            glyph.width(),
                            glyph.height(),
                            glyph.u1(),
                            glyph.v1(),
                            glyph.u2(),
                            glyph.v2(),
                            RectColors.oneColor(RenderColor.BLACK)
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
