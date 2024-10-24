package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.Events.Entity.FireworkTickEvent;
import com.ferra13671.BThack.Events.PacketEvent;
import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Grim.GrimFreezeUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.IFireworkRocketEntity;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class GrimRocket extends Module {

    public GrimRocket() {
        super("GrimRocket",
                "lang.module.GrimRocket",
                KeyboardUtils.RELEASE,
                Category.MOVEMENT,
                false
        );
    }

    private boolean extendFirework;
    private FireworkRocketEntity firework;

    @Override
    public void onDisable() {
        super.onDisable();
        if (firework != null) {
            ((IFireworkRocketEntity) firework).hookExplodeAndRemove();
        }
        firework = null;
        extendFirework = false;
        GrimFreezeUtils.stopFreeze();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventSubscriber
    public void onFireworkTick(FireworkTickEvent e) {
        if (mc.player == null) {
            return;
        }
        if (mc.player.isFallFlying() && firework != e.firework
                && ((IFireworkRocketEntity) e.firework).hookWasShotByEntity()
                && ((IFireworkRocketEntity) e.firework).getShooter() == mc.player) {
            extendFirework = true;
            e.setCancelled(true);
            firework = e.firework;
            GrimFreezeUtils.freeze(57000);
        }
    }

    @EventSubscriber
    public void onTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;
        if (!extendFirework) {
            return;
        }
        if (!mc.player.isFallFlying() || mc.player.isOnGround() || GrimFreezeUtils.getFreezeMillis() <= 0) {
            extendFirework = false;
            if (firework != null) {
                ((IFireworkRocketEntity) firework).hookExplodeAndRemove();
                firework = null;
            }
            GrimFreezeUtils.stopFreeze();
        }
    }

    @EventSubscriber
    public void onPacketSend(PacketEvent.Send e) {
        if (nullCheck()) return;
        if (e.getPacket() instanceof CommonPongC2SPacket packet
                && (!extendFirework || !mc.player.isFallFlying())) {
            GrimFreezeUtils.stopFreeze();
        }
    }

    @EventSubscriber
    public void onPacketReceive(PacketEvent.Receive e) {
        if (nullCheck() || !mc.player.isFallFlying() || !extendFirework) {
            return;
        }
        if (e.getPacket() instanceof EntitiesDestroyS2CPacket packet && firework != null) {
            for (int id : packet.getEntityIds()) {
                if (id == firework.getId()) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
        if (e.getPacket() instanceof PlayerPositionLookS2CPacket packet) {
            extendFirework = false;
            if (firework != null) {
                ((IFireworkRocketEntity) firework).hookExplodeAndRemove();
                firework = null;
            }
            GrimFreezeUtils.stopFreeze();
        }
    }
}
