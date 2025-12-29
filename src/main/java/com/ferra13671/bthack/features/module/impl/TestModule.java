package com.ferra13671.bthack.features.module.impl;

import com.ferra13671.MegaEvents.eventbus.EventSubscriber;
import com.ferra13671.bthack.events.Render2DEvent;
import com.ferra13671.bthack.features.module.BThackModule;
import com.ferra13671.bthack.features.module.ModuleInfo;
import com.ferra13671.bthack.render.Fonts;
import com.ferra13671.bthack.render.drawer.impl.text.RenderText;
import com.ferra13671.bthack.render.drawer.impl.text.TextDrawer;
import net.minecraft.ChatFormatting;

import java.awt.*;

@ModuleInfo(name = "Test Module", category = "misc")
public class TestModule extends BThackModule {


    @EventSubscriber(event = Render2DEvent.class)
    public void onRender2D() {
        TextDrawer drawer = new TextDrawer(Fonts.SEMIBOLD.getOrLoad(30));

        drawer.text(
                new RenderText(
                        "Hello " + ChatFormatting.RED + "w" + ChatFormatting.YELLOW + "o" + ChatFormatting.GREEN + "r" + ChatFormatting.AQUA + "l" + ChatFormatting.BLUE + "d" + ChatFormatting.DARK_PURPLE + "!",
                        300,
                        300
                )
        );
        drawer.end().tryDraw().close();
    }
}
