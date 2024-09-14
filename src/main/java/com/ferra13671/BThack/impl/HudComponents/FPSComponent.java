package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.HudComponent.HudComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

public class FPSComponent extends HudComponent {

    public FPSComponent() {
        super("FPS",
                5,
                47,
                true
        );
    }

    @Override
    public void render(RenderType type) {
        if (nullCheck()) return;

        String text = "FPS: " + Formatting.WHITE + MinecraftClient.getInstance().getCurrentFps();

        drawText(text, (int) this.getX(), (int) this.getY());
        this.width = mc.textRenderer.getWidth(text);
        this.height = mc.textRenderer.fontHeight;
    }
}
