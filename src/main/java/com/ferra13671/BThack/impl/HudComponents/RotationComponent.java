package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.HudComponent.HudComponent;
import net.minecraft.util.Formatting;

import java.text.DecimalFormat;

public class RotationComponent extends HudComponent {

    public RotationComponent() {
        super("Rotation",
                5,
                75,
                true
        );
    }

    @Override
    public void render(RenderType type) {
        if (nullCheck()) return;

        String yaw;
        String pitch;
        double preYaw;

        preYaw = mc.player.getYaw() / 360;
        if (preYaw < 0) {
            preYaw = -preYaw;
            preYaw = (-(preYaw - ((int) preYaw))) * 360;
        } else {
            preYaw = (preYaw - ((int) preYaw)) * 360;
        }

        yaw = new DecimalFormat(pattern).format(preYaw);

        pitch = new DecimalFormat(pattern).format(mc.player.getPitch());

        String text = "Yaw: " + Formatting.WHITE + yaw + Formatting.RESET + " " +
                "Pitch: " + Formatting.WHITE + pitch;

        drawText(text,
                (int) this.getX(), (int) this.getY());

        this.width = mc.textRenderer.getWidth(text);
        this.height = mc.textRenderer.fontHeight;
    }
}
