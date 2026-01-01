package com.ferra13671.bthack.screen.impl.ui.sections.categories;

import com.ferra13671.MegaEvents.eventbus.IEventBus;
import com.ferra13671.bthack.events.screen.ChangeCategoryEvent;
import com.ferra13671.bthack.features.category.ICategory;
import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.RectColors;
import com.ferra13671.bthack.render.RenderColor;
import com.ferra13671.bthack.render.drawer.StaticDrawer;
import com.ferra13671.bthack.render.drawer.impl.BasicTextureDrawer;
import com.ferra13671.bthack.render.drawer.impl.ColoredRectDrawer;
import com.ferra13671.bthack.render.drawer.impl.text.FormattedText;
import com.ferra13671.bthack.render.drawer.impl.text.RenderText;
import com.ferra13671.bthack.render.drawer.impl.text.TextDrawer;
import com.ferra13671.bthack.screen.api.MouseClick;
import com.ferra13671.bthack.screen.api.ScreenObjectImpl;
import com.ferra13671.bthack.screen.impl.ui.Repositionable;
import com.ferra13671.bthack.utils.StyleConstants;
import com.ferra13671.cometrenderer.CometRenderer;

public class CategoryButton extends ScreenObjectImpl implements Repositionable {
    private final CategoriesSection categoriesSection;
    private final ICategory category;
    private final FormattedText formattedName;
    private final int offset;
    private final StaticDrawer<ColoredRectDrawer> selectedLineDrawer = new StaticDrawer<>(() ->
            new ColoredRectDrawer(CometRenderer::resetShaderColor)
                    .rectSized(
                            getX(),
                            getY() + this.height,
                            50,
                            2,
                            RectColors.horizontalGradient(RenderColor.TRANSLUCENT, RenderColor.WHITE)
                    )
                    .rectSized(
                            getX() + 50,
                            getY() + this.height,
                            70,
                            2,
                            RectColors.oneColor(RenderColor.WHITE)
                    )
                    .rectSized(
                            getX() + 50 + 70,
                            getY() + this.height,
                            50,
                            2,
                            RectColors.horizontalGradient(RenderColor.WHITE, RenderColor.TRANSLUCENT)
                    )
    );

    public CategoryButton(IEventBus instanceEventBus, CategoriesSection categoriesSection, ICategory category, int offset) {
        super(instanceEventBus, 0, 0, StyleConstants.UI_CATEGORY_WIDTH, StyleConstants.UI_DEFAULT_ICON_SIZE);
        this.categoriesSection = categoriesSection;
        this.category = category;
        this.formattedName = new FormattedText(category.getName());
        this.offset = offset;
    }

    public void drawIcon(BasicTextureDrawer textureDrawer) {
        textureDrawer.rectSized(
                this.getX(),
                this.getY(),
                StyleConstants.UI_DEFAULT_ICON_SIZE,
                StyleConstants.UI_DEFAULT_ICON_SIZE,
                BThackRenderSystem.TEXTURE_ATLASES.CATEGORIES.getBorder(this.category.getIcon())
        );
    }

    public void drawName(TextDrawer textDrawer) {
        textDrawer.text(
                new RenderText(
                        this.formattedName,
                        getX() + StyleConstants.UI_DEFAULT_ICON_SIZE + StyleConstants.UI_CATEGORY_INTERNAL_STEP,
                        getY()
                )
        );
    }

    @Override
    public void mouseClicked(MouseClick click) {
        this.instanceEventBus.activate(new ChangeCategoryEvent(this.category));
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (this.hoveredState.getValue())
            this.selectedLineDrawer.draw();
    }

    @Override
    public void reposition() {
        this.x = this.categoriesSection.getX() + 25;
        this.y = this.categoriesSection.getY() + StyleConstants.UI_CATEGORY_STEP + ((StyleConstants.UI_CATEGORY_STEP + this.height) * offset);

        this.selectedLineDrawer.rebuild();
    }
}
