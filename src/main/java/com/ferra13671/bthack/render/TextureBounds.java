package com.ferra13671.bthack.render;

public record TextureBounds(float u1, float v1, float u2, float v2) {
    public static TextureBounds FULL = new TextureBounds(0, 0, 1, 1);
}
