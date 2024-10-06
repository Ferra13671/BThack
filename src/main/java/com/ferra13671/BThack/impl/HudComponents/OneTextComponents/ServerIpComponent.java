package com.ferra13671.BThack.impl.HudComponents.OneTextComponents;

import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.impl.HudComponents.AbstractOneTextComponent;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ServerIpComponent extends AbstractOneTextComponent {

    private final BooleanSetting isShort = addCheckbox("Short", false);

    public ServerIpComponent() {
        super("ServerIp",
                5,
                95,
                true
        );
    }

    @Override
    public List<Setting> initSettings() {
        return Arrays.asList(isShort);
    }

    @Override
    public String getText() {
        return (isShort.getValue() ? "" : "IP") + Formatting.WHITE + (mc.isIntegratedServerRunning() ? "Singleplayer" : Objects.requireNonNull(mc.getCurrentServerEntry()).address);
    }
}
