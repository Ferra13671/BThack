package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Utils.SpeedMathThread;
import net.minecraft.util.Formatting;

import java.text.DecimalFormat;

public class SpeedComponent extends HudComponent {

    public SpeedComponent() {
        super("Speed",
                5,
                105,
                true
        );
    }

    @Override
    public void render(RenderType type) {
        if (nullCheck()) return;

        String speed = new DecimalFormat(pattern).format(SpeedMathThread.speed);

        String text = "Speed: " + Formatting.WHITE + speed + "b/s";

        drawText(text,
                (int) this.getX(), (int) this.getY());

        this.width = mc.textRenderer.getWidth(text);
        this.height = mc.textRenderer.fontHeight;
    }
}
