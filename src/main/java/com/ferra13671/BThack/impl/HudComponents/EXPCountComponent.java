package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.mixins.accessor.IPlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;

public class EXPCountComponent extends HudComponent {

    public EXPCountComponent() {
        super("EXPCount",
                5,
                235,
                false
        );
    }

    @Override
    public void render(RenderType type) {
        int bootles = 0;

        for (DefaultedList<ItemStack> list : ((IPlayerInventory) mc.player.getInventory()).getCombinedInventory()) {
            for (ItemStack stack : list) {
                if (stack.getItem() == Items.EXPERIENCE_BOTTLE) {
                    bootles += stack.getCount();
                }
            }
        }

        String text = "EXP Bottles: " + Formatting.WHITE + bootles;

        drawText(text, (int) getX(), (int) getY());
        this.width = mc.textRenderer.getWidth(text);
        this.height = mc.textRenderer.fontHeight;
    }
}
