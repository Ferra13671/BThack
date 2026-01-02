package com.ferra13671.bthack.screen.impl.ui;

import com.ferra13671.MegaEvents.eventbus.EventSubscriber;
import com.ferra13671.MegaEvents.eventbus.IEventBus;
import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.events.screen.ChangeCategoryEvent;
import com.ferra13671.bthack.events.screen.RebuildModulesSectionEvent;
import com.ferra13671.bthack.events.screen.RebuildElementsEvent;
import com.ferra13671.bthack.features.category.ICategory;
import com.ferra13671.bthack.render.*;
import com.ferra13671.bthack.render.drawer.DrawerPool;
import com.ferra13671.bthack.render.drawer.StaticDrawer;
import com.ferra13671.bthack.render.drawer.impl.*;
import com.ferra13671.bthack.screen.api.AbstractScreenObject;
import com.ferra13671.bthack.screen.api.MouseClick;
import com.ferra13671.bthack.screen.api.MouseScroll;
import com.ferra13671.bthack.screen.impl.ui.sections.ClickUISection;
import com.ferra13671.bthack.screen.impl.ui.sections.categories.CategoriesSection;
import com.ferra13671.bthack.screen.impl.ui.sections.WatermarkSection;
import com.ferra13671.bthack.screen.impl.ui.sections.modules.ModulesSection;
import com.ferra13671.bthack.utils.Mc;
import com.ferra13671.bthack.utils.StyleConstants;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ClickUI extends AbstractScreenObject implements Mc, Rebuildable {
    @Getter
    private final int width = StyleConstants.UI_WIDTH;
    @Getter
    private final int height = StyleConstants.UI_HEIGHT;
    @Getter
    private int x;
    @Getter
    private int y;
    @Getter
    private final WatermarkSection watermarkSection;
    @Getter
    private final CategoriesSection categoriesSection;
    private final List<ModulesSection> modulesSections = new ArrayList<>();
    @Getter
    private ModulesSection currentModules;
    private final DrawerPool platePool = new DrawerPool(
            new StaticDrawer<>(() ->
                    new RoundedShadowDrawer()
                            .rectSized(
                                    this.x + 9,
                                    this.y + 9,
                                    this.width - 11,
                                    this.height - 11,
                                    StyleConstants.UI_PLATE_ROUND_RADIUS,
                                    RectColors.oneColor(RenderColor.of(0f, 0f, 0f, 0.5f))
                            )
            ),
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
        this.categoriesSection = new CategoriesSection(this, instanceEventBus);

        for (ICategory category : BThackClient.getInstance().getCategoryManager().getCategories())
            this.modulesSections.add(new ModulesSection(this, instanceEventBus, category));
    }

    @Override
    public void update() {
        this.watermarkSection.update();
        this.categoriesSection.update();
        if (this.currentModules != null)
            this.currentModules.update();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.platePool.draw();
        this.watermarkSection.render(mouseX, mouseY);
        this.categoriesSection.render(mouseX, mouseY);
        if (this.currentModules != null)
            this.currentModules.render(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(MouseClick click) {
        clickIfHovered(this.watermarkSection, click);
        clickIfHovered(this.categoriesSection, click);
        if (this.currentModules != null)
            clickIfHovered(this.currentModules, click);
    }

    @Override
    public void mouseReleased(MouseClick click) {
        this.watermarkSection.mouseReleased(click);
        this.categoriesSection.mouseReleased(click);
        if (this.currentModules != null)
            this.currentModules.mouseReleased(click);
    }

    @Override
    public void mouseScrolled(MouseScroll scroll) {
        scrollIfHovered(this.watermarkSection, scroll);
        scrollIfHovered(this.categoriesSection, scroll);
        if (this.currentModules != null)
            scrollIfHovered(this.currentModules, scroll);
    }

    @Override
    public void mouseMoved(int mouseX, int mouseY) {
        this.watermarkSection.mouseMoved(mouseX, mouseY);
        this.categoriesSection.mouseMoved(mouseX, mouseY);
        if (this.currentModules != null)
            this.currentModules.mouseMoved(mouseX, mouseY);
    }

    @Override
    public void keyTyped(int keyCode) {
        this.watermarkSection.keyTyped(keyCode);
        this.categoriesSection.keyTyped(keyCode);
        if (this.currentModules != null)
            this.currentModules.keyTyped(keyCode);
    }

    @Override
    public void charTyped(int _char) {
        this.watermarkSection.charTyped(_char);
        this.categoriesSection.charTyped(_char);
        if (this.currentModules != null)
            this.currentModules.charTyped(_char);
    }

    private void clickIfHovered(ClickUISection section, MouseClick click) {
        if (section.isMouseOnObject(click.x(), click.y()))
            section.mouseClicked(click);
    }

    private void scrollIfHovered(ClickUISection section, MouseScroll scroll) {
        if (section.isMouseOnObject(scroll.x(), scroll.y()))
            section.mouseScrolled(scroll);
    }

    @EventSubscriber(event = ChangeCategoryEvent.class)
    public void onChangeCategory(ChangeCategoryEvent e) {
        this.categoriesSection.rebuild();

        for (ModulesSection section : this.modulesSections) {
            if (section.getCategory() == e.category) {
                this.currentModules = section;
                return;
            }
        }

        this.currentModules = null;
    }

    @EventSubscriber(event = RebuildModulesSectionEvent.class)
    public void onRebuildModulesSection() {
        if (this.currentModules != null)
            this.currentModules.rebuild();
    }

    @EventSubscriber(event = RebuildElementsEvent.class, priority = 1000)
    public void onRebuildElements() {
        rebuild();
        this.platePool.rebuild();
    }

    @Override
    public void rebuild() {
        this.x = (mc.getWindow().getWidth() / 2) - (this.width / 2);
        this.y = (mc.getWindow().getHeight() / 2) - (this.height / 2);

        this.watermarkSection.rebuild();
        this.categoriesSection.rebuild();
        this.modulesSections.forEach(ModulesSection::rebuild);
    }
}
