package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Events.Render.RenderHudPostEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.PathMode;

import java.util.ArrayList;
import java.util.Arrays;

public class Caipirinha extends Module {

    public static NumberSetting size;
    public static NumberSetting speed;

    public Caipirinha() {
        super("Caipirinha",
                "lang.module.Caipirinha",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        size = new NumberSetting("Size", this, 7, 15, 5, true);
        speed = new NumberSetting("Speed", this, 2.2, 1.4, 2.70, false);

        initSettings(
                size,
                speed
        );
    }

    double currentCaipirinha = 0;
    @EventSubscriber
    public void onClientTick(ClientTickEvent e) {
        if (nullCheck()) return;

        currentCaipirinha += 0.15 * speed.getValue();

        if (currentCaipirinha > 6)
            currentCaipirinha = 0;
    }

    ArrayList<GLTexture> caipirinhi = new ArrayList<>(Arrays.asList(
            new GLTexture("assets/bthack/caipirinha/caipirinha1.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA),
            new GLTexture("assets/bthack/caipirinha/caipirinha2.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA),
            new GLTexture("assets/bthack/caipirinha/caipirinha3.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA),
            new GLTexture("assets/bthack/caipirinha/caipirinha4.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA),
            new GLTexture("assets/bthack/caipirinha/caipirinha5.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA),
            new GLTexture("assets/bthack/caipirinha/caipirinha6.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA)
    ));

    @EventSubscriber
    public void onGuiRender(RenderHudPostEvent e) {
        if (nullCheck()) return;

        int _size = mc.getWindow().getScaledWidth() / (int) size.getValue();

        float x = (mc.getWindow().getScaledWidth() / 2f) + ((mc.getWindow().getScaledWidth() / 4f) - (_size / 2f));
        float y = (float) ((mc.getWindow().getScaledHeight() - _size) + (_size * 0.065));
        BThackRender.drawTextureRect(caipirinhi.get((int) currentCaipirinha), x, y, x + _size, y + _size);
    }
}
