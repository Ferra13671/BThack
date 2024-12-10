package com.ferra13671.BThack.impl.Modules.PLAYER.InventoryManager;

import com.ferra13671.BThack.api.Events.Block.AttackBlockEvent;
import com.ferra13671.BThack.api.Events.Block.UseBlockEvent;
import com.ferra13671.BThack.api.Events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.api.Events.Render.RenderHudPostEvent;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Ferra13671 and Nikitadan4pi
 */

public class InventoryManager extends Module{
    public static ModeSetting page;

    public static BooleanSetting replan;
    public static BooleanSetting saver;
    public static BooleanSetting dropper;

    public static NumberSetting count;
    public static ModeSetting delayMode;
    public static NumberSetting delay;

    public static NumberSetting minStrength;
    public static BooleanSetting attackSaver;

    public static Replanish replanish;
    public static ItemSaver itemSaver;

    public InventoryManager () {
        super ("InventoryManager",
                "lang.module.InventoryManager",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false);

        page = new ModeSetting("Page", this, new ArrayList<>(Arrays.asList("Replanish", "ItemSaver", "TrashDropper")));
        replanish = new Replanish(this);
        itemSaver = new ItemSaver(this);

        replan = new BooleanSetting("Replanish", this, false, () -> page.getValue().equals("Replanish"));
        saver = new BooleanSetting ("ItemSaver", this, false, () -> page.getValue().equals("ItemSaver"));
        dropper = new BooleanSetting ("TrashDropper", this, false, () -> page.getValue().equals("TrashDropper"));

        initSettings(
                page,
                replan,
                saver,
                dropper,

                count,
                delayMode,
                delay,

                minStrength,
                attackSaver
        );
    }

    @Override
    public void onEnable(){
        super.onEnable();
        if(replan.getValue()) replanish.onEnable();
    }

    @EventSubscriber
    public void onTick (ClientTickEvent e) {
        if (nullCheck()) return;
        if (replan.getValue()) replanish.onTick(e);
    }

    @EventSubscriber
    public void onRenderOverlay(RenderHudPostEvent e) {
        if (saver.getValue()) itemSaver.onRenderOverlay(e);
    }

    @EventSubscriber
    public void onAttackBlock(AttackBlockEvent e) {
        if (nullCheck()) return;

        if (saver.getValue())itemSaver.onAttackBlock(e);
    }

    @EventSubscriber
    public void onUseBlock(UseBlockEvent e) {
        if (nullCheck()) return;

        if (saver.getValue()) itemSaver.onUseBlock(e);
    }

    @EventSubscriber
    public void onAttack(AttackEntityEvent e) {
        if (nullCheck()) return;

        if (saver.getValue()) itemSaver.onAttack(e);
    }

}