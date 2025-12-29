package com.ferra13671.bthack.render.drawer.impl.text;

import com.ferra13671.bthack.render.RenderColor;
import lombok.Getter;

@Getter
public class RenderText {
    private FormattedText text;
    private float x;
    private float y;
    private RenderColor color = RenderColor.WHITE;
    private boolean shadow = false;

    public RenderText(String s, float x, float y) {
        this(new FormattedText(s), x, y);
    }

    public RenderText(FormattedText text, float x, float y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public RenderText withText(String s) {
        return withText(new FormattedText(s));
    }

    public RenderText withText(FormattedText text) {
        this.text = text;
        return this;
    }

    public RenderText withX(float x) {
        this.x = x;
        return this;
    }

    public RenderText withY(float y) {
        this.y = y;
        return this;
    }

    public RenderText withColor(RenderColor color) {
        this.color = color;
        return this;
    }

    public RenderText withShadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }
}
