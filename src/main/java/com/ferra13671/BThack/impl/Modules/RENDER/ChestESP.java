package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.Core.Render.Line.RenderLine;
import com.ferra13671.BThack.Events.Events.Render.RenderWorldEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.*;
import net.minecraft.block.enums.ChestType;
import net.minecraft.util.math.Box;

import java.util.ArrayList;

public class ChestESP extends Module {

    public static BooleanSetting chests;
    public static BooleanSetting enderChests;
    public static BooleanSetting shulkers;
    public static BooleanSetting barrels;
    public static BooleanSetting hoppers;
    public static BooleanSetting droppers;
    public static BooleanSetting dispensers;

    public static BooleanSetting tracers;
    public static NumberSetting renderRange;

    public ChestESP() {
        super("ChestESP",
                "lang.module.ChestESP",
                KeyboardUtils.RELEASE,
                Category.RENDER,
                false
        );

        chests = new BooleanSetting("Chests", this, true);
        enderChests = new BooleanSetting("EnderChests", this, true);
        shulkers = new BooleanSetting("Shulkers", this, true);
        barrels = new BooleanSetting("Barrels", this, true);
        hoppers = new BooleanSetting("Hoppers", this, false);
        droppers = new BooleanSetting("Droppers", this, false);
        dispensers = new BooleanSetting("Dispensers", this, false);

        tracers = new BooleanSetting("Tracers", this, false);
        renderRange = new NumberSetting("Range", this, 200, 50, 600, false);

        initSettings(
                chests,
                enderChests,
                shulkers,
                barrels,
                hoppers,
                droppers,
                dispensers,
                tracers,
                renderRange
        );
    }

    @EventSubscriber
    public void onRender(RenderWorldEvent.End e) {
        if (nullCheck()) return;

        ArrayList<RenderBox> boxes = new ArrayList<>();

        for (BlockEntity entity : ChunkUtils.getLoadedBlockEntitiesOnArrayList()) {
            if (MathUtils.getDistance(mc.player.getPos(), entity.getPos().toCenterPos()) > renderRange.getValue()) return;

            Box box;

            box = BlockUtils.getBox(entity);
            if (box == null) continue;

            if (entity instanceof ChestBlockEntity && chests.getValue()) {
                BlockState state = entity.getCachedState();
                ChestType chestType = state.get(ChestBlock.CHEST_TYPE);

                if (chestType == ChestType.LEFT) continue;

                boxes.add(new RenderBox(box, 1f, 0.39f, 0, 0.6f, 1f, 0.53f, 0, 0.4f));
            }
            if (entity instanceof EnderChestBlockEntity && enderChests.getValue()) {
                boxes.add(new RenderBox(box, 0.8f, 0, 0.8f, 0.6f, 0.8f, 0, 0.8f, 0.4f));
            }
            if (entity instanceof ShulkerBoxBlockEntity && shulkers.getValue()) {
                boxes.add(new RenderBox(box, 1, 0.45f, 1, 0.6f, 1, 0.3f, 1, 0.4f));
            }
            if (entity instanceof BarrelBlockEntity && barrels.getValue()) {
                boxes.add(new RenderBox(box, 0.65f, 0.253f, 0, 0.6f, 0.65f, 0.253f, 0, 0.4f));
            }
            if (entity instanceof HopperBlockEntity && hoppers.getValue()) {
                boxes.add(new RenderBox(box, 0.5f, 0.5f, 0.5f, 0.6f, 0.5f, 0.5f, 0.5f, 0.4f));
            }
            if (entity instanceof DropperBlockEntity && droppers.getValue()) {
                boxes.add(new RenderBox(box, 0.7f, 0.7f, 0.7f, 0.6f, 0.7f, 0.7f, 0.7f, 0.4f));
            }
            if (entity instanceof DispenserBlockEntity && dispensers.getValue()) {
                boxes.add(new RenderBox(box, 0.7f, 0.7f, 0.7f, 0.6f, 0.7f, 0.7f, 0.7f, 0.4f));
            }
        }

        BThackRender.boxRender.prepareBoxRender();
        BThackRender.boxRender.renderBoxes(boxes);
        BThackRender.boxRender.stopBoxRender();
    }

    @EventSubscriber(priority = -1)
    public void onRender2(RenderWorldEvent.Last e) {
        if (!tracers.getValue()) return;

        ArrayList<RenderLine> lines = new ArrayList<>();

        for (BlockEntity entity : ChunkUtils.getLoadedBlockEntitiesOnArrayList()) {
            if (entity instanceof ChestBlockEntity && chests.getValue()) {
                lines.add(new RenderLine(entity, 1, 0.39f, 0, 1f));
            }
            if (entity instanceof EnderChestBlockEntity && enderChests.getValue()) {
                lines.add(new RenderLine(entity, 0.8f, 0, 0.8f, 1f));
            }
            if (entity instanceof ShulkerBoxBlockEntity && shulkers.getValue()) {
                lines.add(new RenderLine(entity, 1f, 0.45f, 1f, 1f));
            }
            if (entity instanceof BarrelBlockEntity && barrels.getValue()) {
                lines.add(new RenderLine(entity, 0.65f, 0.253f, 0, 1f));
            }
            if (entity instanceof HopperBlockEntity && hoppers.getValue()) {
                lines.add(new RenderLine(entity, 0.5f, 0.5f, 0.5f, 1));
            }
            if (entity instanceof DropperBlockEntity && droppers.getValue()) {
                lines.add(new RenderLine(entity, 0.7f, 0.7f, 0.7f, 1f));
            }
            if (entity instanceof DispenserBlockEntity && dispensers.getValue()) {
                lines.add(new RenderLine(entity, 0.7f, 0.7f, 0.7f, 1));
            }
        }

        BThackRender.lineRender.prepareLineRenderer();
        BThackRender.lineRender.renderLines(lines);
        BThackRender.lineRender.stopLineRenderer();
    }
}