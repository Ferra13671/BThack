package com.ferra13671.BThack.impl.Modules.PLAYER.InventoryManager;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Events.Block.AttackBlockEvent;
import com.ferra13671.BThack.api.Events.Block.UseBlockEvent;
import com.ferra13671.BThack.api.Events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.api.Events.Render.RenderHudPostEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.MegaEvents.Base.Event;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class ItemSaver implements Mc {

    public ItemSaver(Module module) {

        InventoryManager.minStrength = new NumberSetting("Min Strength(%)", module, 5, 1, 90, false, () -> InventoryManager.page.getValue().equals("ItemSaver"));
        InventoryManager.attackSaver = new BooleanSetting("Attack Saver", module, true, () -> InventoryManager.page.getValue().equals("ItemSaver"));
    }

    private int alpha = 0;

    public void onRenderOverlay(RenderHudPostEvent e) {
        if (alpha > 0) {
            BThackRender.drawString(LanguageSystem.translate("lang.module.ItemSaver.saveMessage"), (mc.getWindow().getScaledWidth() / 2f) - (mc.textRenderer.getWidth(LanguageSystem.translate("lang.module.ItemSaver.saveMessage")) / 2f), (mc.getWindow().getScaledHeight() / 2f) + 40, new Color(255, 98, 0, Math.min(Math.max(alpha, 1), 255)).hashCode());
        }
        if (alpha > 0) alpha--;
    }

    public void onAttackBlock(AttackBlockEvent e) {
        check(e);
    }

    public void onUseBlock(UseBlockEvent e) {
        check(e);
    }

    public void onAttack(AttackEntityEvent e) {

        if (InventoryManager.attackSaver.getValue()) check(e);
    }


    private void check(Event e) {
        ItemStack item = InventoryUtils.getItem(mc.player.getInventory().selectedSlot);
        if (item.getItem() instanceof BlockItem) return;
        float currentDamage = ItemUtils.getItemDurabilityInPercentages(item);
        if (currentDamage < InventoryManager.minStrength.getValue()) {
            if (alpha != 380) alpha = 380;
            e.setCancelled(true);
        }
    }
}
