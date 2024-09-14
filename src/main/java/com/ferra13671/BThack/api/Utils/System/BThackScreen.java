package com.ferra13671.BThack.api.Utils.System;


import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Storage.MusicStorage;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class BThackScreen extends Screen implements Mc {
    public ArrayList<Button> buttons = new ArrayList<>();

    public Button activeButton = new Button(Integer.MIN_VALUE, -100, -100, 1, 1, "nullButton");

    protected BThackScreen(Text title) {
        super(title);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        for (Button button : buttons) {
            button.updateButton(mouseX, mouseY);

            button.renderButton();
        }
        glDisable(GL_BLEND);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        activeButton = new Button(Integer.MIN_VALUE, -100, -100, 1, 1, "nullButton");

        for (Button button : buttons) {
            button.mouseClicked((int) mouseX, (int) mouseY);

            if (button.isMouseOnButton((int) mouseX, (int) mouseY)) {
                activeButton = button;
                MusicStorage.playSound(MusicStorage.buttonClicked, mc.options.getSoundVolume(SoundCategory.MASTER) / 2);
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Button button : buttons) {
            button.keyTyped(keyCode);
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public Button getButtonFromId(int id) {
        for (Button button : buttons) {
            if (button.getId() == id)
                return button;
        }
        return null;
    }
}
