package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.mixins.accessor.IPlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;

public class GappleCountComponent extends HudComponent {
    public GappleCountComponent() {
        super("GappleCount",
                5,
                245,
                false
        );
    }

    @Override
    public void render(RenderType type) {
        int gapples = 0;

        for (DefaultedList<ItemStack> list : ((IPlayerInventory) mc.player.getInventory()).getCombinedInventory()) {
            for (ItemStack stack : list) {
                if (stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
                    gapples += stack.getCount();
                }
            }
        }

        String text = "Gapples: " + Formatting.WHITE + gapples;

        drawText(text, (int) getX(), (int) getY());
        this.width = mc.textRenderer.getWidth(text);
        this.height = mc.textRenderer.fontHeight;
    }
}
