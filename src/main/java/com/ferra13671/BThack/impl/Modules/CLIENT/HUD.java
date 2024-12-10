package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Events.Render.RenderHudPostEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Gui.HudMover.HudMoverScreen;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.SpeedMathThread;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.PathMode;

public class HUD extends Module {

    public static BooleanSetting rainbow;
    public static NumberSetting rainbowType;

    public HUD() {
        super("HUD",
                "lang.module.HUD",
                KeyboardUtils.RELEASE,
                MCategory.CLIENT,
                true
        );

        ModuleList.HUD = this;

        allowRemapKeyCode = false;

        mc.getWindow().swapBuffers();

        rainbow = new BooleanSetting("Rainbow", this, true);
        rainbowType = new NumberSetting("Rainbow type", this, 3, 1, 8, true, () -> rainbow.getValue());

        initSettings(
                rainbow,
                rainbowType
        );
    }

    public static final GLTexture bthack_logo = new GLTexture("assets/bthack/bthacklogo.png", PathMode.INSIDEJAR, GLTexture.ColorMode.RGBA);


    int updateTickDelay = 0;

    @Override
    public void onDisable() {
        super.onDisable();
        updateTickDelay = 0;
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;
        if (mc.currentScreen instanceof HudMoverScreen) return;

        updateTickDelay++;
        if (updateTickDelay < 3) return;
        updateTickDelay = 0;

        for (HudComponent component : Client.hudComponents) {
            component.setToggled(component.parentSetting.getValue());
            if (component.toggled) {
                component.tick();
            }
        }

        if (!SpeedMathThread.active) {
            new SpeedMathThread().start();
        }
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    public void onRender(RenderHudPostEvent e) {
        if (mc.currentScreen instanceof HudMoverScreen) return;
        BThackRender.guiGraphics.getMatrices().push();
        BThackRender.guiGraphics.getMatrices().translate(0,0,3000);

        for (HudComponent component : Client.hudComponents) {
            if (component.toggled) {
                component.render();
            }
        }

        BThackRender.guiGraphics.getMatrices().pop();
    }

    public static int getHUDColor() {
        if (rainbow.getValue()) {
            return ColorUtils.rainbowType((int) rainbowType.getValue());
        } else {
            return ClickGui.getClickGuiColor(false);
        }
    }
}