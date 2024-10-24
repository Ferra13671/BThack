package com.ferra13671.BThack.impl.Modules.RENDER;


import com.ferra13671.BTbot.api.Utils.Generate.GenerateNumber;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Events.Render.RenderHudPostEvent;
import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import java.awt.*;

public class Africa extends Module {
    private int africaColor = new Color(255, 101, 0, 76).hashCode();

    public Africa() {
        super("Africa",
                "lang.module.Africa",
                KeyboardUtils.RELEASE,
                Category.RENDER,
                false
        );
    }

    @EventSubscriber
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        mc.player.setFireTicks(1);
    }

    private short tickTimer = 0;
    private int colorAlpha = 76;

    @EventSubscriber(priority = Integer.MIN_VALUE)
    public void onHudRender(RenderHudPostEvent e) {
        BThackRender.drawRect(0,0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), africaColor);
        tickTimer++;
        if (tickTimer > 15) {
            int alpha = (int) (colorAlpha * GenerateNumber.generateFloat(0.94f, 1.05f));
            if (alpha > 110) {
                alpha = 110;
            } else if (alpha < 50) {
                alpha = 50;
            }
            colorAlpha = alpha;

            africaColor = new Color(255, 101, 0, alpha).hashCode();
            tickTimer = 0;
        }
    }
}
