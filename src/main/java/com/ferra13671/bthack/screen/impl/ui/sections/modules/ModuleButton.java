package com.ferra13671.bthack.screen.impl.ui.sections.modules;

import com.ferra13671.MegaEvents.eventbus.IEventBus;
import com.ferra13671.bthack.events.screen.RebuildModulesSectionEvent;
import com.ferra13671.bthack.features.module.IModule;
import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.RectColors;
import com.ferra13671.bthack.render.RenderColor;
import com.ferra13671.bthack.render.drawer.impl.ColoredTextureDrawer;
import com.ferra13671.bthack.render.drawer.impl.RoundedOutlinedRectDrawer;
import com.ferra13671.bthack.render.drawer.impl.RoundedRectDrawer;
import com.ferra13671.bthack.render.drawer.impl.text.FormattedText;
import com.ferra13671.bthack.render.drawer.impl.text.RenderText;
import com.ferra13671.bthack.render.drawer.impl.text.TextDrawer;
import com.ferra13671.bthack.screen.api.MouseClick;
import com.ferra13671.bthack.screen.api.ScreenObjectImpl;
import com.ferra13671.bthack.screen.impl.ui.Rebuildable;
import com.ferra13671.bthack.utils.StyleConstants;

public class ModuleButton extends ScreenObjectImpl implements Rebuildable {
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
    protected void mouseEnter() {
        this.instanceEventBus.activate(new RebuildModulesSectionEvent());
    }

    @Override
    protected void mouseLeave() {
        this.instanceEventBus.activate(new RebuildModulesSectionEvent());
    }

    @Override
    public void mouseClicked(MouseClick click) {
        if (click.button() == 0) {
            this.module.setEnabled(!this.module.isEnabled());
            this.instanceEventBus.activate(new RebuildModulesSectionEvent());
        }
    }

    public void drawDownPlate(RoundedRectDrawer drawer) {
        StyleConstants.ModuleColorPack colorPack = getColorPack();

        drawer
                .rectSized(
                        getX(),
                        getY(),
                        this.width,
                        this.height,
                        StyleConstants.UI_PLATE_ROUND_RADIUS,
                        RectColors.oneColor(colorPack.background())
                );
    }

    public void drawOutlinePlate(RoundedOutlinedRectDrawer drawer) {
        StyleConstants.ModuleColorPack colorPack = getColorPack();

        drawer
                .rectSized(
                        getX(),
                        getY(),
                        this.width,
                        this.height,
                        StyleConstants.UI_PLATE_ROUND_RADIUS,
                        StyleConstants.UI_PLATE_OUTLINE_SIZE,
                        RectColors.oneColor(RenderColor.TRANSLUCENT),
                        RectColors.oneColor(colorPack.outline())
                );
    }

    public void drawModuleName(TextDrawer textDrawer) {
        StyleConstants.ModuleColorPack colorPack = getColorPack();

        textDrawer
                .text(
                        new RenderText(
                                this.formattedName,
                                getX() + StyleConstants.UI_MODULE_NAME_INTERNAL_X_STEP,
                                getY() + StyleConstants.UI_MODULE_NAME_INTERNAL_Y_STEP
                        )
                        .withColor(colorPack.text())
                );
    }

    public void drawMenuIcon(ColoredTextureDrawer textureDrawer) {
        StyleConstants.ModuleColorPack colorPack = getColorPack();

        textureDrawer
                .rectSized(
                        getX() + this.width - StyleConstants.UI_MODULE_MENU_ICON_INTERNAL_X_STEP - StyleConstants.UI_DEFAULT_ICON_SIZE,
                        getY() + StyleConstants.UI_MODULE_MENU_ICON_INTERNAL_Y_STEP,
                        StyleConstants.UI_DEFAULT_ICON_SIZE,
                        StyleConstants.UI_DEFAULT_ICON_SIZE,
                        RectColors.oneColor(colorPack.text()),
                        BThackRenderSystem.TEXTURE_ATLASES.ICONS.getBorder(BThackRenderSystem.TEXTURES.MENU_ICON)
                );
    }

    private StyleConstants.ModuleColorPack getColorPack() {
        boolean enabled = this.module.isEnabled();
        boolean hovered = this.hoveredState.getValue();

        return
                enabled ?
                        hovered ?
                                StyleConstants.UI_MODULE_ENABLED_HOVERED_PACK
                                :
                                StyleConstants.UI_MODULE_ENABLED_PACK
                        :
                        hovered ?
                                StyleConstants.UI_MODULE_DISABLED_HOVERED_PACK
                                :
                                StyleConstants.UI_MODULE_DISABLED_PACK;
    }

    @Override
    public void rebuild() {
        this.x = this.section.getX() + StyleConstants.UI_MODULE_STEP + ((StyleConstants.UI_MODULE_WIDTH + StyleConstants.UI_MODULE_STEP) * xOffset);
        this.y = this.section.getY() + StyleConstants.UI_MODULE_STEP + ((StyleConstants.UI_MODULE_HEIGHT + StyleConstants.UI_MODULE_STEP) * yOffset);
    }
}
