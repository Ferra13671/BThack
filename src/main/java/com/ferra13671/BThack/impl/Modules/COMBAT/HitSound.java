package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.Arrays;

public class HitSound extends Module {

    public static ModeSetting sound;
    public static NumberSetting volume;

    public HitSound() {
        super("HitSound",
                "lang.module.HitSound",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        sound = new ModeSetting("Sound", this, new ArrayList<>(Arrays.asList("Ding","Meow","Villager","Enderman","EnderDragon","Blaze","Chicken","Cow")));
        volume = new NumberSetting("Volume", this, 1, 0.5, 3, false);

        initSettings(
                sound,
                volume
        );
    }

    @EventSubscriber
    public void onUpdate(AttackEntityEvent e) {
        if (nullCheck()) return;

        if (e.getPlayer() == mc.player) {
            if (e.getEntity() instanceof EndCrystalEntity) return;

            float volume1 = (float) volume.getValue();

            switch (sound.getValue()) {
                case "Ding":
                    mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, volume1, 1);
                    break;
                case "Meow":
                    mc.player.playSound(SoundEvents.ENTITY_CAT_AMBIENT, volume1, 1);
                    break;
                case "Villager":
                    mc.player.playSound(SoundEvents.ENTITY_VILLAGER_HURT, volume1, 1);
                    break;
                case "Enderman":
                    mc.player.playSound(SoundEvents.ENTITY_ENDERMAN_HURT, volume1, 1);
                    break;
                case "EnderDragon":
                    mc.player.playSound(SoundEvents.ENTITY_ENDER_DRAGON_HURT, volume1, 1);
                    break;
                case "Blaze":
                    mc.player.playSound(SoundEvents.ENTITY_BLAZE_HURT, volume1, 1);
                    break;
                case "Chicken":
                    mc.player.playSound(SoundEvents.ENTITY_CHICKEN_HURT, volume1, 1);
                    break;
                case "Cow":
                    mc.player.playSound(SoundEvents.ENTITY_COW_HURT, volume1, 1);
                    break;
            }
        }
    }
}