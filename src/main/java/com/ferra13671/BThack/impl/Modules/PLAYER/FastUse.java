package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.*;

public class FastUse extends Module {

    public static BooleanSetting experiense;
    public static BooleanSetting crystals;
    public static BooleanSetting fishRods;
    public static BooleanSetting throwables;
    public static BooleanSetting others;

    public FastUse() {
        super("FastUse",
                "lang.module.FastUse",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );

        experiense = new BooleanSetting("Experience", this, true);
        crystals = new BooleanSetting("Crystals", this, true);
        fishRods = new BooleanSetting("Fishing Rods", this, true);
        throwables = new BooleanSetting("Throwables", this, true);
        others = new BooleanSetting("Others", this, true);

        initSettings(
                experiense,
                crystals,
                fishRods,
                throwables,
                others
        );
    }

    @EventSubscriber
    public void onUpdate(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        if (experiense.getValue()) {
            if (mc.player.getMainHandStack().getItem() instanceof ExperienceBottleItem) {
                mc.itemUseCooldown = 0;
            }
        }
        if (crystals.getValue()) {
            if (mc.player.activeItemStack.getItem() instanceof EndCrystalItem) {
                mc.itemUseCooldown = 0;
            }
        }
        if (fishRods.getValue()) {
            if (mc.player.activeItemStack.getItem() instanceof FishingRodItem) {
                mc.itemUseCooldown = 0;
            }
        }
        if (throwables.getValue()) {
            if (mc.player.activeItemStack.getItem() instanceof EggItem || mc.player.activeItemStack.getItem() instanceof EnderPearlItem || mc.player.activeItemStack.getItem() instanceof EnderEyeItem || mc.player.activeItemStack.getItem() instanceof FireworkRocketItem || mc.player.activeItemStack.getItem() instanceof SnowballItem || mc.player.activeItemStack.getItem() instanceof SplashPotionItem) {
                mc.itemUseCooldown = 0;
            }
        }
        if (others.getValue()) {
            if (!(mc.player.activeItemStack.getItem() instanceof FishingRodItem || mc.player.getMainHandStack().getItem() instanceof BlockItem)) {
                mc.itemUseCooldown = 0;
            }
        }
    }
}
