package com.ferra13671.BThack.api.Utils.System.buttons;


import com.ferra13671.BThack.Core.Render.BThackRender;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderButton extends Button {

    public boolean dragging = false;
    public double value;
    public final double min;
    public final double max;

    private double renderWidth;

    public SliderButton(int id, int x, int y, int width, int height, String text, double defaultValue, double min, double max) {
        super(id, x, y, width, height, text);
        this.min = min;
        this.max = max;
        value = defaultValue;
    }

    @Override
    public void renderButton() {
        BThackRender.drawRect(getCenterX() - getWidth(), getCenterY() - getHeight(), getCenterX() + getWidth(), getCenterY() + getHeight(), rectColor);

        BThackRender.drawRect(getCenterX() - getWidth(), getCenterY() - getHeight(), getCenterX() - getWidth() + (int) renderWidth, getCenterY() + getHeight(), new Color(255,255,255,100).hashCode());

        BThackRender.drawOutlineRect(getCenterX() - getWidth(), getCenterY() - getHeight(), getCenterX() + getWidth(), getCenterY() + getHeight(), 1, Color.white.hashCode());

        BThackRender.drawString(getText() + ": " + value, getCenterX() - getWidth() + 3, getCenterY() - (int) (mc.textRenderer.fontHeight / 2f), Color.white.hashCode());
    }

    @Override
    public void updateButton(int mouseX, int mouseY) {
        int x = getCenterX() - getWidth();

        //double diff = Math.min((getWidth()), Math.max(0, mouseX - x));
        double diff = ((mouseX - x) / (getWidth() * 2d)) * 100;
        diff = Math.max(0, Math.min(100, diff));

        renderWidth = (getWidth() * 2) * ((value - min) / (max - min));

        if (dragging) {
            if (diff == 0) {
                value = min;
            } else {
                value = roundToPlace(((diff / 100) * (max - min) + min), 2);
            }
        }
    }

    private static double roundToPlace(double value, int places) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            dragging = true;
            updateButton(mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0)
            dragging = false;
    }
}
