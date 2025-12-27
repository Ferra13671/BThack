package com.ferra13671.bthack.render;

public record RectColors(float[] x1y1Color, float[] x1y2Color, float[] x2y2Color, float[] x2y1Color) {

    public static RectColors oneColor(float[] color) {
        return new RectColors(color, color, color, color);
    }

    public static RectColors horizontalGradient(float[] left, float[] right) {
        return new RectColors(left, left, right, right);
    }

    public static RectColors verticalGradient(float[] up, float[] down) {
        return new RectColors(up, down, down, up);
    }
}
