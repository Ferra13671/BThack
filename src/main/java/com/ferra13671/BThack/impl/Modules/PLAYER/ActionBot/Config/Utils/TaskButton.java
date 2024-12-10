package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils;



import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;

import java.awt.*;

import static com.ferra13671.BThack.api.Gui.ActionBot.ActionBotConfigGui.*;

public class TaskButton extends Button {
    private boolean selected;
    private int offset;
    public final ActionBotTask task;


    public TaskButton(int id, String taskName, int offset, ActionBotTask task) {
        super(id ,(int) (widthFactor * 9), (int) (((heightFactor * 8) + ((heightFactor * offsetFactor) * offset)) + (heightFactor / 2)), (int) ((mc.textRenderer.getWidth(taskName) / 2f) * 1.2), (int) (heightFactor / 2), taskName);

        this.offset = offset;
        this.task = task;
    }

    short alpha = 255;
    boolean alphaIvert = true;

    @Override
    public void renderButton() {
        float stringWidth = (mc.textRenderer.getWidth(this.text) / 2f);

        BThackRender.drawRect((int) (this.getCenterX() - (stringWidth * 1.2)), (int) (this.getCenterY() - (heightFactor / 2)), (int) (this.getCenterX() + (stringWidth * 1.2)), (int) (this.getCenterY() + (heightFactor / 2)), Color.black.hashCode());
        BThackRender.drawString(this.text, (int) (this.getCenterX() - stringWidth), (int) ((heightFactor * 8) + ((heightFactor * offsetFactor) * offset) + (((heightFactor * offsetFactor) / 2) - (mc.textRenderer.fontHeight))), Color.white.hashCode());

        if (selected) {
            BThackRender.drawOutlineRect((int) (this.getCenterX() - (stringWidth * 1.2) - 1), (int) (this.getCenterY() - (heightFactor / 2) - 1), (int) (this.getCenterX() + (stringWidth * 1.2) + 1), (int) (this.getCenterY() + (heightFactor / 2) + 1), 1, ColorUtils.rainbow(100));
        }

        if (hovered) {
            if (!alphaIvert) {
                if (alpha < 255) {
                    alpha += 3;
                } else {
                    alphaIvert = true;
                }
            } else {
                if (alpha > 1) {
                    alpha -= 3;
                } else {
                    alphaIvert = false;
                }
            }

            Color hoveredColor = new Color(255,255,255, alpha);

            if (!selected) {
                BThackRender.drawOutlineRect((int) (this.getCenterX() - (stringWidth * 1.2) - 1), (int) (this.getCenterY() - (heightFactor / 2) - 1), (int) (this.getCenterX() + (stringWidth * 1.2) + 1), (int) (this.getCenterY() + (heightFactor / 2) + 1), 1, hoveredColor.hashCode());
            } else {
                BThackRender.drawOutlineRect((int) (this.getCenterX() - (stringWidth * 1.2) - 2), (int) (this.getCenterY() - (heightFactor / 2) - 2), (int) (this.getCenterX() + (stringWidth * 1.2) + 2), (int) (this.getCenterY() + (heightFactor / 2) + 2), 1, hoveredColor.hashCode());
            }
        }
    }

    @Override
    public void updateButton(int mouseX, int mouseY) {
        super.updateButton(mouseX, mouseY);


    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            selected = isMouseOnButton(mouseX, mouseY);
        }
    }


    public void setOffset(int offset) {
        this.offset = offset;

        this.setCenterY((int) (((heightFactor * 8) + ((heightFactor * offsetFactor) * offset)) + (heightFactor / 2)));
    }

    public boolean isSelected() {
        return this.selected;
    }
}
