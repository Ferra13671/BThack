package com.ferra13671.BThack.impl.Modules.MOVEMENT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Arrays;

public class SafeWalk extends Module {

    public static ModeSetting mode;
    public static NumberSetting edgeDistance;

    public SafeWalk() {
        super("SafeWalk",
                "lang.module.SafeWalk",
                KeyboardUtils.RELEASE,
                MCategory.MOVEMENT,
                false
        );

        mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Normal", "Shift", "Legit Shift")));
        edgeDistance = new NumberSetting("Edge Distance", this, 0.05, 0.05, 0.25, false);

        initSettings(
                mode,
                edgeDistance
        );
    }

    private boolean sneaking;

    @Override
    public void onEnable() {
        super.onEnable();

        sneaking = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (sneaking)
            sneaking = false;
        mc.options.sneakKey.setPressed(KeyboardUtils.isKeyDown(mc.options.sneakKey.getDefaultKey().getCode()));
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck() || !mode.getValue().equals("Legit Shift") || !mc.player.verticalCollision) return;

        if (BuildManager.ignoreBlocks.contains(mc.world.getBlockState(new ModifyBlockPos(mc.player.getX(), mc.player.getY() - 0.2, mc.player.getZ())).getBlock())) {
            mc.options.sneakKey.setPressed(true);
        } else {
            mc.options.sneakKey.setPressed(KeyboardUtils.isKeyDown(mc.options.sneakKey.getDefaultKey().getCode()));
        }
    }

    public void onClipAtLedge(boolean clipping) {
        if (mode.getValue().equals("Legit Shift")) {
            if (sneaking)
                setSneaking(false);
            return;
        }

        if(!isEnabled() || mode.getValue().equals("Normal") || !mc.player.isOnGround()) {
            if(sneaking)
                setSneaking(false);

            return;
        }

        Box box = mc.player.getBoundingBox();
        Box adjustedBox = box.stretch(0, -mc.player.stepHeight, 0)
                .expand(-edgeDistance.getValue(), 0, -edgeDistance.getValue());

        if(mc.world.isSpaceEmpty(mc.player, adjustedBox))
            clipping = true;

        setSneaking(clipping);
    }

    private void setSneaking(boolean sneaking) {
        KeyBinding sneakKey = mc.options.sneakKey;

        if(sneaking)
            sneakKey.setPressed(true);
        else
            sneakKey.setPressed(KeyboardUtils.isKeyDown(sneakKey.getDefaultKey().getCode()));

        this.sneaking = sneaking;
    }
}
