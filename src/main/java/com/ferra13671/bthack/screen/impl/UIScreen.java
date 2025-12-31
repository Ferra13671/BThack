package com.ferra13671.bthack.screen.impl;

import com.ferra13671.MegaEvents.eventbus.EventSubscriber;
import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.events.screen.RepositionElementsEvent;
import com.ferra13671.bthack.managers.bind.Bind;
import com.ferra13671.bthack.managers.bind.BindController;
import com.ferra13671.bthack.managers.bind.BindListener;
import com.ferra13671.bthack.managers.bind.BindType;
import com.ferra13671.bthack.render.RectColors;
import com.ferra13671.bthack.render.drawer.DrawerPool;
import com.ferra13671.bthack.render.drawer.StaticDrawer;
import com.ferra13671.bthack.render.drawer.impl.RoundedBlurDrawer;
import com.ferra13671.bthack.render.drawer.impl.RoundedOutlinedRectDrawer;
import com.ferra13671.bthack.screen.BThackScreen;
import com.ferra13671.bthack.utils.StyleConstants;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;

public class UIScreen extends BThackScreen {

    private final DrawerPool backgroundPool = new DrawerPool(
            new StaticDrawer<>(() ->
                new RoundedBlurDrawer()
                        .rectSized(
                                (mc.getWindow().getWidth() / 2f) - (StyleConstants.UI_WIDTH / 2f),
                                (mc.getWindow().getHeight() / 2f) - (StyleConstants.UI_HEIGHT / 2f),
                                StyleConstants.UI_WIDTH,
                                StyleConstants.UI_HEIGHT,
                                StyleConstants.UI_PLATE_ROUND_RADIUS
                        )
            ),
            new StaticDrawer<>(() ->
                new RoundedOutlinedRectDrawer()
                        .rectSized(
                                (mc.getWindow().getWidth() / 2f) - (StyleConstants.UI_WIDTH / 2f),
                                (mc.getWindow().getHeight() / 2f) - (StyleConstants.UI_HEIGHT / 2f),
                                StyleConstants.UI_WIDTH,
                                StyleConstants.UI_HEIGHT,
                                StyleConstants.UI_PLATE_ROUND_RADIUS,
                                StyleConstants.UI_PLATE_OUTLINE_SIZE,
                                RectColors.oneColor(StyleConstants.UI_PLATE_BACKGROUND_COLOR),
                                RectColors.oneColor(StyleConstants.UI_PLATE_OUTLINE_COLOR)
                        )
            )
    );

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
        this.backgroundPool.draw();
    }

    @EventSubscriber(event = RepositionElementsEvent.class)
    public void onRepositionElements() {
        this.backgroundPool.rebuild();
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {}

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
