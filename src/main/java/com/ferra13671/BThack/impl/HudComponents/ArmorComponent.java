package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Utils.ItemsUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.awt.*;

public class ArmorComponent extends HudComponent {

    public ArmorComponent() {
        super("Armor",
                (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f) + (MinecraftClient.getInstance().getWindow().getScaledWidth() / 7.5f),
                MinecraftClient.getInstance().getWindow().getScaledHeight() - 140,
                true
        );
    }

    @Override
    public void render(RenderType type) {
        if (nullCheck()) return;

        int y = 0;

        float maxWidth = 0;

        for (int i = 3; i > -1; i--) {
            ItemStack armorStack = mc.player.getInventory().armor.get(i);

            if (armorStack.getItem() != Items.AIR) {
                String text = ItemsUtils.getItemDurability(armorStack) + "/" + ItemsUtils.getItemMaxDurability(armorStack) + " (" + ItemsUtils.getItemDurabilityInPercentages(armorStack) + ")";

                if (mc.textRenderer.getWidth(text) > maxWidth) {
                    maxWidth = mc.textRenderer.getWidth(text);
                }

                BThackRender.drawItem(BThackRender.guiGraphics ,armorStack, (int) getX(), (int) getY() + y, null, false);
                BThackRender.drawString(text, (int) getX() + 20, (int) getY() + y, armorStack.getItemBarColor());
                BThackRender.drawRect((int) getX() + 20, (int) (getY() + y + mc.textRenderer.fontHeight), (int) getX() + 20 + 50, (int) (getY() + y + mc.textRenderer.fontHeight + 3), Color.black.hashCode());
                if (ItemsUtils.getItemDurabilityInPercentages(armorStack) > 0)
                    BThackRender.drawRect((int) getX() + 20, (int) (getY() + y + mc.textRenderer.fontHeight), (int) (getX() + 20 + (50 * (ItemsUtils.getItemDurabilityInPercentages(armorStack) / 100f))), (int) (getY() + y + mc.textRenderer.fontHeight + 3), new Color(armorStack.getItemBarColor()).hashCode());
            }

            y += 20;
        }

        this.height = y;
        this.width = 20 + maxWidth;
    }
}
