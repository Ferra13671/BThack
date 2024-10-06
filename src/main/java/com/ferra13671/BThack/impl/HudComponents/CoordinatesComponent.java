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

    String xyz1 = "";
    String xyz2 = "";

    @Override
    public void render() {
        if (nullCheck()) return;

        drawText(xyz1, (int) getX(), (int) getY());
        drawText(xyz2, (int) getX(), (int) (getY() + 9.3f));
    }

    @Override
    public void tick() {
        String overWorldX;
        String overWorldZ;
        String netherX;
        String netherZ;
        if (mc.world.getRegistryKey() == World.NETHER) {
            overWorldX = decimal.format(mc.player.getX() * 8);
            overWorldZ = decimal.format(mc.player.getZ() * 8.0D);
            netherX = decimal.format(mc.player.getX());
            netherZ = decimal.format(mc.player.getZ());
        } else {
            overWorldX = decimal.format(mc.player.getX());
            overWorldZ = decimal.format(mc.player.getZ());
            netherX = decimal.format(mc.player.getX() / 8.0D);
            netherZ = decimal.format(mc.player.getZ() / 8.0D);
        }

        xyz1 = "XYZ: " + Formatting.WHITE + overWorldX + " " + Math.round(mc.player.getY()) + " " + overWorldZ;
        xyz2 = "Nether: " + Formatting.WHITE + netherX + " " + Math.round(mc.player.getY()) + " " + netherZ;

        this.width = Math.max(mc.textRenderer.getWidth(xyz1), mc.textRenderer.getWidth(xyz2));
        this.height = mc.textRenderer.fontHeight * 2.2f;
    }
}
