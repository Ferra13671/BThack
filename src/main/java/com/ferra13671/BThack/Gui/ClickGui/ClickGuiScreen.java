package com.ferra13671.BThack.api.Gui.ClickGui;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.FileSystem.ConfigInit.ConfigInit;
import com.ferra13671.BThack.api.Gui.ClickGui.component.Component;
import com.ferra13671.BThack.api.Gui.ClickGui.component.Frame;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.Button;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class ClickGuiScreen extends Screen implements Mc {
    public static ArrayList<Frame> frames;

    public static int color;

    public static int descriptionY;

    public ClickGuiScreen(Text title) {
        super(title);
        frames = new ArrayList<>();
        int frameX = 0;
        int frameY = 0;
        for(Module.Category category : Module.Category.values()) {
            Frame frame = new Frame(category);
            frame.setY(frameY);
            frame.setX(frameX);
            frames.add(frame);
            frameX += 100;
        }

        for (Frame frame : ClickGuiScreen.frames) {
            frame.refreshButtonEffects();
        }
    }

    @Override
    public void init() {
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        for(Frame frame : frames) {
            frame.renderFrame();
            frame.updatePosition(mouseX, mouseY);
            for(Component comp : frame.getButtons()) {
                comp.updateComponent(mouseX, mouseY);

            }

        }
        descriptionY = height - (height / 40);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for(Frame frame : frames) {
            if(frame.isWithinHeader((int) mouseX, (int) mouseY) && mouseButton == 0) {
                frame.setDrag(true);
                frame.dragX = (int) mouseX - frame.getX();
                frame.dragY = (int) mouseY - frame.getY();
            }
            if(frame.isWithinHeader((int) mouseX, (int) mouseY) && mouseButton == 1) {
                frame.setOpen(!frame.isOpen());
            }
            if(frame.isOpen()) {
                if(!frame.getButtons().isEmpty()) {
                    for(Component component : frame.getButtons()) {
                        component.mouseClicked((int) mouseX, (int) mouseY, mouseButton);
                    }
                }
            }
        }
        return super.mouseClicked(mouseX,mouseY,mouseButton);
    }

    public boolean firstIgnore = true;

    @Override
    public boolean keyPressed(int keyCode, int p_95592_, int p_95593_) {
        for(Frame frame : frames) {
            if(frame.isOpen() && keyCode != 1) {
                if(!frame.getButtons().isEmpty()) {
                    for(Component component : frame.getButtons()) {
                        component.keyTyped(keyCode);
                    }
                }
            }
        }

        if (keyCode == Client.getModuleByName("ClickGui").getKey()) {
            if (!firstIgnore) {
                ConfigInit.saveConfig();
                mc.setScreen(null);

                return true;
            } else {
                firstIgnore = false;
            }
        }

        switch (keyCode) {
            case KeyboardUtils.KEY_ESCAPE:
                mc.setScreen(null);
                break;
            case KeyboardUtils.KEY_LEFT:
            case KeyboardUtils.KEY_RIGHT:
            case KeyboardUtils.KEY_UP:
            case KeyboardUtils.KEY_DOWN:
                for(Frame frame : frames) {
                    frame.moveFrame(keyCode);
                }
        }

        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        for(Frame frame : frames) {
            frame.setDrag(false);
        }
        for(Frame frame : frames) {
            if(frame.isOpen()) {
                if(!frame.getButtons().isEmpty()) {
                    for(Component component : frame.getButtons()) {
                        component.mouseReleased((int) mouseX, (int) mouseY, state);
                    }
                }
            }
        }

        return super.mouseReleased(mouseX, mouseY, state);
    }

    public void clickGuiTick() {
        for (Frame frame : frames) {
            frame.tick();
        }
    }

    @Override
    public void removed() {
        ConfigInit.saveConfig();
        for (Frame frame : frames) {
            frame.refreshButtonEffects();
            for (Button component : frame.buttons) {
                component.open = false;
                component.parent.refresh();
            }
        }
    }
}
