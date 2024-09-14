package com.ferra13671.BThack.api.Utils.System.buttons;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import net.minecraft.util.Identifier;

import java.awt.*;

public class Button implements Mc {

    private final int id;

    public double centerX;
    public double centerY;

    private int width;
    private int height;

    private Identifier texture = null;

    public String text;

    public boolean hovered;

    public boolean outline = false;



    public Button(int id, int x, int y, int width, int height, String text) {
        this.id = id;

        this.centerX = x;
        this.centerY = y;

        this.width = width;
        this.height = height;

        this.text = text;
    }

    public void updateButton(int mouseX, int mouseY) {
        this.hovered = isMouseOnButton(mouseX, mouseY);
    }

    public void mouseClicked(int mouseX, int mouseY) {}


    public int rectColor = new Color(0, 0, 0, 76).hashCode();

    public int whiteColor = new Color(255,255,255, 178).hashCode();
    public int alphaColor = new Color(255,255,255,0).hashCode();
    public void renderButton() {
        if (!this.hovered) {
            BThackRender.drawRect(getCenterX() - this.width, getCenterY() - this.height, getCenterX() + this.width, getCenterY() + this.height, rectColor);
        } else {
            BThackRender.drawRect(getCenterX() - this.width - 1, getCenterY() - this.height - 1, getCenterX() + this.width + 1, getCenterY() + this.height + 1, rectColor);

            BThackRender.drawHorizontalGradientRect((int)(getCenterX() - (this.width * 0.8)), getCenterY() + this.height - 4, getCenterX(), getCenterY() + this.height - 2, alphaColor, whiteColor);
            BThackRender.drawHorizontalGradientRect(getCenterX(), getCenterY() + this.height - 4, (int)(getCenterX() + (this.width * 0.8)), getCenterY() + this.height - 2, whiteColor, alphaColor);
        }
        if (outline)
            BThackRender.drawOutlineRect(getCenterX() - this.width, getCenterY() - this.height, getCenterX() + this.width, getCenterY() + this.height, 1, -1);
        BThackRender.drawString(getText(), (getCenterX() - (mc.textRenderer.getWidth(getText()) / 2)), (getCenterY() - (mc.textRenderer.fontHeight / 2)), -1);
    }



    public boolean isMouseOnButton(int mouseX, int mouseY) {
        return getCenterX() - width <= mouseX && mouseX <= getCenterX() + width && getCenterY() - height <= mouseY && mouseY <= getCenterY() + height;
    }

    public void keyTyped(int key) {}


    public int getId() {
        return this.id;
    }

    public int getCenterX() {
        return (int) centerX;
    }

    public int getCenterY() {
        return (int) centerY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getText() {
        if (text.startsWith("lang."))
            return LanguageSystem.translate(text);
        else
            return text;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setText(String text) {
        this.text = text;
    }
}
