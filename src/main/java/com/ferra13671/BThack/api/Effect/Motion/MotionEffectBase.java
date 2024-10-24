package com.ferra13671.BThack.api.Effect.Motion;

public class MotionEffectBase {
    private final int startX;
    private final int startY;

    private final int endX;
    private final int endY;

    private int currentX;
    private int currentY;

    public MotionEffectBase(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;

        this.endX = endX;
        this.endY = endY;
    }

    public void tick() {}

    public void reset() {}



    public int getCurrentX() {
        return this.currentX;
    }

    public int getCurrentY() {
        return this.currentY;
    }

    public int getStartX() {
        return this.startX;
    }

    public int getStartY() {
        return this.startY;
    }

    public int getEndX() {
        return this.endX;
    }

    public int getEndY() {
        return this.endY;
    }
}
