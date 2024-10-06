package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Line.RenderLine;
import com.ferra13671.BThack.Events.Events.Render.RenderWorldEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Clans.Clan;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Social.Enemies.EnemiesUtils;
import com.ferra13671.BThack.api.Social.Friends.FriendsUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;

public class Tracers extends Module {

    public static BooleanSetting players;
    public static BooleanSetting mobs;
    public static BooleanSetting animals;
    public static BooleanSetting items;

    public Tracers() {
        super("Tracers",
                "lang.module.Tracers",
                KeyboardUtils.RELEASE,
                Category.RENDER,
                false
        );

        players = new BooleanSetting("Players", this, true);
        mobs = new BooleanSetting("Mobs", this, true);
        animals = new BooleanSetting("Animals", this, true);
        items = new BooleanSetting("Items", this, false);

        initSettings(
                players,
                mobs,
                animals,
                items
        );
    }

    @EventSubscriber(priority = -1)
    @SuppressWarnings("unused")
    public void onRender(RenderWorldEvent.End e) {
        if (nullCheck()) return;

        ArrayList<RenderLine> lines = new ArrayList<>();

        if (players.getValue()) {
            for (PlayerEntity playerEntity : mc.world.getPlayers()) {
                if (playerEntity != null && playerEntity != mc.player && !playerEntity.isDead()) {
                    String name = playerEntity.getDisplayName().getString();
                    if (FriendsUtils.isFriend(name)) {
                        lines.add(new RenderLine(playerEntity, 0.03f, 0.96f, 0.86f, 1f));
                    } else if (EnemiesUtils.isEnemy(name)) {
                        lines.add(new RenderLine(playerEntity, 1f, 0, 0, 1f));
                    } else if (ClansUtils.isAlly(name)) {
                        Clan clan = ClansUtils.getFirstClanFromMember(name);
                        if (clan != null) {
                            lines.add(new RenderLine(playerEntity, clan.getR(), clan.getG(), clan.getB(), 1f));
                        } else {
                            lines.add(new RenderLine(playerEntity, 1f, 1f, 1f, 1f));
                        }
                    } else {
                        lines.add(new RenderLine(playerEntity, 1f, 1f, 1f, 1f));
                    }
                }
            }
        }

        if (mobs.getValue() || animals.getValue() || items.getValue()) {
            for (Entity entity : mc.world.getEntities()) {
                if (mobs.getValue() && KillAuraUtils.isCurrentMonster(entity)) {
                    lines.add(new RenderLine(entity, 0.83f,0.92f,0.17f, 1f));
                } else if (animals.getValue() && KillAuraUtils.isCurrentAnimal(entity)) {
                    lines.add(new RenderLine(entity, 0.69f,1f,0.34f, 1f));
                } else if (items.getValue() && entity instanceof ItemEntity) {
                    lines.add(new RenderLine(entity, 0.59f,0.59f, 1f, 1f));
                }
            }
        }

        BThackRender.lineRender.prepareLineRenderer();
        BThackRender.lineRender.renderLines(lines);
        BThackRender.lineRender.stopLineRenderer();
    }
}
