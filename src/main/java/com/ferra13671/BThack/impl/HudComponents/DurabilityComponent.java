package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Utils.ItemsUtils;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DurabilityComponent extends HudComponent {

    private final ModeSetting mode = addMode("Info", new ArrayList<>(Arrays.asList("Normal", "Full 1", "Full 2")));

    public DurabilityComponent() {
        super("Durability",
                5,
                215,
                false
        );
    }

    @Override
    public List<Setting> initSettings() {
        return Arrays.asList(mode);
    }

    @Override
    public void render(RenderType type) {
        String text;

        switch (mode.getValue()) {
            case "Normal":
                text = "Durability: " + Formatting.WHITE + ItemsUtils.getItemDurability(mc.player.getInventory().getMainHandStack());
                drawText(text, (int) getX(), (int) getY());
                this.width = mc.textRenderer.getWidth(text);
                this.height = mc.textRenderer.fontHeight;
                break;
            case "Full 1":
                text = "Left HandD: " + Formatting.WHITE + ItemsUtils.getItemDurability(mc.player.getOffHandStack()) + Formatting.RESET + "  Right HandD: " + Formatting.WHITE + ItemsUtils.getItemDurability(mc.player.getInventory().getMainHandStack());
                drawText(text, (int) getX(), (int) getY());
                this.width = mc.textRenderer.getWidth(text);
                this.height = mc.textRenderer.fontHeight;
                break;
            case "Full 2":
                text = "Left     : " + Formatting.WHITE + ItemsUtils.getItemDurability(mc.player.getOffHandStack()) + Formatting.RESET + "  Right     :" + Formatting.WHITE + ItemsUtils.getItemDurability(mc.player.getInventory().getMainHandStack());
                drawText(text, (int) getX(), (int) getY());
                BThackRender.drawItem(BThackRender.guiGraphics, mc.player.getOffHandStack(), (int) getX() + mc.textRenderer.getWidth("Left") + 1, (int) getY() - 5, null, false);
                BThackRender.drawItem(BThackRender.guiGraphics, mc.player.getInventory().getMainHandStack(), (int) getX() + mc.textRenderer.getWidth("Left     : " + Formatting.WHITE + ItemsUtils.getItemDurability(mc.player.getOffHandStack()) + Formatting.RESET + "  Right") + 1, (int) getY() - 5, null, false);
                this.width = mc.textRenderer.getWidth(text);
                this.height = mc.textRenderer.fontHeight;
                break;
        }
    }
}
