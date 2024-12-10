package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.impl.Modules.CLIENT.HUD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class WatermarkComponent extends HudComponent {

    private final ModeSetting logoType = addMode("Logo Type", new ArrayList<>(Arrays.asList("Logo", "Text")));

    public WatermarkComponent() {
        super("Watermark",
                5,
                5,
                true
        );
    }

    @Override
    public List<Setting> initSettings() {
        return Arrays.asList(logoType);
    }

    @Override
    public void render() {
        if (nullCheck()) return;

        if (logoType.getValue().equals("Text")) {
            drawText(Client.clientInfo.getCName(), (int) this.getX(), (int) this.getY());
        } else {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            BThackRender.drawTextureRect(HUD.bthack_logo, getX(), getY() - 18, getX() + 138, getY() + 54);
            //glDisable(GL_BLEND);
        }
    }

    @Override
    public void tick() {
        if (nullCheck()) return;

        if (logoType.getValue().equals("Text")) {
            this.width = mc.textRenderer.getWidth(Client.clientInfo.getCName());
            this.height = mc.textRenderer.fontHeight;
        } else {
            this.width = 138;
            this.height = 42;
        }
    }
}
