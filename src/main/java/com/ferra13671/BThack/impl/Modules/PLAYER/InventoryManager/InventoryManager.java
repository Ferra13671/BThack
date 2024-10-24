package com.ferra13671.BThack.impl.Modules.PLAYER.InventoryManager;

import com.ferra13671.BThack.Events.Block.AttackBlockEvent;
import com.ferra13671.BThack.Events.Block.UseBlockEvent;
import com.ferra13671.BThack.Events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.Events.Render.RenderHudPostEvent;
import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Ferra13671 and Nikitadan4pi
 */

public class InventoryManager extends Module {
    public static ModeSetting page;

    public static BooleanSetting replanishMode;
    public static NumberSetting count;
    public static ModeSetting delayMode;
    public static NumberSetting delay;

    public static BooleanSetting itemSaverMode;
    public static NumberSetting minStrength;
    public static BooleanSetting attackSaver;
    
    public static Replanish replanish;
    public static ItemSaver itemSaver;
    
    public InventoryManager () {
        super ("InventoryManager",
                "lang.module.InventoryManager",
                KeyboardUtils.RELEASE,
                Category.PLAYER,
                false);
                
        page = new ModeSetting("Page", this, new ArrayList<>(Arrays.asList("Replanish", "ItemSaver", "TrashDropper")));

        replanishMode = new BooleanSetting("Replanish", this, true);
        replanish = new Replanish(this);

        itemSaverMode = new BooleanSetting("Item Saver", this, true);
        itemSaver = new ItemSaver(this);

        initSettings(
                page,

                itemSaverMode,
                count,
                delayMode,
                delay,

                itemSaverMode,
                minStrength,
                attackSaver
        );
    }

    @Override
    public void onEnable(){
      super.onEnable();
      replanish.onEnable();
    }

    @EventSubscriber
    public void ontick (TickEvent.ClientTickEvent e) {
        if (nullCheck() || !replanishMode.getValue()) return;
      replanish.onTick(e);
    }

    @EventSubscriber
    public void onRenderOverlay(RenderHudPostEvent e) {
        if (!itemSaverMode.getValue()) return;
        itemSaver.onRenderOverlay(e);
    }

    @EventSubscriber
    public void onAttackBlock(AttackBlockEvent e) {
        if (nullCheck() || !itemSaverMode.getValue()) return;

        itemSaver.onAttackBlock(e);
    }

    @EventSubscriber
    public void onUseBlock(UseBlockEvent e) {
        if (nullCheck() || !itemSaverMode.getValue()) return;

        itemSaver.onUseBlock(e);
    }

    @EventSubscriber
    public void onAttack(AttackEntityEvent e) {
        if (nullCheck() || !itemSaverMode.getValue()) return;

        itemSaver.onAttack(e);
    }
  
}