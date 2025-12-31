package com.ferra13671.bthack.render;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.ARGB;
import org.joml.Vector4f;

import java.awt.*;

@AllArgsConstructor
@Getter
public class RenderColor {
    public static RenderColor TRANSLUCENT = RenderColor.of(0f, 0f, 0f, 0f);
    public static RenderColor WHITE = RenderColor.ofRGBA(-1);
    public static RenderColor BLACK = RenderColor.of(0, 0, 0, 255);

    private float[] color;

    public RenderColor multiply(RenderColor renderColor) {
        return RenderColor.of(
                this.color[0] * renderColor.getColor()[0],
                this.color[1] * renderColor.getColor()[1],
                this.color[2] * renderColor.getColor()[2],
                this.color[3] * renderColor.getColor()[3]
        );
    }

    public Vector4f toVector4f() {
        return new Vector4f(this.color[0], this.color[1], this.color[2], this.color[3]);
    }

    public static RenderColor of(Color color) {
        return new RenderColor(new float[]{color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f});
    }

    public static RenderColor of(int red, int green, int blue, int alpha) {
        return of(red / 255f, green / 255f, blue / 255f, alpha / 255f);
    }

    public static RenderColor of(float red, float green, float blue, float alpha) {
        return new RenderColor(new float[]{red, green, blue, alpha});
    }

    public static RenderColor ofRGBA(int rgba) {
        return new RenderColor(new float[]{ARGB.redFloat(rgba), ARGB.greenFloat(rgba), ARGB.blueFloat(rgba), ARGB.alphaFloat(rgba)});
    }

    public static RenderColor ofRGB(int rgb) {
        return new RenderColor(new float[]{ARGB.redFloat(rgb), ARGB.greenFloat(rgb), ARGB.blueFloat(rgb), 1f});
    }
}
