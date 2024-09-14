package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ServerIpComponent extends HudComponent {

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
    public void render(RenderType type) {
        String renderString = "";

        if (mc.isIntegratedServerRunning()) {
            renderString = !isShort.getValue() ? ("IP " + Formatting.WHITE + "Singleplayer") : (Formatting.WHITE + "Singleplayer");
        } else {
            renderString = !isShort.getValue() ? ("IP " + Formatting.WHITE + Objects.requireNonNull(mc.getCurrentServerEntry()).address) : (Formatting.WHITE + Objects.requireNonNull(mc.getCurrentServerEntry()).address);
        }

        this.width = mc.textRenderer.getWidth(renderString);
        this.height = mc.textRenderer.fontHeight;
        drawText(renderString, (int) getX(), (int) getY());
    }
}
