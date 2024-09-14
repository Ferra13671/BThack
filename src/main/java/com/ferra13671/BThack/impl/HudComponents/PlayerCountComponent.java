package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.HudComponent.HudComponent;
import net.minecraft.util.Formatting;

public class PlayerCountComponent extends HudComponent {

    public PlayerCountComponent() {
        super("PlayerCount",
                5,
                135,
                true
        );
    }

    @Override
    public void render(RenderType type) {
        if (nullCheck()) return;

        String renderString = "Players " + Formatting.WHITE + mc.player.networkHandler.getPlayerList().size();
        drawText(renderString, (int) getX(), (int) getY());
        this.width = mc.textRenderer.getWidth(renderString);
        this.height = mc.textRenderer.fontHeight;
    }
}
