package com.ferra13671.BThack.impl.HudComponents;


import com.ferra13671.BThack.api.HudComponent.HudComponent;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.text.DecimalFormat;

public class CoordinatesComponent extends HudComponent {

    public CoordinatesComponent() {
        super("Coordinates",
                5,
                57,
                true
        );
    }

    @Override
    public void render(RenderType type) {
        if (nullCheck()) return;

        String overWorldX;
        String overWorldZ;
        String netherX;
        String netherZ;
        if (mc.world.getRegistryKey() == World.NETHER) {
            overWorldX = new DecimalFormat(pattern).format(mc.player.getX() * 8);
            overWorldZ = new DecimalFormat(pattern).format(mc.player.getZ() * 8.0D);
            netherX = new DecimalFormat(pattern).format(mc.player.getX());
            netherZ = new DecimalFormat(pattern).format(mc.player.getZ());
        } else {
            overWorldX = new DecimalFormat(pattern).format(mc.player.getX());
            overWorldZ = new DecimalFormat(pattern).format(mc.player.getZ());
            netherX = new DecimalFormat(pattern).format(mc.player.getX() / 8.0D);
            netherZ = new DecimalFormat(pattern).format(mc.player.getZ() / 8.0D);
        }

        String xyzText = "XYZ: " + Formatting.WHITE + overWorldX + " " + Math.round(mc.player.getY()) + " " + overWorldZ;
        String netherText = "Nether: " + Formatting.WHITE + netherX + " " + Math.round(mc.player.getY()) + " " + netherZ;

        drawText(xyzText, (int) this.getX(), (int) this.getY());
        drawText(netherText, (int) this.getX(), (int) (this.getY() + 9.3f));

        this.width = Math.max(mc.textRenderer.getWidth(xyzText), mc.textRenderer.getWidth(netherText));
        this.height = mc.textRenderer.fontHeight * 2.2f;
    }
}
