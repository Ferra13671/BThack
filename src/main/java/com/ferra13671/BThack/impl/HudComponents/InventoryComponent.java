package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class InventoryComponent extends HudComponent {

    public InventoryComponent() {
        super("Inventory",
                (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f) + (MinecraftClient.getInstance().getWindow().getScaledWidth() / 7.5f),
                MinecraftClient.getInstance().getWindow().getScaledHeight() - 60,
                true
        );

        this.width = 144;
        this.height = 48;
    }

    @Override
    public void render(RenderType type) {
        if (nullCheck()) return;

        BThackRender.drawRect((int) getX() - 3, (int) getY() - 3, (int) (getX() + width) + 3, (int) (getY() + height) + 3, new Color(20, 20, 20, 135).getRGB());
        BThackRender.drawOutlineRect((int) getX() - 4, (int) getY() - 4, (int) (getX() + width) + 4, (int) (getY() + height) + 4, 1, new Color(255, 255, 255, 125).getRGB());

        for (int i = 0; i < 27; i++) {
            ItemStack itemStack = mc.player.getInventory().main.get(i + 9);

            int offsetX = (int) getX() + (i % 9) * 16;
            int offsetY = (int) getY() + (i / 9) * 16;

            BThackRender.drawItem(BThackRender.guiGraphics, itemStack, offsetX, offsetY, null, true);
            //mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, offsetX, offsetY, null);
        }

        /*
        RenderHelper.disableStandardItemLighting();

        mc.getItemRenderer().zLevel = 0.0f;

        GlStateManager.popMatrix();

         */
    }
}
