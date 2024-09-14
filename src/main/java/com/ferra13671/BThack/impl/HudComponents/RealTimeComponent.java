package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RealTimeComponent extends HudComponent {
    private final ModeSetting mode = addMode("Hour Mode", new ArrayList<>(Arrays.asList("24", "12")));

    public RealTimeComponent() {
        super("RealTime",
                MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f,
                10,
                true
        );
    }

    @Override
    public List<Setting> initSettings() {
        return Arrays.asList(mode);
    }

    @Override
    public void render(RenderType type) {
        String text = "Real Time " + Formatting.WHITE + (mode.getValue().equals("12") ? new SimpleDateFormat("h:mm").format(new Date()) : new SimpleDateFormat("k:mm").format(new Date()));
        drawText(text, (int) getX(), (int) getY());
        this.width = mc.textRenderer.getWidth(text);
        this.height = mc.textRenderer.fontHeight;
    }
}
