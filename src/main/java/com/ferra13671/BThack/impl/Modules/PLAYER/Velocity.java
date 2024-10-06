package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Events.Events.PacketEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;

import java.util.ArrayList;
import java.util.Arrays;


public class Velocity extends Module {

    public static ModeSetting mode;

    public static BooleanSetting velocity;
    public static NumberSetting velocityV;
    public static NumberSetting velocityH;

    public static BooleanSetting explosion;
    public static NumberSetting explosionV;
    public static NumberSetting explosionH;

    public static BooleanSetting noPushEntities;
    public static BooleanSetting noPushLiquids;
    public static BooleanSetting noPushBlocks;

    public Velocity() {
        super("Velocity",
                "lang.module.Velocity",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );

        mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Normal", "Cancel")));

        velocity = new BooleanSetting("Velocity", this, true);
        velocityV = new NumberSetting("Velocity Vertical", this, 0.0,0.0,100.0,true, () -> velocity.getValue() && mode.getValue().equals("Normal"));
        velocityH = new NumberSetting("Velocity Horizontal", this, 0.0,0.0,100.0,true, () -> velocity.getValue() && mode.getValue().equals("Normal"));

        explosion = new BooleanSetting("Explosions", this, true);
        explosionV = new NumberSetting("Expl Vertical", this, 0.0,0.0,100.0,true, () -> explosion.getValue() && mode.getValue().equals("Normal"));
        explosionH = new NumberSetting("Expl Horizontal", this, 0.0,0.0,100.0,true, () -> explosion.getValue() && mode.getValue().equals("Normal"));

        noPushEntities = new BooleanSetting("NoPush Entities", this, true);
        noPushLiquids = new BooleanSetting("NoPush Liquids", this, true);
        noPushBlocks = new BooleanSetting("NoPush Blocks", this, true);

        initSettings(
                mode,

                velocity,
                velocityV,
                velocityH,

                explosion,
                explosionV,
                explosionH,

                noPushEntities,
                noPushLiquids,
                noPushBlocks
        );
    }

    @EventSubscriber
    public void onUpdate(PacketEvent.Receive e) {
        if (nullCheck()) return;
        if (mc.player.isFallFlying()) return;

        int velV = (int) explosionV.getValue();
        int velH = (int) explosionH.getValue();
        float explV = (float) explosionV.getValue();
        float explH = (float) explosionH.getValue();

        if (e.getPacket() instanceof EntityVelocityUpdateS2CPacket packet && velocity.getValue()) {
            if (packet.id == mc.player.getId()) {
                switch (mode.getValue()) {
                    case "Normal":
                        if (velH == 0 && velV == 0) {
                            e.setCancelled(true);
                        } else {
                            velV = velV / 100;
                            velH = velH / 100;
                            packet.velocityX *= velH;
                            packet.velocityY *= velV;
                            packet.velocityZ *= velH;
                        }
                        break;
                    case "Cancel":
                        e.setCancelled(true);
                        break;
                }
            }
        }
        if (e.getPacket() instanceof ExplosionS2CPacket packet && explosion.getValue()) {
            switch (mode.getValue()) {
                case "Normal":
                    if (explH == 0.0f && explV == 0.0f) {
                        packet.playerVelocityX = 0;
                        packet.playerVelocityY = 0;
                        packet.playerVelocityZ = 0;
                    } else {
                        explV = explV / 100;
                        explH = explH / 100;
                        packet.playerVelocityX *= explH;
                        packet.playerVelocityY *= explV;
                        packet.playerVelocityZ *= explH;
                    }
                    break;
                case "Cancel":
                    e.setCancelled(true);
                    break;
            }
        }
    }
}
