package com.ferra13671.bthack.screen.impl.ui;

import com.ferra13671.MegaEvents.eventbus.EventSubscriber;
import com.ferra13671.MegaEvents.eventbus.IEventBus;
import com.ferra13671.bthack.events.screen.RepositionElementsEvent;
import com.ferra13671.bthack.render.*;
import com.ferra13671.bthack.render.drawer.DrawerPool;
import com.ferra13671.bthack.render.drawer.StaticDrawer;
import com.ferra13671.bthack.render.drawer.impl.*;
import com.ferra13671.bthack.screen.api.ScreenObject;
import com.ferra13671.bthack.screen.impl.ui.sections.CategoryBarSection;
import com.ferra13671.bthack.screen.impl.ui.sections.WatermarkSection;
import com.ferra13671.bthack.utils.Mc;
import com.ferra13671.bthack.utils.StyleConstants;
import lombok.Getter;

public class ClickUI extends ScreenObject implements Mc {
    @Getter
    private final int width = StyleConstants.UI_WIDTH;
    @Getter
    private final int height = StyleConstants.UI_HEIGHT;
    @Getter
    private int x;
    @Getter
    private int y;
    @Getter
    private WatermarkSection watermarkSection;
    @Getter
    private CategoryBarSection categoryBarSection;
    private final DrawerPool platePool = new DrawerPool(
            new StaticDrawer<>(() ->
                    new RoundedBlurDrawer()
                            .rectSized(
                                    this.x,
                                    this.y,
                                    this.width,
                                    this.height,
                                    StyleConstants.UI_PLATE_ROUND_RADIUS
                            )
            ),
            new StaticDrawer<>(() ->
                    new RoundedRectDrawer()
                            .rectSized(
                                    this.x,
                                    this.y,
                                    this.width,
                                    this.height,
                                    StyleConstants.UI_PLATE_ROUND_RADIUS,
                                    RectColors.oneColor(StyleConstants.UI_PLATE_BACKGROUND_COLOR)
                            )
            ),
            new StaticDrawer<>(() ->
                    new RoundedOutlinedRectDrawer()
                            .rectSized(
                                    this.x,
                                    this.y,
                                    this.width,
                                    this.height,
                                    StyleConstants.UI_PLATE_ROUND_RADIUS,
                                    StyleConstants.UI_PLATE_OUTLINE_SIZE,
                                    RectColors.oneColor(RenderColor.TRANSLUCENT),
                                    RectColors.oneColor(StyleConstants.UI_PLATE_OUTLINE_COLOR)
                            )
            ),
            new StaticDrawer<>(() ->
                    new ColoredRectDrawer()
                            .rectSized(
                                    this.x + 1 + 208,
                                    this.y + 1,
                                    2,
                                    607,
                                    RectColors.oneColor(StyleConstants.UI_PLATE_OUTLINE_COLOR)
                            )
                            .rectSized(
                                    this.x + 1,
                                    this.y + 65,
                                    208,
                                    2,
                                    RectColors.oneColor(StyleConstants.UI_PLATE_OUTLINE_COLOR)
                            )
                            .rectSized(
                                    this.x + 1 + 210,
                                    this.y + 65,
                                    640,
                                    2,
                                    RectColors.oneColor(StyleConstants.UI_PLATE_OUTLINE_COLOR)
                            )
                            .rectSized(
                                    this.x + 1,
                                    this.y + this.height - 1 - 82,
                                    208,
                                    2,
                                    RectColors.oneColor(StyleConstants.UI_PLATE_OUTLINE_COLOR)
                            )
            )
    );

    public ClickUI(IEventBus instanceEventBus) {
        super(instanceEventBus);

        this.watermarkSection = new WatermarkSection(this, instanceEventBus);
        this.categoryBarSection = new CategoryBarSection(this, instanceEventBus);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.platePool.draw();
        this.watermarkSection.render(mouseX, mouseY);
        this.categoryBarSection.render(mouseX, mouseY);
    }

    @EventSubscriber(event = RepositionElementsEvent.class, priority = 1000)
    public void onRepositionElements() {
        initPos();
        this.platePool.rebuild();
    }

    private void initPos() {
        this.x = (mc.getWindow().getWidth() / 2) - (this.width / 2);
        this.y = (mc.getWindow().getHeight() / 2) - (this.height / 2);
    }
}
