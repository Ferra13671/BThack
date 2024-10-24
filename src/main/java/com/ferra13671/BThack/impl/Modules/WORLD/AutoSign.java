package com.ferra13671.BThack.impl.Modules.WORLD;

import com.ferra13671.BThack.Events.GuiOpenEvent;
import com.ferra13671.BThack.Events.PacketEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.IAbstractSignEditScreen;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;

public class AutoSign extends Module {

    public static BooleanSetting closeScreen;

    public AutoSign() {
        super("AutoSign",
                "lang.module.AutoSign",
                KeyboardUtils.RELEASE,
                Category.WORLD,
                false
        );

        closeScreen = new BooleanSetting("Close Screen", this, true);

        initSettings(
                closeScreen
        );
    }

    private String[] text;

    @Override
    public void onDisable() {
        super.onDisable();
        text = null;
    }

    @EventSubscriber
    public void onPacketSend(PacketEvent.Send e) {
        if (e.getPacket() instanceof UpdateSignC2SPacket packet) {
            text = packet.getText();
        }
    }

    @EventSubscriber
    public void onOpenScreen(GuiOpenEvent e) {
        if (!(e.getScreen() instanceof AbstractSignEditScreen) || text == null) return;

        SignBlockEntity sign = ((IAbstractSignEditScreen) e.getScreen()).getSign();

        mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign.getPos(), true, text[0], text[1], text[2], text[3]));

        if (closeScreen.getValue())
            e.setCancelled(true);
    }
}
