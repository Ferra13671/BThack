package com.ferra13671.bthack.screen.impl.ui.sections.categories;

import com.ferra13671.MegaEvents.eventbus.IEventBus;
import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.features.category.ICategory;
import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.Fonts;
import com.ferra13671.bthack.render.drawer.DrawerPool;
import com.ferra13671.bthack.render.drawer.StaticDrawer;
import com.ferra13671.bthack.render.drawer.impl.BasicTextureDrawer;
import com.ferra13671.bthack.render.drawer.impl.text.TextDrawer;
import com.ferra13671.bthack.screen.api.MouseClick;
import com.ferra13671.bthack.screen.api.ScreenObjectImpl;
import com.ferra13671.bthack.screen.impl.ui.ClickUI;
import com.ferra13671.bthack.screen.impl.ui.sections.ClickUISection;
import com.ferra13671.bthack.utils.StyleConstants;

import java.util.ArrayList;
import java.util.List;

public class CategoriesSection extends ClickUISection {
    private final List<CategoryButton> categoryButtons = new ArrayList<>();
    private final DrawerPool categoriesPool = new DrawerPool(
            new StaticDrawer<>(() -> {
                BasicTextureDrawer textureDrawer = new BasicTextureDrawer()
                        .setTexture(BThackRenderSystem.TEXTURE_ATLASES.CATEGORIES);

                this.categoryButtons.forEach(button -> button.drawIcon(textureDrawer));

                return textureDrawer;
            }),
            new StaticDrawer<>(() -> {
                TextDrawer textDrawer = new TextDrawer(Fonts.REGULAR.getOrLoad(29));

                this.categoryButtons.forEach(button -> button.drawName(textDrawer));

                return textDrawer;
            })
    );

    public CategoriesSection(ClickUI clickUI, IEventBus instanceEventBus) {
        super(clickUI, instanceEventBus, StyleConstants.UI_SECTION_CATEGORIES_WIDTH, StyleConstants.UI_SECTION_CATEGORIES_HEIGHT);

        int offset = 0;
        for (ICategory category : BThackClient.getInstance().getCategoryManager().getCategories()) {
            this.categoryButtons.add(new CategoryButton(instanceEventBus, this, category, offset));

            offset++;
        }
    }

    @Override
    public void update() {
        this.categoryButtons.forEach(ScreenObjectImpl::update);
        super.update();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.categoriesPool.draw();
        for (CategoryButton button : this.categoryButtons)
            button.render(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(MouseClick click) {
        for (CategoryButton button : this.categoryButtons)
            if (button.isMouseOnObject(click.x(), click.y()))
                button.mouseClicked(click);
    }

    @Override
    public void reposition() {
        this.x = this.clickUI.getX() + 1;
        this.y = this.clickUI.getWatermarkSection().getY() + this.clickUI.getWatermarkSection().getHeight();

        this.categoryButtons.forEach(CategoryButton::reposition);
        this.categoriesPool.rebuild();
    }
}
