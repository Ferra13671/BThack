package com.ferra13671.bthack.utils;

import com.ferra13671.bthack.render.RenderColor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StyleConstants {
    public int UI_WIDTH = 852;
    public int UI_HEIGHT = 609;
    public int UI_PLATE_OUTLINE_SIZE = 2;
    public int UI_PLATE_ROUND_RADIUS = 20;
    public RenderColor UI_PLATE_BACKGROUND_COLOR = RenderColor.of(0.117f, 0.117f, 0.117f, 0.4f);
    public RenderColor UI_PLATE_OUTLINE_COLOR = RenderColor.of(1f, 1f, 1f, 0.3f);

    public int UI_DEFAULT_ICON_SIZE = 35;

    public int UI_CATEGORY_WIDTH = 165;
    public int UI_CATEGORY_STEP = 19;
    public int UI_CATEGORY_INTERNAL_STEP = 19;
    public RenderColor UI_CATEGORY_DEFAULT_COLOR = RenderColor.WHITE;
    public RenderColor UI_CATEGORY_SELECTED_COLOR = RenderColor.of(235, 196, 255, 255);

    public int UI_MODULE_WIDTH = 292;
    public int UI_MODULE_HEIGHT = 55;
    public int UI_MODULE_STEP = 18;
    public int UI_MODULE_NAME_INTERNAL_X_STEP = 20;
    public int UI_MODULE_NAME_INTERNAL_Y_STEP = 12;
    public int UI_MODULE_MENU_ICON_INTERNAL_X_STEP = 18;
    public int UI_MODULE_MENU_ICON_INTERNAL_Y_STEP = 8;
    public ModuleColorPack UI_MODULE_DISABLED_PACK = new ModuleColorPack(
            RenderColor.of(0.117f, 0.117f, 0.117f, 0.1f),
            UI_PLATE_OUTLINE_COLOR,
            RenderColor.of(1f, 1f, 1f, 0.8f)
    );
    public ModuleColorPack UI_MODULE_ENABLED_PACK = new ModuleColorPack(
            RenderColor.of(1f, 1f, 1f, 0.2f),
            RenderColor.of(1f, 1f, 1f, 0.7f),
            RenderColor.WHITE
    );
    public ModuleColorPack UI_MODULE_DISABLED_HOVERED_PACK = new ModuleColorPack(
            UI_MODULE_DISABLED_PACK.background(),
            UI_MODULE_ENABLED_PACK.outline(),
            UI_MODULE_ENABLED_PACK.text()
    );
    public ModuleColorPack UI_MODULE_ENABLED_HOVERED_PACK = new ModuleColorPack(
            UI_MODULE_ENABLED_PACK.background(),
            RenderColor.WHITE,
            RenderColor.WHITE
    );

    public int UI_SECTION_WATERMARK_WIDTH = 208;
    public int UI_SECTION_WATERMARK_HEIGHT = 64;
    public int UI_SECTION_CATEGORIES_WIDTH = 208;
    public int UI_SECTION_CATEGORIES_HEIGHT = 458;
    public int UI_SECTION_MODULES_WIDTH = 638;
    public int UI_SECTION_MODULES_HEIGHT = 540;


    public record ModuleColorPack(RenderColor background, RenderColor outline, RenderColor text) {
    }
}
