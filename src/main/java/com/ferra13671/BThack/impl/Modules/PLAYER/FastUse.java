package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.mixins.accessor.IMinecraftClient;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.*;

import java.util.Arrays;

public class FastUse extends Module {

    public static ModeSetting mode;
    public static NumberSetting times;

    public static BooleanSetting crystals;
    public static BooleanSetting fishRods;
    public static BooleanSetting throwables;
    public static BooleanSetting expBottle;
    public static BooleanSetting others;


    public FastUse() {
        super("FastUse",
                "lang.module.FastUse",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        mode = new ModeSetting("Mode", this, Arrays.asList("Normal", "Ultra"));
        times = new NumberSetting("Times", this, 30, 5, 64, true, () -> mode.getValue().equals("Ultra"));

        crystals = new BooleanSetting("Crystals", this, true);
        fishRods = new BooleanSetting("Fishing Rods", this, true);
        throwables = new BooleanSetting("Throwables", this, false);
        expBottle = new BooleanSetting("ExpBottle", this, true, () -> !throwables.getValue());
        others = new BooleanSetting("Others", this, true);

        initSettings(
                mode,
                times,

                crystals,
                fishRods,
                throwables,
                expBottle,
                others
        );
    }

    @EventSubscriber
    public void onUpdate(ClientTickEvent e) {
        if (nullCheck()) return;

        Item item = mc.player.activeItemStack.getItem();

        if (crystals.getValue())
            if (item instanceof EndCrystalItem)
                fastUseAction();
        if (fishRods.getValue())
            if (item instanceof FishingRodItem)
                fastUseAction();
        if (throwables.getValue())
            if (item instanceof ExperienceBottleItem ||
                    item instanceof EggItem ||
                    item instanceof EnderPearlItem ||
                    item instanceof EnderEyeItem ||
                    item instanceof FireworkRocketItem ||
                    item instanceof SnowballItem ||
                    item instanceof SplashPotionItem
            )
                fastUseAction();
        if (expBottle.getValue() && !throwables.getValue())
            if (item instanceof ExperienceBottleItem)
                fastUseAction();
        if (others.getValue())
            if (!(item instanceof FishingRodItem || item instanceof BlockItem) &&
                    !item.isFood() &&
                    !(item instanceof BowItem)
            )
                fastUseAction();
    }

    public void fastUseAction() {
        if (mode.getValue().equals("Normal"))
            mc.itemUseCooldown = 0;
        else
            ultraMegaSuper52691488UltimateUse();
    }

    public void ultraMegaSuper52691488UltimateUse() {
        if (mc.options.useKey.isPressed())
            for (int i = 0; i < (int) times.getValue(); i++)
                ((IMinecraftClient) mc).useItem();
    }
}
