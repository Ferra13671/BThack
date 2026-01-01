package com.ferra13671.bthack.screen.impl.ui.sections.modules;

import com.ferra13671.MegaEvents.eventbus.IEventBus;
import com.ferra13671.bthack.features.module.IModule;
import com.ferra13671.bthack.render.RectColors;
import com.ferra13671.bthack.render.RenderColor;
import com.ferra13671.bthack.render.drawer.impl.RoundedOutlinedRectDrawer;
import com.ferra13671.bthack.render.drawer.impl.RoundedRectDrawer;
import com.ferra13671.bthack.render.drawer.impl.text.FormattedText;
import com.ferra13671.bthack.render.drawer.impl.text.RenderText;
import com.ferra13671.bthack.render.drawer.impl.text.TextDrawer;
import com.ferra13671.bthack.screen.api.ScreenObjectImpl;
import com.ferra13671.bthack.screen.impl.ui.Repositionable;
import com.ferra13671.bthack.utils.StyleConstants;

public class ModuleButton extends ScreenObjectImpl implements Repositionable {
    private final ModulesSection section;
    private final IModule module;
    private final FormattedText formattedName;
    private final int xOffset;
    private final int yOffset;

    public ModuleButton(IEventBus instanceEventBus, ModulesSection section, IModule module, int xOffset, int yOffset) {
        super(instanceEventBus, 0, 0, StyleConstants.UI_MODULE_WIDTH, StyleConstants.UI_MODULE_HEIGHT);

        this.section = section;
        this.module = module;
        this.formattedName = new FormattedText(module.getName());
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public void render(int mouseX, int mouseY) {}

    public void drawDownPlate(RoundedRectDrawer drawer) {
        drawer
                .rectSized(
                        getX(),
                        getY(),
                        this.width,
                        this.height,
                        StyleConstants.UI_PLATE_ROUND_RADIUS,
                        RectColors.oneColor(StyleConstants.UI_MODULE_PLATE_BACKGROUND_DISABLE_COLOR)
                );
    }

    public void drawOutlinePlate(RoundedOutlinedRectDrawer drawer) {
        drawer
                .rectSized(
                        getX(),
                        getY(),
                        this.width,
                        this.height,
                        StyleConstants.UI_PLATE_ROUND_RADIUS,
                        StyleConstants.UI_PLATE_OUTLINE_SIZE,
                        RectColors.oneColor(RenderColor.TRANSLUCENT),
                        RectColors.oneColor(StyleConstants.UI_PLATE_OUTLINE_COLOR)
                );
    }

    public void drawModuleName(TextDrawer textDrawer) {
        textDrawer
                .text(
                        new RenderText(
                                this.formattedName,
                                getX() + StyleConstants.UI_MODULE_NAME_INTERNAL_X_STEP,
                                getY() + StyleConstants.UI_MODULE_NAME_INTERNAL_Y_STEP
                        )
                );
    }

    @Override
    public void reposition() {
        this.x = this.section.getX() + StyleConstants.UI_MODULE_STEP + ((StyleConstants.UI_MODULE_WIDTH + StyleConstants.UI_MODULE_STEP) * xOffset);
        this.y = this.section.getY() + StyleConstants.UI_MODULE_STEP + ((StyleConstants.UI_MODULE_HEIGHT + StyleConstants.UI_MODULE_STEP) * yOffset);
    }
}
