package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Box.RenderBox;
import com.ferra13671.BThack.Events.Render.RenderWorldEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.EntityUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ESP extends Module {

    public static ModeSetting mode;
    public static BooleanSetting players;
    public static BooleanSetting items;
    public static BooleanSetting monsters;
    public static BooleanSetting animals;


    private boolean need = true;

    public static List<Entity> glowed = new ArrayList<>();

    public ESP() {
        super("ESP",
                "lang.module.ESP",
                KeyboardUtils.RELEASE,
                Category.RENDER,
                false
        );

        mode = new ModeSetting("Mode", this, new ArrayList<>(Arrays.asList("Box", "Glow")));

        players = new BooleanSetting("Players", this, true);
        items = new BooleanSetting("Items", this, true);
        monsters = new BooleanSetting("Monsters", this, true);
        animals = new BooleanSetting("Animals", this, true);

        initSettings(
                mode,
                players,
                items,
                monsters,
                animals
        );
    }

    @EventSubscriber
    public void onRender(RenderWorldEvent.Last e) {
        if (nullCheck()) return;

        String _mode = mode.getValue();

        arrayListInfo = _mode;

        switch (_mode) {
            case "Box":
                if (need) {
                    for (Entity entity : glowed) {
                        entity.setGlowing(false);
                    }
                    glowed.clear();
                    need = false;
                }

                ArrayList<RenderBox> renderBoxes = new ArrayList<>();

                for (PlayerEntity entity : mc.world.getPlayers()) {
                    if (players.getValue() && entity != mc.player && entity != null) {
                        renderBoxes.add(new RenderBox(EntityUtils.getLerpedBox(entity, mc.getTickDelta()), 1,1,1, 0.6F,1,1,1, 0.2F));
                    }
                }
                for (Entity mob : mc.world.getEntities()) {
                    if (items.getValue() && mob instanceof ItemEntity) {
                        renderBoxes.add(new RenderBox(EntityUtils.getLerpedBox(mob, mc.getTickDelta()), 0.59F,0.59F, 1,0.6F,0.59F,0.59F,1, 0.2F));
                    }
                    if (monsters.getValue() && (mob instanceof MobEntity || mob instanceof GolemEntity)) {
                        renderBoxes.add(new RenderBox(EntityUtils.getLerpedBox(mob, mc.getTickDelta()), 0.83F,0.92F,0.17F, 0.6F,0.83F,0.92F,0.17F, 0.2F));
                    }
                    if (animals.getValue() && mob instanceof PassiveEntity) {
                        renderBoxes.add(new RenderBox(EntityUtils.getLerpedBox(mob, mc.getTickDelta()), 0.69F,1,0.34F, 0.6F,0.69F,1,0.34F, 0.2F));
                    }
                }

                BThackRender.boxRender.prepareBoxRender();

                BThackRender.boxRender.renderBoxes(renderBoxes);

                BThackRender.boxRender.stopBoxRender();

                break;
            case "Glow":
                need = true;
                for (PlayerEntity playerEntity : mc.world.getPlayers()) {
                    if (playerEntity != mc.player && playerEntity != glowed && !playerEntity.isDead()) {
                        playerEntity.setGlowing(true);
                        glowed.add(playerEntity);
                    }
                }
                for (Entity mob : mc.world.getEntities()) {
                    if (mob != glowed) {
                        if (mob instanceof ItemEntity) {
                            mob.setGlowing(true);
                            glowed.add(mob);
                        }
                        if (mob instanceof MobEntity && mob.isAlive()) {
                            mob.setGlowing(true);
                            glowed.add(mob);
                        }
                        if (mob instanceof WaterCreatureEntity && mob.isAlive()) {
                            mob.setGlowing(true);
                            glowed.add(mob);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onDisable() {
        if (nullCheck()) return;

        for (Entity entity : glowed) {
            entity.setGlowing(false);
        }
        glowed.clear();
        need = true;

        super.onDisable();
    }
}
