package com.ferra13671.bthack.screen.impl.ui.sections.modules;

import com.ferra13671.MegaEvents.eventbus.IEventBus;
import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.features.category.ICategory;
import com.ferra13671.bthack.features.module.IModule;
import com.ferra13671.bthack.render.Fonts;
import com.ferra13671.bthack.render.drawer.DrawerPool;
import com.ferra13671.bthack.render.drawer.StaticDrawer;
import com.ferra13671.bthack.render.drawer.impl.RoundedOutlinedRectDrawer;
import com.ferra13671.bthack.render.drawer.impl.RoundedRectDrawer;
import com.ferra13671.bthack.render.drawer.impl.text.TextDrawer;
import com.ferra13671.bthack.screen.impl.ui.ClickUI;
import com.ferra13671.bthack.screen.impl.ui.sections.ClickUISection;
import com.ferra13671.bthack.utils.StyleConstants;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ModulesSection extends ClickUISection {
    @Getter
    private final ICategory category;
    private final List<ModuleButton> moduleButtons = new ArrayList<>();
    private final DrawerPool modulesPool = new DrawerPool(
            new StaticDrawer<>(() -> {
                RoundedRectDrawer drawer = new RoundedRectDrawer();

                this.moduleButtons.forEach(module -> module.drawDownPlate(drawer));

                return drawer;
            }),
            new StaticDrawer<>(() -> {
                RoundedOutlinedRectDrawer drawer = new RoundedOutlinedRectDrawer();

                this.moduleButtons.forEach(module -> module.drawOutlinePlate(drawer));

                return drawer;
            }),
            new StaticDrawer<>(() -> {
                TextDrawer drawer = new TextDrawer(Fonts.REGULAR.getOrLoad(26));

                this.moduleButtons.forEach(module -> module.drawModuleName(drawer));

                return drawer;
            })
    );

    public ModulesSection(ClickUI clickUI, IEventBus instanceEventBus, ICategory category) {
        super(clickUI, instanceEventBus, StyleConstants.UI_SECTION_MODULES_WIDTH, StyleConstants.UI_SECTION_MODULES_HEIGHT);

        this.category = category;

        int xOffset = 0;
        int yOffset = 0;
        for (IModule module : BThackClient.getInstance().getCategoryManager().getModules(category)) {
            this.moduleButtons.add(new ModuleButton(instanceEventBus, this, module, xOffset, yOffset));

            if (xOffset >= 1) {
                xOffset = 0;
                yOffset++;
            }

            xOffset++;
        }
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.modulesPool.draw();
    }

    @Override
    public void reposition() {
        this.x = this.clickUI.getCategoriesSection().getX() + this.clickUI.getCategoriesSection().getWidth() + StyleConstants.UI_PLATE_OUTLINE_SIZE;
        this.y = this.clickUI.getY() + this.clickUI.getWatermarkSection().getHeight() + StyleConstants.UI_PLATE_OUTLINE_SIZE;

        this.moduleButtons.forEach(ModuleButton::reposition);

        this.modulesPool.rebuild();
    }
}
