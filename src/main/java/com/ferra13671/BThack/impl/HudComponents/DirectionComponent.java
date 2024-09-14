package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import net.minecraft.util.Formatting;

public class DirectionComponent extends HudComponent {

    public DirectionComponent() {
        super("Direction",
                5,
                85,
                true
        );
    }

    @Override
    public void render(RenderType type) {
        if (nullCheck()) return;

        String direction = AimBotUtils.getDirection(mc.player);

        String text = "Direction: " + Formatting.WHITE + direction;

        drawText(text,
                (int)  this.getX(), (int) this.getY());

        this.width = mc.textRenderer.getWidth(text);
        this.height = mc.textRenderer.fontHeight;
    }
}
