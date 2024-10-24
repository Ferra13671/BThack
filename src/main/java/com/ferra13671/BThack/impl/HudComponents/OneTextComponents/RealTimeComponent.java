package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RealTimeComponent extends AbstractOneTextComponent {
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
    public String getText() {
        return "Real Time " + Formatting.WHITE + (mode.getValue().equals("12") ? new SimpleDateFormat("h:mm").format(new Date()) : new SimpleDateFormat("k:mm").format(new Date()));
    }
}
