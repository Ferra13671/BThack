package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.Events.Events.Render.RenderWorldEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockHighlight extends Module {

    public static NumberSetting renderRed;
    public static NumberSetting renderGreen;
    public static NumberSetting renderBlue;
    public static NumberSetting renderAlpha;
    public static NumberSetting linesAlpha;

    public BlockHighlight() {
        super("BlockHighlight",
                "lang.module.BlockHighlight",
                KeyboardUtils.RELEASE,
                Category.RENDER,
                false
        );

        renderRed = new NumberSetting("Render Red", this, 200, 0, 255, true);
        renderGreen = new NumberSetting("Render Green", this, 200, 0, 255, true);
        renderBlue = new NumberSetting("Render Blue", this, 200, 0, 255, true);
        renderAlpha = new NumberSetting("Render Alpha", this, 220, 0, 255, true);
        linesAlpha = new NumberSetting("Lines Alpha", this, 255, 0, 255, true);

        initSettings(
                renderRed,
                renderGreen,
                renderBlue,
                renderAlpha,
                linesAlpha
        );
    }

    @EventSubscriber
    public void onWorldRender(RenderWorldEvent.Last e) {
        if (nullCheck()) return;

        if (mc.crosshairTarget != null && mc.crosshairTarget instanceof BlockHitResult blockHitResult) {
            float red = (float) renderRed.getValue() / 255f;
            float green = (float) renderGreen.getValue() / 255f;
            float blue = (float) renderBlue.getValue() / 255f;
            float alpha = (float) renderAlpha.getValue() / 255f;
            float lAlpha = (float) linesAlpha.getValue() / 255f;

            if (BuildManager.airs.contains(mc.world.getBlockState(blockHitResult.getBlockPos()).getBlock())) return;

            Box box = BlockUtils.getBoundingBox(blockHitResult.getBlockPos());
            if (box == null) {
                return;
            }

            BThackRender.boxRender.prepareBoxRender();
            BThackRender.boxRender.renderBoxes(new ArrayList<>(Arrays.asList(new RenderBox(box, red, green, blue, lAlpha, red, green, blue, alpha))));
            BThackRender.boxRender.stopBoxRender();
        }
    }
}
