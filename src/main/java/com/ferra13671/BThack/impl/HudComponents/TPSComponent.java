package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import net.minecraft.util.Formatting;

public class TPSComponent extends HudComponent {

    public TPSComponent() {
        super("TPS",
                5,
                125,
                true
        );
    }

    @Override
    public void render(RenderType type) {
        if (nullCheck()) return;

        String renderString = String.format("TPS%s %.2f", Formatting.WHITE, Client.tpsManager.getTickRate());
        drawText(renderString, (int) getX(), (int) getY());
        this.width = mc.textRenderer.getWidth(renderString);
        this.height = mc.textRenderer.fontHeight;
    }
}
