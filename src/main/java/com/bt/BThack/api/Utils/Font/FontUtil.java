package com.bt.BThack.api.Utils.Font;

import com.bt.BThack.System.Client;
import com.bt.BThack.api.Module.Module;
import com.bt.BThack.api.Utils.Interfaces.Mc;

public final class FontUtil implements Mc {
    public static final BThackFontRenderer ubuntuFont = new BThackFontRenderer("Ubuntu", 16.0f);
    public static final BThackFontRenderer latoFont = new BThackFontRenderer("Lato", 16.0f);
    public static final BThackFontRenderer verdanaFont = new BThackFontRenderer("Ubuntu", 16.0f);
    public static final BThackFontRenderer comfortaaFont = new BThackFontRenderer("Comfortaa", 16.0f);
    public static final BThackFontRenderer subtitleFont = new BThackFontRenderer("Subtitle", 16.0f);
    public static final BThackFontRenderer comicSansFont = new BThackFontRenderer("ComicSans", 16.0f);

    public static BThackFontRenderer getCurrentCustomFont() {
        switch (Module.getMode("Font", "Font")) {
            case "Ubuntu":
                return ubuntuFont;
            case "Lato":
                return latoFont;
            case "Verdana":
                return verdanaFont;
            case "Comfortaa":
                return comfortaaFont;
            case "Subtitle":
                return subtitleFont;
            case "ComicSans":
                return comicSansFont;
        }

        return ubuntuFont;
    }

    public static void drawText(String text, float x, float y, int colour) {

        if (Client.getModuleByName("Font").isEnabled()) {
            if (Module.getCheckbox("Font", "Shadow")) {
                getCurrentCustomFont().drawStringWithShadow(Module.getCheckbox("Font", "Lowercase") ? text.toLowerCase() : text, x, y, colour);
            } else {
                getCurrentCustomFont().drawString(Module.getCheckbox("Font", "Lowercase") ? text.toLowerCase() : text, x, y, colour);
            }
        } else {
            mc.fontRenderer.drawStringWithShadow(Module.getCheckbox("Font", "Lowercase") ? text.toLowerCase() : text, x, y, colour);
        }
    }

    public static void drawTextWithAlphaCheck(String text, float x, float y, int colour) {
        if ((colour >> 24 & 255) <= 0) return;

        drawText(text, x, y, colour);
    }

    public static void drawTextNoShadow(String text, float x, float y, int colour) {
        if (Client.getModuleByName("Font").isEnabled()) {
            getCurrentCustomFont().drawString(Module.getCheckbox("Font", "Lowercase") ? text.toLowerCase() : text, x, y, colour);
        } else {
            mc.fontRenderer.drawString(Module.getCheckbox("Font", "Lowercase") ? text.toLowerCase() : text, (int) x, (int) y, colour);
        }
    }

    public static float getStringWidth(String text) {
        if (Client.getModuleByName("Font").isEnabled()) {
            return getCurrentCustomFont().getStringWidth(text);
        } else {
            return mc.fontRenderer.getStringWidth(text);
        }
    }

    public static float getStringHeight(String text) {
        if (Client.getModuleByName("Font").isEnabled()) {
            return getCurrentCustomFont().getStringHeight(text);
        } else {
            return 7;
        }
    }
}