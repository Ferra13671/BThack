package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.util.collection.DefaultedList;

import java.awt.*;



public class Tooltips extends Module {

    public static BooleanSetting shulkers;
    public static BooleanSetting maps;

    public Tooltips() {
        super("Tooltips",
                "lang.module.Tooltips",
                KeyboardUtils.RELEASE,
                MCategory.RENDER,
                false
        );

        shulkers = new BooleanSetting("Shulkers", this, true);
        maps = new BooleanSetting("Maps", this, true);

        initSettings(
                shulkers,
                maps
        );
    }

    public static void renderShulkerTooltip(ItemStack itemStack ,DefaultedList<ItemStack> stacks, int x, int y) {

        BThackRender.guiGraphics.getMatrices().push();

        BThackRender.guiGraphics.getMatrices().translate(0f, 0f, 600f);

        BThackRender.guiGraphics.fill(x + 7, y - 22, x + 159, y + 49, Color.white.hashCode());
        BThackRender.guiGraphics.fill(x + 8, y - 21, x + 158, y - 6, ColorUtils.rainbow(100));
        BThackRender.guiGraphics.fillGradient(x + 8, y - 6, x + 158, y + 48, new Color(5, 5, 5, 225).hashCode(), new Color(50, 50, 50, 225).hashCode());

        BThackRender.drawString(itemStack.getName().getString(), x + 10, y - 18, -1);

        for (int i = 0; i < 27; i++) {
            int offsetX = x + (i % 9) * 16 + 11;
            int offsetY = y + (i / 9) * 16 - 3;

            ItemStack stack = stacks.get(i);

            BThackRender.drawItem(BThackRender.guiGraphics, stack, offsetX, offsetY, null, true);
        }

        BThackRender.guiGraphics.getMatrices().pop();
    }

    public static void renderMapTooltip(DrawContext context, ItemStack stack, int x, int y) {
        MinecraftClient mc = MinecraftClient.getInstance();

        RenderSystem.enableBlend();
        context.getMatrices().push();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int y1 = y - 10;
        int y2 = y1 + 116;
        int x1 = x + 8;
        int x2 = x1 + 118;
        int z = 601;

        MapState mapState = FilledMapItem.getMapState(stack, mc.world);

        if (mapState != null) {
            mapState.getPlayerSyncData(mc.player);
            double scale = 0.8;
            context.getMatrices().translate(x + 16, y - 4, z);
            context.getMatrices().scale((float) scale, (float) scale, 0);

            BThackRender.guiGraphics.getMatrices().push();

            BThackRender.guiGraphics.getMatrices().translate(0f, 0f, 600f);
            BThackRender.guiGraphics.fillGradient(x1, y1 - 10, x2, y2, new Color(5, 5, 5, 255).hashCode(), new Color(100, 100, 100, 255).hashCode());
            BThackRender.drawOutlineRect(x1, y1 - 10, x2, y2, 1, ColorUtils.rainbow(100));

            BThackRender.guiGraphics.getMatrices().scale(0.75f, 0.75f, 0.75f);
            BThackRender.drawString(stack.getItem().getName().getString(), (int) ((x1 + 5) * 1.3333), (int) ((y1 - 5) * 1.3333), -1);

            BThackRender.guiGraphics.getMatrices().pop();

            VertexConsumerProvider.Immediate consumer = mc.getBufferBuilders().getEntityVertexConsumers();
            mc.gameRenderer.getMapRenderer().draw(context.getMatrices(), consumer, FilledMapItem.getMapId(stack), mapState, false, 0xF000F0);
        }
        context.getMatrices().pop();
    }
}
