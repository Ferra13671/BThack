package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PingComponent extends HudComponent {
    private final ModeSetting mode = addMode("Ping Mode", new ArrayList<>(Arrays.asList("Normal", "Short")));

    public PingComponent() {
        super("Ping",
                5,
                115,
                true
        );
    }

    @Override
    public List<Setting> initSettings() {
        return Arrays.asList(mode);
    }

    @Override
    public void render(RenderType type) {
        if (nullCheck()) return;

        String renderString;

        switch (mode.getValue()) {
            case "Normal":
                renderString = "Ping " + Formatting.WHITE + getPing() + "ms";
                this.width = mc.textRenderer.getWidth(renderString);
                this.height = mc.textRenderer.fontHeight;
                drawText(renderString, (int) getX(), (int) getY());
                break;
            case "Short":
                renderString = "" + Formatting.WHITE + getPing() + "ms";
                this.width = mc.textRenderer.getWidth(renderString);
                this.height = mc.textRenderer.fontHeight;
                drawText(renderString, (int) getX(), (int) getY());
                break;
        }
    }

    private int getPing() {
        if (mc.player != null && mc.getNetworkHandler() != null && mc.getNetworkHandler().getPlayerListEntry(mc.player.getGameProfile().getName()) != null) {
            return Objects.requireNonNull(mc.getNetworkHandler().getPlayerListEntry(mc.player.getGameProfile().getName())).getLatency();
        }

        return -1;
    }
}
