package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Events.Events.PacketEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class NoRender extends Module {

    public static BooleanSetting explosions;
    public static BooleanSetting particles;
    public static BooleanSetting overlay;
    public static BooleanSetting waterFog;
    public static BooleanSetting lavaFog;
    public static BooleanSetting powderSnowFog;

    public static BooleanSetting chestRender;
    public static NumberSetting chestRadius;

    public static BooleanSetting shulkerRender;
    public static NumberSetting shulkerRadius;

    public static BooleanSetting eTableRender;
    public static NumberSetting eTableRadius;

    public NoRender() {
        super("NoRender",
                "lang.module.NoRender",
                KeyboardUtils.RELEASE,
                Category.RENDER,
                false
        );

        explosions = new BooleanSetting("Explosions", this, true);
        particles = new BooleanSetting("Particles", this, false);
        overlay = new BooleanSetting("Overlay", this, true);
        waterFog = new BooleanSetting("Water Fog", this, true);
        lavaFog = new BooleanSetting("Lava Fog", this, true);
        powderSnowFog = new BooleanSetting("PowderSnow Fog", this, true);

        chestRender = new BooleanSetting("Chest Render", this, false);
        chestRadius = new NumberSetting("CRender Range", this, 10, 5, 50, false, chestRender::getValue);

        shulkerRender = new BooleanSetting("Shulker Render", this, false);
        shulkerRadius = new NumberSetting("SRender Range", this, 20, 5, 50, false, shulkerRender::getValue);

        eTableRender = new BooleanSetting("ETable Render", this, false);
        eTableRadius = new NumberSetting("ETRender Range", this, 10, 5, 50, false, eTableRender::getValue);


        initSettings(
                explosions,
                particles,
                overlay,
                waterFog,
                lavaFog,
                powderSnowFog,

                chestRender,
                chestRadius,

                shulkerRender,
                shulkerRadius,

                eTableRender,
                eTableRadius
        );
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (nullCheck()) return;

        if (explosions.getValue() && e.getPacket() instanceof ExplosionS2CPacket packet) {
            mc.world.playSound(mc.player ,packet.getX(), packet.getY(), packet.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE);
            e.setCancelled(true);
        }
    }
}
