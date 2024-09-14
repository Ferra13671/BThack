package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.mixins.accessor.IPlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;

public class CrystalCountComponent extends HudComponent {

    public CrystalCountComponent() {
        super("CrystalCount",
                5,
                225,
                false
        );
    }

    @Override
    public void render(RenderType type) {
        int crystals = 0;

        for (DefaultedList<ItemStack> list : ((IPlayerInventory) mc.player.getInventory()).getCombinedInventory()) {
            for (ItemStack stack : list) {
                if (stack.getItem() == Items.END_CRYSTAL) {
                    crystals += stack.getCount();
                }
            }
        }

        //if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
        //    crystals += mc.player.getHeldItemOffhand().getCount();
        //}

        String text = "Crystals: " + Formatting.WHITE + crystals;

        drawText(text, (int) getX(), (int) getY());
        this.width = mc.textRenderer.getWidth(text);
        this.height = mc.textRenderer.fontHeight;
    }
}
