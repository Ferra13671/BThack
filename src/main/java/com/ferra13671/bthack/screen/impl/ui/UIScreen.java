package com.ferra13671.bthack.screen.impl.ui;

import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.managers.bind.Bind;
import com.ferra13671.bthack.managers.bind.BindController;
import com.ferra13671.bthack.managers.bind.BindListener;
import com.ferra13671.bthack.managers.bind.BindType;
import com.ferra13671.bthack.screen.BThackScreen;
import com.ferra13671.bthack.screen.api.MouseClick;
import com.ferra13671.bthack.screen.api.MouseScroll;
import com.ferra13671.bthack.utils.MouseUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.lwjgl.glfw.GLFW;

public class UIScreen extends BThackScreen {
    private final ClickUI clickUI = new ClickUI(this.eventBus);

    //TODO remove later
    public UIScreen() {
        BThackClient.getInstance().getBindManager().register(new BindListener(
                new Bind(GLFW.GLFW_KEY_RIGHT_SHIFT, BindType.Toggle, BindController.Keyboard),
                state -> {
                    if (state)
                        mc.setScreen(this);
                    else
                        mc.setScreen(null);
                },
                () -> mc.screen == this
        ));
    }

    @Override
    public void render(int mouesX, int mouseY) {
        this.clickUI.render(mouesX, mouseY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        BThackClient.getInstance().getExecutor().execute(() -> this.clickUI.mouseClicked(
                new MouseClick(
                        MouseUtils.getMouseX(),
                        MouseUtils.getMouseY(),
                        event.button()
                )
        ));

        return false;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        BThackClient.getInstance().getExecutor().execute(() -> this.clickUI.mouseReleased(
                new MouseClick(
                        MouseUtils.getMouseX(),
                        MouseUtils.getMouseY(),
                        event.button()
                )
        ));

        return false;
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
        BThackClient.getInstance().getExecutor().execute(() -> this.clickUI.mouseScrolled(
                new MouseScroll(
                        MouseUtils.getMouseX(),
                        MouseUtils.getMouseY(),
                        (float) scrollY
                )
        ));

        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        BThackClient.getInstance().getExecutor().execute(() -> this.clickUI.keyTyped(event.key()));

        return super.keyPressed(event);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        BThackClient.getInstance().getExecutor().execute(() -> this.clickUI.charTyped(event.codepoint()));

        return false;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {}

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
