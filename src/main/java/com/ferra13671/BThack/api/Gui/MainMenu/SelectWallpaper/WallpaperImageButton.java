package com.ferra13671.BThack.api.Gui.MainMenu.SelectWallpaper;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.ColourUtils;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;

public class WallpaperImageButton extends Button {
    public final Wallpaper wallpaper;

    public boolean selected;

    private int offset;

    public WallpaperImageButton(int id, int x, int y, int width, int height, Wallpaper wallpaper) {
        super(id, x, y, width, height, wallpaper.filename());

        this.wallpaper = wallpaper;
    }

    public void setOffset(int newOffset) {
        offset = newOffset;
    }


    @Override
    public void renderButton() {
        super.renderButton();

        if (selected)
            BThackRender.drawOutlineRect(getCenterX() - getWidth(), getCenterY() - getHeight(), getCenterX() + getWidth(), getCenterY() + getHeight(), 1, ColourUtils.rainbow(100));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        this.selected = true;
    }

    @Override
    public int getCenterY() {
        return (int) centerY + ((offset * (getHeight() * 2)));
    }
}
