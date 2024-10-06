package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Events.Events.InputEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.KeyCodeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Player.PlayerUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.UUID;

public class MultiFakePlayer extends Module {

    public static KeyCodeSetting summonKey;

    public MultiFakePlayer() {
        super("MultiFakePlayer",
                "lang.module.MultiFakePlayer",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );

        summonKey = new KeyCodeSetting("Summon Key", this);

        initSettings(
                summonKey
        );
    }

    ArrayList<Entity> fakePlayers = new ArrayList<>();

    @EventSubscriber
    public void onInput(InputEvent.KeyInputEvent e) {
        if (nullCheck()) return;

        if (summonKey.isPressed()) {
            Entity fakePlayer = PlayerUtils.createNewFakePlayer(mc.player, new GameProfile(UUID.randomUUID(), "FakePlayer" + fakePlayers.size()));
            fakePlayer.tick();
            fakePlayers.add(fakePlayer);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        for (Entity fakePlayer : fakePlayers) {
            PlayerUtils.removeEntity(fakePlayer);
        }

        fakePlayers.clear();
    }
}