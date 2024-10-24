package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Events.Render.RenderHudPostEvent;
import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;

public class Caipirinha extends Module {

    public static NumberSetting size;
    public static NumberSetting speed;

    public Caipirinha() {
        super("Caipirinha",
                "lang.module.Caipirinha",
                KeyboardUtils.RELEASE,
                Category.RENDER,
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
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        currentCaipirinha += 0.15 * speed.getValue();

        if (currentCaipirinha > 6)
            currentCaipirinha = 0;
    }

    ArrayList<Identifier> caipirinhi = new ArrayList<>(Arrays.asList(
            new Identifier("bthack", "caipirinha/caipirinha1.png"),
            new Identifier("bthack", "caipirinha/caipirinha2.png"),
            new Identifier("bthack", "caipirinha/caipirinha3.png"),
            new Identifier("bthack", "caipirinha/caipirinha4.png"),
            new Identifier("bthack", "caipirinha/caipirinha5.png"),
            new Identifier("bthack", "caipirinha/caipirinha6.png")
    ));

    @EventSubscriber
    public void onGuiRender(RenderHudPostEvent e) {
        if (nullCheck()) return;

        int _size = mc.getWindow().getScaledWidth() / (int) size.getValue();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        float x = (mc.getWindow().getScaledWidth() / 2f) + ((mc.getWindow().getScaledWidth() / 4f) - (_size / 2f));
        float y = (float) ((mc.getWindow().getScaledHeight() - _size) + (_size * 0.065));
        BThackRender.drawTextureRect(caipirinhi.get((int) currentCaipirinha), x, y, x + _size, y + _size);
        glDisable(GL_BLEND);
    }
}
