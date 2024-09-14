package com.ferra13671.BThack.api.Gui.HudMover;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.FileSystem.ConfigInit.ConfigInit;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Gui.HudMover.Utils.HudComponentButton;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.google.common.collect.Sets;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.io.IOException;
import java.util.Set;

public class HudMoverScreen extends Screen implements Mc {


    public HudMoverScreen(Text title) {
        super(title);
    }

    private final Set<HudComponentButton> hudComponentButtons = Sets.newHashSet();

    @Override
    public void init() {
        for (HudComponent component : Client.getAllHudComponents()) {
            hudComponentButtons.add(new HudComponentButton(0, component));
        }
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        BThackRender.draw4ColorRect( 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), new Color(0,0,0, 64).hashCode(), new Color(0,0,0, 64).hashCode(), new Color(161,0, 255, 128).hashCode(), new Color(255, 0, 0, 128).hashCode());

        for (HudComponentButton button : hudComponentButtons) {
            button.updateButton(mouseX, mouseY);
            button.renderButton();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for (HudComponentButton button : hudComponentButtons) {
            button.mouseClicked((int) mouseX, (int) mouseY);
        }
        return super.mouseClicked(mouseX,mouseY,mouseButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        for (HudComponentButton button : hudComponentButtons) {
            button.setDrag(false);
        }
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void removed() {
        try {
            ConfigInit.saveHudComponents();
        } catch (IOException ignored) {}
    }
}
