package com.ferra13671.bthack.screen.impl.ui.sections;

import com.ferra13671.MegaEvents.eventbus.IEventBus;
import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.Fonts;
import com.ferra13671.bthack.render.drawer.DrawerPool;
import com.ferra13671.bthack.render.drawer.StaticDrawer;
import com.ferra13671.bthack.render.drawer.impl.BasicTextureDrawer;
import com.ferra13671.bthack.render.drawer.impl.text.RenderText;
import com.ferra13671.bthack.render.drawer.impl.text.TextDrawer;
import com.ferra13671.bthack.screen.impl.ui.ClickUI;
import com.ferra13671.bthack.utils.StyleConstants;
import com.ferra13671.gltextureutils.atlas.TextureBorder;

public class WatermarkSection extends ClickUISection {
    private final DrawerPool drawerPool = new DrawerPool(
            new StaticDrawer<>(() ->
                    new BasicTextureDrawer()
                            .setTexture(BThackRenderSystem.TEXTURES.CLIENT_ICON)
                            .rectSized(
                                    this.x + 1 + 24,
                                    this.y + 1 + 14,
                                    35,
                                    35,
                                    new TextureBorder(0, 0, 1, 1)
                            )
            ),
            new StaticDrawer<>(() ->
                    new TextDrawer(Fonts.SEMIBOLD.getOrLoad(34))
                            .text(new RenderText(
                                    "BThack",
                                    this.x + 1 + 70,
                                    this.y + 1 + 14 + (35 / 2f) - (Fonts.SEMIBOLD.getOrLoad(34).getTextHeight() / 2f)
                            ))
            )
    );

    public WatermarkSection(ClickUI clickUI, IEventBus instanceEventBus) {
        super(clickUI, instanceEventBus, StyleConstants.UI_SECTION_WATERMARK_WIDTH, StyleConstants.UI_SECTION_WATERMARK_HEIGHT);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.drawerPool.draw();
        super.render(mouseX, mouseY);
    }

    @Override
    public void rebuild() {
        this.x = this.clickUI.getX() + 1;
        this.y = this.clickUI.getY() + 1;

        this.drawerPool.rebuild();
    }
}
