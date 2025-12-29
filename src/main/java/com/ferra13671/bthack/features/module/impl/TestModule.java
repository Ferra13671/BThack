package com.ferra13671.bthack.features.module.impl;

import com.ferra13671.MegaEvents.eventbus.EventSubscriber;
import com.ferra13671.bthack.events.Render2DEvent;
import com.ferra13671.bthack.features.module.BThackModule;
import com.ferra13671.bthack.features.module.ModuleInfo;
import com.ferra13671.bthack.render.RenderColor;
import com.ferra13671.bthack.render.drawer.impl.text.RenderText;
import com.ferra13671.bthack.render.drawer.impl.text.TextDrawer;
import com.ferra13671.bthack.render.font.TTFFont;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

@ModuleInfo(name = "Test Module", category = "misc")
public class TestModule extends BThackModule {

    private Font font;
    {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("assets/bthack-client/SF-Pro-Rounded-Semibold.otf")).deriveFont(12f);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private final TTFFont ttfFont = new TTFFont(font, 500);


    @EventSubscriber(event = Render2DEvent.class)
    public void onRender2D() {
        TextDrawer drawer = new TextDrawer(ttfFont);

        Random random = new Random();

        int a = 0;
        RenderText text = new RenderText("", 0, 0);
        for (int i = 0; i < 10; i++) {
            for (int i2 = 0; i2 < 10; i2++) {
                drawer.text(text.withText(a % 2 == 1 ? "Hello world!" : "Пошел нахуй!").withX(200 + (i * 100)).withY(200 + (i2 * 20)).withColor(RenderColor.of(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f)));

                a++;
            }
        }
        drawer.end().tryDraw().close();
    }
}
