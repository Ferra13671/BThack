package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PingComponent extends AbstractOneTextComponent {
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
    public String getText() {
        return switch (mode.getValue()) {
            case "Normal" -> "Ping " + Formatting.WHITE + getPing() + "ms";
            case "Short" -> "" + Formatting.WHITE + getPing() + "ms";
            default -> "";
        };
    }

    private int getPing() {
        if (mc.player != null && mc.getNetworkHandler() != null && mc.getNetworkHandler().getPlayerListEntry(mc.player.getGameProfile().getName()) != null) {
            return Objects.requireNonNull(mc.getNetworkHandler().getPlayerListEntry(mc.player.getGameProfile().getName())).getLatency();
        }

        return -1;
    }
}
