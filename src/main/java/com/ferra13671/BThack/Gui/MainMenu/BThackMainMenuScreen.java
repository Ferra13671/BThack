package com.ferra13671.BThack.api.Gui.MainMenu;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Gui.MainMenu.SelectWallpaper.SelectWallpaperScreen;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import com.ferra13671.BThack.api.Utils.Texture.Texture;
import com.ferra13671.BThack.api.Utils.Texture.TextureColorMode;
import com.ferra13671.BThack.api.Utils.PathMode;
import com.ferra13671.BThack.impl.Modules.CLIENT.HUD;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.text.Text;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;

public class BThackMainMenuScreen extends BThackScreen {
    public static Texture mainMenuTexture = new Texture("assets/bthack/bthack_mainmenu.jpg", PathMode.INSIDEJAR, TextureColorMode.RGBA);

    public BThackMainMenuScreen() {
        super(Text.of("BThack Main Menu"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawWallpaper();

        BThackRender.drawHorizontalGradientRect(0,0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), new Color(0,0,0, 255).hashCode(), new Color(0,0,0, 0).hashCode());

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        BThackRender.drawTextureRect(HUD.bthack_logo, 20, 20, 20 + 138 * 2, 20 + 72 * 2);
        glDisable(GL_BLEND);

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void init() {
        super.init();

        this.buttons.clear();

        this.buttons.add(new Button(1, mc.getWindow().getScaledWidth() / 7, mc.getWindow().getScaledHeight() / 2,
                (mc.getWindow().getScaledWidth() / 7) - 5, 10,
                "lang.screen.mainmenu.singleplayer"));
        this.buttons.add(new Button(2, mc.getWindow().getScaledWidth() / 7, (mc.getWindow().getScaledHeight() / 2) + 22,
                (mc.getWindow().getScaledWidth() / 7) - 5, 10,
                "lang.screen.mainmenu.multiplayer"));
        this.buttons.add(new Button(3, mc.getWindow().getScaledWidth() / 7, (mc.getWindow().getScaledHeight() / 2) + 44,
                (mc.getWindow().getScaledWidth() / 7) - 5, 10,
                "lang.screen.mainmenu.options"));
        this.buttons.add(new Button(4, mc.getWindow().getScaledWidth() / 7, (mc.getWindow().getScaledHeight() / 2) + 66,
                (mc.getWindow().getScaledWidth() / 7) - 5, 10,
                "lang.screen.mainmenu.quit"));

        this.buttons.add(new Button(5, 32, mc.getWindow().getScaledHeight() - 12,
                30, 10,
                "Credits"));
        this.buttons.add(new Button(6, mc.getWindow().getScaledWidth() - 40, 15, 38, 10, "lang.screen.mainmenu.setwallpaper"));

        this.buttons.forEach(button -> button.outline = true);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        switch (activeButton.getId()) {
            case 1:
                mc.setScreen(new SelectWorldScreen(this));
                break;
            case 2:
                mc.setScreen(new MultiplayerScreen(this));
                break;
            case 3:
                mc.setScreen(new OptionsScreen(this, mc.options));
                break;
            case 4:
                mc.stop();
                break;
            case 5:
                mc.setScreen(new BThackCreditsScreen(this));
                break;
            case 6:
                mc.setScreen(new SelectWallpaperScreen());
                break;
        }

        return false;
    }

    public static void drawWallpaper() {
        BThackRender.drawTextureRect(mainMenuTexture, 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
    }
}
