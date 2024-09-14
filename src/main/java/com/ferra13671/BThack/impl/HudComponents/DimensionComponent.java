package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.HudComponent.HudComponent;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class DimensionComponent extends HudComponent {

    public DimensionComponent() {
        super("Dimension",
                5,
                205,
                false
        );
    }

    @Override
    public void render(RenderType type) {
        String text = "";
        if (mc.world.getRegistryKey() == World.NETHER) {
            text = "Dimension: " + Formatting.WHITE + "Nether";
        } else if (mc.world.getRegistryKey() == World.OVERWORLD) {
            text = "Dimension: " + Formatting.WHITE + "Overworld";
        } else {
            text = "Dimension: " + Formatting.WHITE + "End";
        }

        drawText(text, (int) getX(), (int) getY());
        this.width = mc.textRenderer.getWidth(text);
        this.height = mc.textRenderer.fontHeight;
    }
}
