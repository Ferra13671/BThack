package com.ferra13671.BThack.api.Effect.Color;

import java.awt.*;

public class ColorEffectBase {

    private volatile Color color;

    public ColorEffectBase(Color startColor) {
        this.color = startColor;
    }

    public void tick() {}

    public void reset() {}

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
