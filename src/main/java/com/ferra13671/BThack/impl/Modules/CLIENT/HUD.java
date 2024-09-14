package com.ferra13671.BThack.impl.Modules.CLIENT;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.FileSystem.ConfigInit.ConfigInit;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Events.Events.Render.RenderHudPostEvent;
import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Plugin.PluginUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.SpeedMathThread;
import com.ferra13671.BThack.impl.HudComponents.*;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;

public class HUD extends Module {
    public static Identifier bthack_logo = new Identifier("bthack", "bthacklogo.png");
    public static Identifier fire = new Identifier("bthack", "fire.png");
    int alpha = 255;
    boolean invertAlpha = true;

    public static NumberSetting rainbowType;

    public HUD() {
        super("HUD",
                "lang.module.HUD",
                KeyboardUtils.RELEASE,
                Category.CLIENT,
                true
        );

        Client.hudModule = this;

        allowRemapKeyCode = false;

        mc.getWindow().swapBuffers();

        rainbowType = new NumberSetting("Rainbow type", this, 3, 1, 8, true);

        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitHudComponents);

        Client.hudComponents.addAll(Arrays.asList(
                new WatermarkComponent(),
                new FPSComponent(),
                new CoordinatesComponent(),
                new RotationComponent(),
                new DirectionComponent(),
                new ServerIpComponent(),
                new SpeedComponent(),
                new PingComponent(),
                new TPSComponent(),
                new PlayerCountComponent(),
                new InventoryComponent(),
                new ArmorComponent(),
                new RealTimeComponent(),
                new MinecraftTimeComponent(),


                new DimensionComponent(),
                new DurabilityComponent(),
                new CrystalCountComponent(),
                new EXPCountComponent(),
                new GappleCountComponent(),
                new TotemCountComponent(),

                new TextRadarComponent(),



                new ArrayListComponent()
        ));
        Client.hudComponents.addAll(PluginUtils.getPluginsHudComponents());

        try {
            ConfigInit.loadHudComponents();
        } catch (IOException ignored) {}

        initSettings(rainbowType);
    }

    @EventSubscriber
    public void onTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        for (HudComponent component : Client.hudComponents) {
            component.setToggled(component.parentSetting.getValue());
        }
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    public void onRender(RenderHudPostEvent e) {
        for (HudComponent component : Client.hudComponents) {
            if (component.toggled) {
                component.render(HudComponent.RenderType.TEXT);
                component.render(HudComponent.RenderType.IMAGE);
            }
        }

        if (mc.player.isOnFire()) {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            float size = 20;
            float x = (mc.getWindow().getScaledWidth() / 2f) - size;
            float y = mc.getWindow().getScaledHeight() - (mc.getWindow().getScaledHeight() / 6f);
            RenderSystem.setShaderColor(1, 1, 1, alpha / 255f);
            BThackRender.drawTextureRect(fire, x, y, x + size, y + size);
            glDisable(GL_BLEND);
            if (!invertAlpha)
                alpha += 2;
            else alpha -= 2;
            if (alpha >= 255 || alpha <= 0)
                invertAlpha = !invertAlpha;
            RenderSystem.setShaderColor(1,1,1,1);
        }

        if (!SpeedMathThread.active) {
            new SpeedMathThread().start();
        }
    }
}