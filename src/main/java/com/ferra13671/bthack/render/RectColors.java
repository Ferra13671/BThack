package com.ferra13671.bthack.render;

public record RectColors(RenderColor x1y1Color, RenderColor x1y2Color, RenderColor x2y2Color, RenderColor x2y1Color) {

    public static RectColors oneColor(RenderColor color) {
        return new RectColors(color, color, color, color);
    }

    public static RectColors horizontalGradient(RenderColor left, RenderColor right) {
        return new RectColors(left, left, right, right);
    }

    public static RectColors verticalGradient(RenderColor up, RenderColor down) {
        return new RectColors(up, down, down, up);
    }
}
