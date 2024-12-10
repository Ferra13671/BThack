package com.ferra13671.BThack.api.Gui.ClickGui;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.Core.Render.Utils.RainbowUtils;
import com.ferra13671.BThack.api.Category.Categories;
import com.ferra13671.BThack.api.Category.Category;
import com.ferra13671.BThack.api.Gui.ClickGui.component.Component;
import com.ferra13671.BThack.api.Gui.ClickGui.component.Frame;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.ModuleButton;
import com.ferra13671.BThack.api.Gui.ClickGui.component.components.setting.settings.Slider;
import com.ferra13671.BThack.api.Gui.Config.LoadConfigScreen;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.PluginModule;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import com.ferra13671.BThack.api.Utils.System.buttons.SliderButton;
import com.ferra13671.BThack.api.Utils.System.buttons.TextFrameButton;
import com.ferra13671.BThack.api.Utils.Ticker;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickGuiScreen extends BThackScreen implements Mc {
    public static final ArrayList<Frame> frames = new ArrayList<>();
    public static int descriptionY;

    public boolean startSaving = false;

    private ScreenGetter instanceScreen;


    public static SliderButton guiScaleSlider;

    public Module descriptionModule;
    public Slider writingSlider = null;

    private final Ticker ticker = new Ticker();

    public ClickGuiScreen(Text title) {
        super(title);
        int frameX = 0;
        int frameY = 0;
        for (Category category : Categories.getCategories()) {
            Frame frame = new Frame(category);
            frame.setY(frameY);
            frame.setX(frameX);
            frames.add(frame);
            frameX += 100;
            frame.refreshButtonEffects();
        }

        ticker.reset();
    }

    @Override
    public void onDisplayed() {
        ModuleList.clickGui.updateColorTheme();
        ThreadManager.startNewThread(thread -> {
            while (mc.currentScreen == BThack.instance.clickGui) {
                if (ticker.passed(10)) {
                    clickGuiTick();
                    ticker.reset();
                }
            }
        });
    }

    public void setInstanceScreen(ScreenGetter screen) {
        instanceScreen = screen;
    }

    @Override
    public void init() {
        buttons.clear();

        buttons.add(Button.of(0,
                mc.getWindow().getScaledWidth() - 50, mc.getWindow().getScaledHeight() - 15, 40, 10, "Load Config"));
        buttons.add(Button.of(1,
                mc.getWindow().getScaledWidth() - 50, mc.getWindow().getScaledHeight() - 40, 40, 10, "Save Config"));
        buttons.add(new TextFrameButton(8,
                mc.getWindow().getScaledWidth() - 70, mc.getWindow().getScaledHeight() - 65, 60, 10));
        buttons.add(Button.of(9
                , mc.getWindow().getScaledWidth() - 150, mc.getWindow().getScaledHeight() - 40, 40, 10, "Confirm"));

        guiScaleSlider = new SliderButton(10, mc.getWindow().getScaledWidth() / 2, mc.getWindow().getScaledHeight() - 15, 50, 10, "Gui Scale", ClickGui.guiScale.getValue(), 0.5, 1.5);
        buttons.add(guiScaleSlider);

        getButtonFromId(8).hided = !startSaving;
        getButtonFromId(9).hided = !startSaving;


        buttons.forEach(button -> button.outline = true);
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (instanceScreen != null) {
            instanceScreen.getRenderScreen().render(guiGraphics, mouseX, mouseY, partialTicks);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        boolean needUpdate = true;
        for (int i = frames.size() - 1; i > -1; i--) {
            Frame frame = frames.get(i);
            for (ModuleButton button : frame.getButtons()) {
                if (!needUpdate) {
                    button.resetHovered();
                    continue;
                }
                if (!button.updateComponent(mouseX, mouseY)) {
                    needUpdate = false;
                }

            }
        }

        BThackRender.guiGraphics.getMatrices().push();
        BThackRender.guiGraphics.getMatrices().scale((float) ClickGui.guiScale.getValue(), (float) ClickGui.guiScale.getValue(), 1);
        for(Frame frame : frames) {
            frame.renderFrame();
            frame.updatePosition((int) (mouseX / ClickGui.guiScale.getValue()), (int) (mouseY / ClickGui.guiScale.getValue()));
        }

        BThackRender.guiGraphics.getMatrices().translate(1,1, 10);

        int length;
        int rainbowType = (int) ClickGui.rainbowSpeed.getValue();
        float speed = RainbowUtils.getRainbowRectSpeed(rainbowType)[0];
        int delay = (int) RainbowUtils.getRainbowRectSpeed(rainbowType)[1];
        if (descriptionModule != null) {
            if (descriptionModule instanceof PluginModule pluginMod) {
                int pluginNameLength = mc.textRenderer.getWidth("Plugin: " + pluginMod.plugin.pluginName) + 10;
                int descriptionLength = mc.textRenderer.getWidth(descriptionModule.getDescription()) + 10;
                length = Math.max(pluginNameLength, descriptionLength);
                BThackRender.drawRect(1, ClickGuiScreen.descriptionY - 17, length, (ClickGuiScreen.descriptionY + 12), new Color(Client.clientInfo.getColorTheme().getBackgroundFontColour()).hashCode());
                BThackRender.drawOutlineRect(1, ClickGuiScreen.descriptionY - 17, length, (ClickGuiScreen.descriptionY + 12), 1, ColorUtils.rainbow(delay, speed));
                BThackRender.drawString(descriptionModule.getDescription(), 6, (ClickGuiScreen.descriptionY - 1), Client.clientInfo.getColorTheme().getModuleDisabledColour());
                BThackRender.drawString("Plugin: " + pluginMod.plugin.pluginName, 6, (ClickGuiScreen.descriptionY - 12), Client.clientInfo.getColorTheme().getModuleDisabledColour());
            } else {
                length = mc.textRenderer.getWidth(descriptionModule.getDescription()) + 10;
                BThackRender.drawRect(1, ClickGuiScreen.descriptionY - 5, length, (ClickGuiScreen.descriptionY + 12), new Color(Client.clientInfo.getColorTheme().getBackgroundFontColour()).hashCode());
                BThackRender.drawOutlineRect(1, ClickGuiScreen.descriptionY - 5, length, (ClickGuiScreen.descriptionY + 12), 1, ColorUtils.rainbow(delay, speed));
                BThackRender.drawString(descriptionModule.getDescription(), 6, (ClickGuiScreen.descriptionY - 1), Client.clientInfo.getColorTheme().getModuleDisabledColour());
            }
        }
        BThackRender.guiGraphics.getMatrices().pop();
        if (writingSlider != null) {
            if (writingSlider.writing) {
                BThackRender.drawString("New Value: " + writingSlider.textBuilder, (int) ((mc.getWindow().getScaledWidth() / 2f) - (mc.textRenderer.getWidth("New Value: " + writingSlider.textBuilder) / 2)), (mc.getWindow().getScaledHeight() - 45), Color.white.hashCode());
            }
        }

        descriptionY = (int) ((height - (height / 40)) / ClickGui.guiScale.getValue());
        descriptionModule = null;
    }

    @Override
    public void tick() {
        ClickGui.guiScale.setValue(guiScaleSlider.value);
        if (instanceScreen != null) {
            instanceScreen.getRenderScreen().tick();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        boolean needClickButtons = true;
        for(Frame frame : frames) {
            if(frame.isWithinHeader((int) mouseX, (int) mouseY) && mouseButton == 0) {
                frame.setDrag(true);
                frame.dragX = (int) (mouseX / ClickGui.guiScale.getValue()) - frame.getX();
                frame.dragY = (int) (mouseY / ClickGui.guiScale.getValue()) - frame.getY();
                return false;
            }
            if(frame.isWithinHeader((int) mouseX, (int) mouseY) && mouseButton == 1) {
                frame.setOpen(!frame.isOpen());
                return false;
            }
            if(frame.isOpen()) {
                if(!frame.getButtons().isEmpty()) {
                    for(Component component : frame.getButtons()) {
                        if (component.mouseClicked((int) mouseX, (int) mouseY, mouseButton))
                            needClickButtons = false;
                    }
                }
            }
        }

        if (needClickButtons) {
            super.mouseClicked(mouseX, mouseY, mouseButton);

            if (activeButton.getId() == 0) {
                startSaving = false;
                init();
                mc.setScreen(new LoadConfigScreen());
            }
            if (activeButton.getId() == 1) {
                startSaving = true;
                init();
            }
            if (activeButton.getId() == 9) {
                TextFrameButton button = (TextFrameButton) getButtonFromId(8);

                try {
                    ConfigSystem.saveConfigFile(button.getText());
                } catch (IOException ignored) {}
                startSaving = false;
                button.setText("");
                init();
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (Frame frame : frames)
            frame.moveFrame(horizontalAmount, verticalAmount);
        return false;
    }

    public boolean firstIgnore = true;



    @Override
    public boolean keyPressed(int keyCode, int scanCode, int shift) {
        for(Frame frame : frames) {
            if(frame.isOpen() && keyCode != 1) {
                if(!frame.getButtons().isEmpty()) {
                    for(Component component : frame.getButtons()) {
                        component.keyTyped(keyCode);
                    }
                }
            }
        }

        if (keyCode == ModuleList.clickGui.getKey()) {
            if (!firstIgnore) {
                ConfigSystem.saveConfig();
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

        return super.keyPressed(keyCode, scanCode, shift);
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
        ConfigSystem.saveConfig();
        for (Frame frame : frames) {
            frame.refreshButtonEffects();
            for (ModuleButton component : frame.buttons) {
                component.open = false;
                component.parent.refresh();
            }
        }
        instanceScreen = null;
    }


    public interface ScreenGetter {

        Screen getRenderScreen();
    }
}
