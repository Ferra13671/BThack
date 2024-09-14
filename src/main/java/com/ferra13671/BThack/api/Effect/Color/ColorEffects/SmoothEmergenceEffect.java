package com.ferra13671.BThack.api.Effect.Color.ColorEffects;

import com.ferra13671.BThack.api.Effect.Color.ColorEffectBase;

import java.awt.*;

public class SmoothEmergenceEffect extends ColorEffectBase {
    private final int needTicks;
    private final int startDelayTicks;
    private int currentTick = 0;

    private final int endAlpha;

    public SmoothEmergenceEffect(Color endColor, int delayTicks) {
        super(endColor);

        this.endAlpha = endColor.getAlpha();
        this.startDelayTicks = 0;

        setColor(new Color(endColor.getRed(), endColor.getGreen(), endColor.getBlue(), 0));

        this.needTicks = delayTicks;
    }

    public SmoothEmergenceEffect(Color endColor,int startDelayTicks, int delayTicks) {
        super(endColor);

        this.endAlpha = endColor.getAlpha();
        this.startDelayTicks = startDelayTicks;

        setColor(new Color(endColor.getRed(), endColor.getGreen(), endColor.getBlue(), 0));

        this.needTicks = delayTicks;
    }

    @Override
    public void tick() {
        int tick = currentTick - startDelayTicks;
        if (tick < 0) {
            currentTick++;
            return;
        }
        if (tick <= needTicks) {
            double factor = Math.ceil(((double) tick / (double) needTicks) * 100d);

            int alpha;

            if (factor < 100.0)
                alpha = (int) ((endAlpha / 100d) * factor);
            else
                alpha = endAlpha;

            Color color = getColor();

            if (alpha < 0) {
                alpha = 0;
            }

            if (alpha > 0 && alpha <= 255) {

                setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
            }
            currentTick++;
        }
    }

    @Override
    public void reset() {
        currentTick = 0;

        Color color = getColor();

        setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0));
    }
}
