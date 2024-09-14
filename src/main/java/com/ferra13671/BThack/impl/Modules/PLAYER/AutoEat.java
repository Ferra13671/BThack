package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.util.Hand;

public class AutoEat extends Module {

    public static NumberSetting startFoodLevel;
    public static BooleanSetting allowChorus;

    public static BooleanSetting eatGapples;
    public static NumberSetting startHP;


    public AutoEat() {
        super("AutoEat",
                "lang.module.AutoEat",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false
        );

        startFoodLevel = new NumberSetting("Start FoodL", this, 15, 6, 19, false);
        allowChorus = new BooleanSetting("Allow Chorus", this, false);


        eatGapples = new BooleanSetting("Eat Gapples", this, false);
        startHP = new NumberSetting("Start HP", this, 15, 5, 19, false, eatGapples::getValue);


        initSettings(
                startFoodLevel,
                allowChorus,

                eatGapples,
                startHP
        );
    }

    public boolean foodEating = false;
    public boolean gappleEating = false;

    @Override
    public void onEnable() {
        super.onEnable();
        foodEating = false;
        gappleEating = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        foodEating = false;
        gappleEating = false;
    }

    @EventSubscriber
    public void onTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        onAutoEat();
    }

    public void onAutoEat() {
        if (mc.player.getHealth() <= startHP.getValue()) {
            Item mainItem = mc.player.getMainHandStack().getItem();
            if (mainItem != Items.ENCHANTED_GOLDEN_APPLE) {
                for (int i = 0; i < 36; i++) {
                    Item item = mc.player.getInventory().getStack(i).getItem();
                    if (item == Items.ENCHANTED_GOLDEN_APPLE) {
                        if (i < 9) {
                            InventoryUtils.swapItem(i);
                            gappleEating = true;
                        } else {
                            for (int a = 0; a < 9; a++) {
                                if (mc.player.getInventory().getStack(a).getItem() == Items.AIR) {
                                    InventoryUtils.swapItemOnInventory(a, i);
                                    InventoryUtils.swapItem(a);
                                    gappleEating = true;
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            } else {
                mc.options.useKey.setPressed(true);
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                gappleEating = true;
            }
        } else {
            if (gappleEating) {
                mc.options.useKey.setPressed(KeyboardUtils.isKeyDown(mc.options.useKey.getDefaultKey().getCode()));
                gappleEating = false;
            }
        }

        if (!gappleEating) {
            if (mc.player.getHungerManager().getFoodLevel() <= startFoodLevel.getValue()) {
                Item mainItem = mc.player.getMainHandStack().getItem();
                if (!mainItem.isFood() && !isAllowedFood(mainItem.getFoodComponent())) {
                    for (int i = 0; i < 36; i++) {
                        Item item = mc.player.getInventory().getStack(i).getItem();
                        if (item.isFood() && isAllowedFood(item.getFoodComponent())) {
                            if (i < 9) {
                                InventoryUtils.swapItem(i);
                                foodEating = true;
                            } else {
                                for (int a = 0; a < 9; a++) {
                                    if (mc.player.getInventory().getStack(a).getItem() == Items.AIR) {
                                        InventoryUtils.swapItemOnInventory(a, i);
                                        InventoryUtils.swapItem(a);
                                        foodEating = true;
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                } else {
                    mc.options.useKey.setPressed(true);
                    mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                    foodEating = true;
                }
            } else {
                if (foodEating) {
                    mc.options.useKey.setPressed(KeyboardUtils.isKeyDown(mc.options.useKey.getDefaultKey().getCode()));
                    foodEating = false;
                }
            }
        }
    }

    private boolean isAllowedFood(FoodComponent food) {
        if (food == null) return false;
        if(!allowChorus.getValue() && food == FoodComponents.CHORUS_FRUIT)
            return false;

        for(Pair<StatusEffectInstance, Float> pair : food.getStatusEffects())
        {
            StatusEffect effect = pair.getFirst().getEffectType();

            if(effect == StatusEffects.HUNGER)
                return false;
            if(effect == StatusEffects.POISON)
                return false;
        }

        return true;
    }
}
