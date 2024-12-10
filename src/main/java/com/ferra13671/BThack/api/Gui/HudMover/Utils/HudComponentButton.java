package com.ferra13671.BThack.api.Gui.HudMover.Utils;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;

import java.awt.*;

public class HudComponentButton extends Button {
    public int x;
    public int y;

    public int dragX = 0;
    public int dragY = 0;
    private boolean isDragging = false;

    public final HudComponent hudComponent;


    public HudComponentButton(int id, HudComponent hudComponent) {
        super(id ,(int) (hudComponent.getX() + (hudComponent.width / 2)),(int) (hudComponent.getY() + (hudComponent.height / 2)),(int) (hudComponent.width / 2),(int) (hudComponent.height / 2), "");
        this.hudComponent = hudComponent;
    }

    short alpha = 255;
    boolean alphaInvert = true;

    @Override
    public void renderButton() {
        hudComponent.render();

        if (isDragging) {
            if (hudComponent.width < 0)
                BThackRender.drawOutlineRect((int) ((x + hudComponent.width) - 1), (int) ((y + hudComponent.height) - 1), x + 1, y + 1, 1, ColorUtils.rainbow(100));
            else
                BThackRender.drawOutlineRect(x - 1, y - 1, (int) (x + hudComponent.width + 1), (int) (y + hudComponent.height + 1), 1, ColorUtils.rainbow(100));
        }

        if (hovered) {
            if (!alphaInvert) {
                if (alpha < 255)
                    alpha += 3;
                else
                    alphaInvert = true;
            } else {
                if (alpha > 1)
                    alpha -= 3;
                else
                    alphaInvert = false;
            }

            Color hoveredColor = new Color(255,255,255, alpha);

            if (!isDragging) {
                if (hudComponent.width < 0)
                    BThackRender.drawOutlineRect((int) ((x + hudComponent.width) - 1), (int) ((y + hudComponent.height) - 1), x + 1, y + 1, 1, hoveredColor.hashCode());
                else
                    BThackRender.drawOutlineRect(x - 1, y - 1, (int) (x + hudComponent.width + 1), (int) (y + hudComponent.height + 1), 1, hoveredColor.hashCode());
            } else {
                if (hudComponent.width < 0)
                    BThackRender.drawOutlineRect((int) ((x + hudComponent.width) - 2), (int) ((y + hudComponent.height) - 2), x + 2, y + 2, 1, hoveredColor.hashCode());
                else
                    BThackRender.drawOutlineRect(x - 2, y - 2, (int) (x + hudComponent.width + 2), (int) (y + hudComponent.height + 2), 1, hoveredColor.hashCode());
            }
        }
    }

    public void setDrag(boolean drag) {
        isDragging = drag;
    }

    @Override
    public void updateButton(int mouseX, int mouseY) {
        super.updateButton(mouseX, mouseY);

        if (isDragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        } else {
            x = (int) hudComponent.getX();
            y = (int) hudComponent.getY();
        }

        hudComponent.setX(x, mc.getWindow().getScaledWidth());
        hudComponent.setY(y, mc.getWindow().getScaledHeight());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (isMouseOnButton(mouseX, mouseY)) {
                dragX = mouseX - x;
                dragY = mouseY - y;
                setDrag(true);
            }
        }
    }

    @Override
    public boolean isMouseOnButton(int mouseX, int mouseY) {
        return hudComponent.width < 0 ?
                (mouseX <= x && mouseX >= x + hudComponent.width && mouseY >= y && mouseY <= y + hudComponent.height) :
                (x <= mouseX && mouseX <= x + hudComponent.width && mouseY >= y && mouseY <= y + hudComponent.height);
    }
}
