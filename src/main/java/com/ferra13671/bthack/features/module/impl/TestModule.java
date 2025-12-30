package com.ferra13671.bthack.features.module.impl;

import com.ferra13671.MegaEvents.eventbus.EventSubscriber;
import com.ferra13671.bthack.events.Render2DEvent;
import com.ferra13671.bthack.features.module.BThackModule;
import com.ferra13671.bthack.features.module.ModuleInfo;
import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.Fonts;
import com.ferra13671.bthack.render.RectColors;
import com.ferra13671.bthack.render.RenderColor;
import com.ferra13671.bthack.render.drawer.impl.BlitDrawer;
import com.ferra13671.bthack.render.drawer.impl.RoundedOutlinedRectDrawer;
import com.ferra13671.bthack.render.drawer.impl.RoundedRectDrawer;
import com.ferra13671.bthack.render.drawer.impl.text.RenderText;
import com.ferra13671.bthack.render.drawer.impl.text.TextDrawer;
import com.ferra13671.bthack.utils.Mc;
import net.minecraft.ChatFormatting;

@ModuleInfo(name = "Test Module", category = "misc")
public class TestModule extends BThackModule implements Mc {


    @EventSubscriber(event = Render2DEvent.class)
    public void onRender2D() {
        BThackRenderSystem.BLUR_PROVIDER.drawBlur();
        new BlitDrawer(
                mc.getWindow().getWidth(),
                mc.getWindow().getHeight(),
                BThackRenderSystem.BLUR_PROVIDER.getBlurFrameBuffer()
        ).tryDraw().close();

        new TextDrawer(Fonts.SEMIBOLD.getOrLoad(30))
                .text(
                        new RenderText(
                                "Hello " + ChatFormatting.RED + "w" + ChatFormatting.YELLOW + "o" + ChatFormatting.GREEN + "r" + ChatFormatting.AQUA + "l" + ChatFormatting.BLUE + "d" + ChatFormatting.DARK_PURPLE + "!",
                                300,
                                300
                        )
                )
                .end()
                .tryDraw()
                .close();

        new RoundedRectDrawer()
                .rectSized(600, 600, 200, 200, 50 * (float) Math.sin(Math.toRadians((System.currentTimeMillis() / 10) % 180)), RectColors.oneColor(RenderColor.WHITE))
                .end()
                .tryDraw()
                .close();

        new RoundedOutlinedRectDrawer()
                .rectSized(900, 600, 200, 200, 50 * (float) Math.sin(Math.toRadians((System.currentTimeMillis() / 10) % 180)), 20, RectColors.oneColor(RenderColor.WHITE), RectColors.oneColor(RenderColor.BLACK))
                .end()
                .tryDraw()
                .close();
    }
}
