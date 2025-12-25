package com.ferra13671.bthack.render;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.ARGB;
import org.joml.Vector4f;

import java.awt.*;

@AllArgsConstructor
@Getter
public class RenderColor {
    private float[] color;

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

    public static RenderColor of(int rgba) {
        return new RenderColor(new float[]{ARGB.redFloat(rgba), ARGB.greenFloat(rgba), ARGB.blueFloat(rgba), ARGB.alphaFloat(rgba)});
    }
}
