package com.ferra13671.BThack.impl.Modules.PLAYER;


import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.ItemsUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.item.Items;

public class AutoPearl extends Module {

    public static BooleanSetting swingHand;

    public AutoPearl() {

        super("AutoPearl",
                "lang.module.AutoPearl",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );

        swingHand = new BooleanSetting("Swing Hand", this, true);

        initSettings(
                swingHand
        );
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        ItemsUtils.useItem(Items.ENDER_PEARL, swingHand.getValue());
        toggle();
    }
}
